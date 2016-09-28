/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2015. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package com.easemob.weichat.robot.migration;

import com.easemob.weichat.models.entity.ChatMessage;
import com.easemob.weichat.models.entity.Enquiry;
import com.easemob.weichat.models.entity.ServiceSession;
import com.easemob.weichat.models.message.EasemobImageMessageBody;
import com.easemob.weichat.models.message.EasemobMessageBody;
import com.easemob.weichat.models.message.EasemobTxtMessageBody;
import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.robot.migration.jdbc.JdbcChatMessageRepository;
import com.easemob.weichat.robot.migration.jdbc.JdbcEnquiryRepository;
import com.easemob.weichat.robot.migration.jdbc.JdbcServiceSessionRepositoryExtProvider;
import com.easemob.weichat.robot.migration.utils.date.DateUtils;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;


/**
 * @author shawn
 *
 */
@Slf4j
@Service
public class ExportDataService {
    @Autowired
    private JdbcChatMessageRepository chatMessageRepository;

    @Autowired
    private JdbcServiceSessionRepositoryExtProvider serviceSessionRepositoryProvider;

    @Autowired
    private JdbcEnquiryRepository enquiryRepo;

    public List<Enquiry> getEnquiriesByServiceSessionId(int tenantId, String sessionServiceId) {
        return enquiryRepo.findByTenantIdAndServiceSessionId(tenantId, sessionServiceId);
    }

    public List<ChatMessage> getChatMessageByServiceSessionId(int tenantId, String sessionServiceId) {
        return chatMessageRepository.getAllMessageRecordByServiceSessionId(tenantId, sessionServiceId);
    }

    public List<ServiceSession> getServiceSessions(int page, int pageSize, String startDate, String endDate) {
        return serviceSessionRepositoryProvider.getServiceSessionList(page, pageSize, startDate, endDate);
    }

    public List<ServiceSession> getServiceSessionsByTenantId(int tenantId, int page, int pageSize, String startDate, String endDate) {
        return serviceSessionRepositoryProvider.getServiceSessionListByTenantId(tenantId, page, pageSize, startDate, endDate);
    }

    public int getTotalCount(String startDate, String endDate) {
        return serviceSessionRepositoryProvider.getTotalCount(startDate, endDate);
    }

    public int getTotalCountByTenantId(int tenantId, String startDate, String endDate) {
        return serviceSessionRepositoryProvider.getTotalCountByTenantId(tenantId, startDate, endDate);
    }


    public void exportSatisfactionDataByTenantId(String path, String style, String start, String end, int tenantId) {
        int count = getTotalCountByTenantId(tenantId, start, end);
        if (count == 0) {
            return;
        }
        log.info("total service session is {}", count);
        final int pageSize = count;
        int pages = count / pageSize;
        if (count % pageSize != 0) {
            pages++;
        }
        for (int i = 0; i < pages; i++) {
            log.info("start export the {} service session", (i + 1) * pageSize);
            List<ServiceSession> ssList = getServiceSessionsByTenantId(tenantId, i, pageSize, start, end);
            switch (OutputStyle.findByName(style)) {
                case VERTICAL:
                    exportServiceSessionListAndSatisfaction(path, ssList);
                    break;
                case JSON:
                    exportServiceSessionListAndSatisfactionJson(path, ssList);
                    break;
                default:
                    break;
            }
        }
    }


    public void exportServiceSessionListAndSatisfaction(String path,
                                                        List<ServiceSession> ssList) {
        for (ServiceSession serviceSession : ssList) {
            List<Enquiry> enquiries = getEnquiriesByServiceSessionId(serviceSession.getTenantId(), serviceSession.getServiceSessionId());
            if (enquiries != null && enquiries.size() > 0) {
                log.info("start exporting service session {}", serviceSession.getServiceSessionId());
                List<ChatMessage> msgs = getChatMessageByServiceSessionId(serviceSession.getTenantId(), serviceSession.getServiceSessionId());
                if (msgs == null || msgs.isEmpty()) {
                    continue;
                }
                File d = new File(path + "/" + serviceSession.getTenantId());
                if (!d.exists()) {
                    d.mkdirs();
                }
                ;
                Date date = serviceSession.getCreateDatetime();
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String sencondPath = formatter1.format(date);
                String fileName = formatter2.format(date);
                writeToFile(d.getAbsolutePath(), sencondPath, fileName, msgs, enquiries);
                log.info("finish writting service session {} into {}", serviceSession.getServiceSessionId(), d.getAbsolutePath() + "/" + sencondPath);
            }

        }
    }

    private void writeToFile(String path, String sencondPath, String filename, List<ChatMessage> messageList, List<Enquiry> enquiries) {
        try {
            File d = new File(path + "/" + sencondPath);
            if (!d.exists()) {
                d.mkdirs();
            }
            ;
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    new File(String.format(d.getAbsolutePath() + "/" + "%s.txt", filename))));
            if (messageList != null && !messageList.isEmpty()) {
                String chat = String.format("ssid: %s", messageList.get(0).getSessionServiceId());
                writer.write(chat + "\r\n\r\n");
            }

            for (Enquiry enquiry : enquiries) {
                writer.write("enquiry:" + "\r\n");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String createTime = formatter.format(enquiry.getCreateDateTime());
                int inviteId = enquiry.getInviteId();
                String summary = enquiry.getSummary();
                String detail = enquiry.getDetail();
                String chat = String.format("%24s |%10s | %10s| %s", createTime, inviteId, summary, detail);
                writer.write(chat + "\r\n");
            }

            writer.write("-----------------------------------------------------------------------\r\n");

            for (ChatMessage chatMessage : messageList) {
                String from = chatMessage.getFromUser().getUserType().toString();
                String fromUserId = chatMessage.getFromUser().getUserId();
                String msgId = chatMessage.getMsgId();
                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String createTime = formatter3.format(chatMessage.getCreateDateTime());
                String extContent = "";
                Object ext = chatMessage.getBody().getExt();
                if (ext != null) {
                    extContent = ext.toString();
                }
                for (EasemobMessageBody messageBody : chatMessage.getBody().getBodies()) {
                    String type = messageBody.getType();
                    String content = null;
                    if (type.equals("txt")) {
                        EasemobTxtMessageBody txt = (EasemobTxtMessageBody) messageBody;
                        content = txt.getMsg();
                    } else if (type.equals("img")) {
                        EasemobImageMessageBody img = (EasemobImageMessageBody) messageBody;
                        content = img.getUrl();
                    }
                    String chat = String.format("%36s |%24s |%36s |%10s | %10s| %s", msgId, createTime, fromUserId, from, content, extContent);
                    writer.write(chat + "\r\n");
                }
            }
            writer.close();
        } catch (Exception e) {

        }
    }

    /**
     * @author dongwt
     * @param path
     * @param ssList
     */
    public void exportServiceSessionListAndSatisfactionJson(String path,
                                                            List<ServiceSession> ssList) {
        for (ServiceSession serviceSession : ssList) {
            log.info("start exporting service session {}", serviceSession.getServiceSessionId());
            List<ChatMessage> msgs = getChatMessageByServiceSessionId(serviceSession.getTenantId(), serviceSession.getServiceSessionId());

            log.info("get message count {}",null==msgs?0:msgs.size());
            if (msgs == null || msgs.isEmpty()) {
                continue;
            }
            File d = new File(path);

            Date date = serviceSession.getCreateDatetime();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String fileName = format.format(date);

            writeToFileJson(d.getAbsolutePath(), fileName, msgs, serviceSession);


            log.info("finish writting service session {} into {}", serviceSession.getServiceSessionId(), d.getAbsolutePath());

        }
    }

    /**
     * @author dongwt
     * @param path
     * @param filename
     * @param messageList
     * @param serviceSession
     */
    private void writeToFileJson(String path, String filename, List<ChatMessage> messageList, ServiceSession serviceSession) {

        BufferedOutputStream outputStream = null;

        try {
            String fullFileName = String.format(path + "/" + "%s.txt.gz", filename);
            outputStream = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(fullFileName, true)));

            ObjectNode sessionChatNode = JSONUtil.getObjectMapper().createObjectNode();
            ObjectNode serviceSessionNode = JSONUtil.getObjectMapper().createObjectNode();
            serviceSessionNode.put("serviceSessionId", serviceSession.getServiceSessionId());
            serviceSessionNode.put("tenantId", serviceSession.getTenantId());
            serviceSessionNode.put("agentUserId", serviceSession.getAgentUserId());
            serviceSessionNode.put("agentUserType", serviceSession.getAgentUserType());
            serviceSessionNode.put("startDateTime", DateUtils.format(serviceSession.getStartDateTime()));
            serviceSessionNode.put("stopDateTime", DateUtils.format(serviceSession.getStopDateTime()));

            sessionChatNode.put("serviceSession", serviceSessionNode);


            ArrayNode chatListNode = JSONUtil.getObjectMapper().createArrayNode();
            for (ChatMessage chatMessage : messageList) {
                ObjectNode chatMsgNode = JSONUtil.getObjectMapper().createObjectNode();
                chatMsgNode.put("tenantId", chatMessage.getTenantId());
                chatMsgNode.put("sessionServiceId", chatMessage.getSessionServiceId());
                chatMsgNode.put("createDateTime", DateUtils.format(chatMessage.getCreateDateTime()));

                ArrayNode msgNode = JSONUtil.getObjectMapper().createArrayNode();
                for (EasemobMessageBody messageBody : chatMessage.getBody().getBodies()) {
                    if (!messageBody.getType().equals("txt")) {
                        continue;
                    }
                    msgNode.add(((EasemobTxtMessageBody) messageBody).getMsg());
                }
                chatMsgNode.put("chatMsg", msgNode);
                chatListNode.add(chatMsgNode);
            }

            log.info("start writting service session {} into {}", serviceSession.getServiceSessionId(),fullFileName);
            sessionChatNode.put("chatList", chatListNode);
            outputStream.write((sessionChatNode.toString() + "\r\n").getBytes());
            outputStream.flush();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
