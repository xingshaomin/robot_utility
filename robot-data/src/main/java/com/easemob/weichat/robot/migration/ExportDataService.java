/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2015. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package com.easemob.weichat.robot.migration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easemob.weichat.models.entity.ChatMessage;
import com.easemob.weichat.models.entity.ServiceSession;
import com.easemob.weichat.models.message.EasemobImageMessageBody;
import com.easemob.weichat.models.message.EasemobMessageBody;
import com.easemob.weichat.models.message.EasemobTxtMessageBody;
import com.easemob.weichat.persistence.jdbc.JdbcChatMessageRepositoryProvider;
import com.easemob.weichat.persistence.jdbc.JdbcServiceSessionRepositoryProvider;
import com.easemob.weichat.robot.migration.jdbc.JdbcServiceSessionRepositoryExtProvider;

import lombok.extern.slf4j.Slf4j;


/**
 * @author shawn
 *
 */
@Slf4j
@Service
public class ExportDataService {
    @Autowired
    private JdbcChatMessageRepositoryProvider chatMessageRepositoryProvider;
    
    @Autowired
    private JdbcServiceSessionRepositoryExtProvider serviceSessionRepositoryProvider;
    
    public List<ChatMessage> getChatMessageByServiceSessionId(int tenantId, String sessionServiceId){
        return chatMessageRepositoryProvider.getAllMessageRecordByServiceSessionId(tenantId, sessionServiceId);
    }
    
    public List<ServiceSession> getServiceSessions(int page, int pageSize, String startDate, String endDate){
        return serviceSessionRepositoryProvider.getServiceSessionList(page, pageSize, startDate, endDate);
    }
    
    public List<ServiceSession> getServiceSessionsByTenantId(int tenantId, int page, int pageSize, String startDate, String endDate){
        return serviceSessionRepositoryProvider.getServiceSessionListByTenantId(tenantId, page, pageSize, startDate, endDate);
    }
    
    public int getTotalCount(String startDate, String endDate){
        return serviceSessionRepositoryProvider.getTotalCount(startDate, endDate);
    }
    
    public int getTotalCountByTenantId(int tenantId, String startDate, String endDate){
        return serviceSessionRepositoryProvider.getTotalCountByTenantId(tenantId, startDate, endDate);
    }
    
    public void exportDataByTenantId(String path, String start, String end, int tenantId) {
        int count = getTotalCountByTenantId(tenantId, start, end);
        log.info("total service session is {}", count);
        final int pageSize = count;
        int pages = count/pageSize;
        if(count % pageSize != 0){
            pages++;
        }
        for (int i = 0; i < pages; i++) {
            log.info("start export the {} service session", (i+1) * pageSize);
            List<ServiceSession> ssList = getServiceSessionsByTenantId(tenantId, i, pageSize, start, end);
            exportServiceSessionList(path, ssList);
        }
    }
    
    public void exportAllTenantsData(String path, String start, String end) {
        int count = getTotalCount(start, end);
        final int pageSize = 1000;
        int pages = count/pageSize;
        if(count % pageSize != 0){
            pages++;
        }
        for (int i = 0; i < pages; i++) {
            List<ServiceSession> ssList = getServiceSessions(i, pageSize, start, end);
            exportServiceSessionList(path, ssList);
        }
    }
    
    public void exportServiceSessionList(String path,
            List<ServiceSession> ssList) {
        for (ServiceSession serviceSession : ssList) {
            log.info("start exporting service session {}", serviceSession.getServiceSessionId());
            List<ChatMessage> msgs = getChatMessageByServiceSessionId(serviceSession.getTenantId(), serviceSession.getServiceSessionId());
            File d = new File(path+"/"+serviceSession.getTenantId());
            if(!d.exists()){
                d.mkdirs();
            };
            Date date = serviceSession.getCreateDatetime();
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String sencondPath = formatter1.format(date);
            String fileName = formatter2.format(date);
            writeToFile(d.getAbsolutePath(), sencondPath, fileName, msgs);
            log.info("finish writting service session {} into {}", serviceSession.getServiceSessionId(), d.getAbsolutePath() + "/" + sencondPath);
        }
    }
    
    private void writeToFile(String path, String sencondPath, String filename, List<ChatMessage> messageList){
        try {
            File d = new File(path+"/"+sencondPath);
            if(!d.exists()){
                d.mkdirs();
            };
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    new File(String.format(d.getAbsolutePath() + "/" + "%s.txt", filename))));
            for (ChatMessage chatMessage : messageList) {
                String from = chatMessage.getFromUser().getUserType().toString();
                String fromUserId = chatMessage.getFromUser().getUserId();
                String msgId = chatMessage.getMsgId();
                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String createTime = formatter3.format(chatMessage.getCreateDateTime());
                String extContent = "";
                Object ext = chatMessage.getBody().getExt();
                if(ext != null){
                    extContent = ext.toString();
                }
                for (EasemobMessageBody messageBody : chatMessage.getBody().getBodies()) {
                    String type = messageBody.getType();
                    String content = null;
                    if(type.equals("txt")){
                        EasemobTxtMessageBody txt = (EasemobTxtMessageBody)messageBody;
                        content =txt.getMsg();
                    } else if (type.equals("img")){
                        EasemobImageMessageBody img = (EasemobImageMessageBody)messageBody;
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
}
