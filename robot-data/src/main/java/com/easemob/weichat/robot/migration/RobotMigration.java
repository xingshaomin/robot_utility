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

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.easemob.weichat.models.entity.ChatMessage;
import com.easemob.weichat.models.entity.ServiceSession;
import com.easemob.weichat.models.message.EasemobImageMessageBody;
import com.easemob.weichat.models.message.EasemobMessageBody;
import com.easemob.weichat.models.message.EasemobTxtMessageBody;
import com.easemob.weichat.service.robot.exception.RobotException;

import lombok.extern.slf4j.Slf4j;


/**
 * 用于做版本升级时机器人数据migration
 * 
 * @author shawn
 * 
 */
@Slf4j
@SpringBootApplication
@ComponentScan("com.easemob.weichat")
public class RobotMigration {
	private static final String COMMAND_LINE = "Java -jar kefu-robot-migration.jar -v 29";
	private static final String INVALID_PARAM = "invalid parameter, the command should be like " + COMMAND_LINE;
	
	public static void main(String[] args){
	    // 启动spring application
		SpringApplication sa = new SpringApplication(RobotMigration.class);
		// disable jetty
		sa.setWebEnvironment(false);
		// get application context
		ConfigurableApplicationContext context = sa.run(args);
		
		// 1. migrate all menu
//		migrateMenu(args, context);
		
		// 2. delete all rules except menu
//		deleteAllRules(context);
		
		// 3. export data
		// "Java -jar kefu-robot-migration.jar /user/local/ 20151101 20151130 1441"
		if(args.length != 4){
            throw new RobotException(INVALID_PARAM);
        }
        String path = args[0];
        String start = args[1];
        String end = args[2];
        String tenantId = args[3];
//        exportAllTenantsData(context, path, start, end);
        exportDataByTenantId(context, Integer.parseInt(tenantId), path, start, end);
    }

    private static void deleteAllRules(ConfigurableApplicationContext context) {
        RobotRulesDataSerivce dataService = context.getBean(RobotRulesDataSerivce.class);
		dataService.deleteAllRules(1441);
    }

    private static void migrateMenu(String[] args, ConfigurableApplicationContext context) {
        RobotMenuMigrationSerivce migrationService = context.getBean(RobotMenuMigrationSerivce.class);

		if(args.length != 2){
			throw new RobotException(INVALID_PARAM);
		}
		String option = args[0];
		String currentVersion = args[1];
		// only do migration when the version is 29
		if("-v".equals(option) && "29".equals(currentVersion)){
			migrationService.doMigrationFrom23To29();
		} else {
			throw new RobotException(INVALID_PARAM);
		}
    }
    
    private static void exportAllTenantsData(ConfigurableApplicationContext context, String path, String start, String end) {
        ExportDataService exportService = context.getBean(ExportDataService.class);
        int count = exportService.getTotalCount(start, end);
        final int pageSize = 1000;
        int pages = count/pageSize;
        if(count % pageSize != 0){
            pages++;
        }
        for (int i = 0; i < pages; i++) {
            List<ServiceSession> ssList = exportService.getServiceSessions(i, pageSize, start, end);
            exportServiceSessionList(path, exportService, ssList);
        }
    }
    
    private static void exportDataByTenantId(ConfigurableApplicationContext context, int tenantId, String path, String start, String end) {
        ExportDataService exportService = context.getBean(ExportDataService.class);
        int count = exportService.getTotalCountByTenantId(tenantId, start, end);
        log.info("total service session is {}", count);
        final int pageSize = 1000;
        int pages = count/pageSize;
        if(count % pageSize != 0){
            pages++;
        }
        for (int i = 0; i < pages; i++) {
            log.info("start export the {} service session", (pages+1) * 1000);
            List<ServiceSession> ssList = exportService.getServiceSessionsByTenantId(tenantId, i, pageSize, start, end);
            exportServiceSessionList(path, exportService, ssList);
        }
    }

    private static void exportServiceSessionList(String path, ExportDataService exportService,
            List<ServiceSession> ssList) {
        for (ServiceSession serviceSession : ssList) {
            log.info("start exporting service session {}", serviceSession.getServiceSessionId());
            List<ChatMessage> msgs = exportService.getChatMessageByServiceSessionId(serviceSession.getTenantId(), serviceSession.getServiceSessionId());
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
    
    private static void writeToFile(String path, String sencondPath, String filename, List<ChatMessage> messageList){
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
                for (EasemobMessageBody messageBody : chatMessage.getBody().getBodies()) {
                    String type = messageBody.getType();
                    String content = null;
                    if(type.equals("txt")){
                        EasemobTxtMessageBody txt = (EasemobTxtMessageBody)messageBody;
                        content =txt.getMsg();
                    } else if (type.equals("img")){
                        EasemobImageMessageBody img = (EasemobImageMessageBody)messageBody;
                        content = img.getUri();
                    }
                    String chat = String.format("%36s |%24s |%36s |%10s | %s", msgId, createTime, fromUserId, from, content);   
                    writer.write(chat + "\r\n");
                }
            }
            writer.close();
        } catch (Exception e) {

        }
    }
}
