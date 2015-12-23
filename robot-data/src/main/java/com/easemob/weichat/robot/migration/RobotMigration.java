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
//      exportAllTenantsData(args, context);
        exportDataByTenantId(args, context);
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
    
    private static void exportAllTenantsData(String[] args, ConfigurableApplicationContext context) {
        if(args.length != 3){
            throw new RobotException(INVALID_PARAM);
        }
        String path = args[0];
        String start = args[1];
        String end = args[2];
        
        ExportDataService exportService = context.getBean(ExportDataService.class);
        exportService.exportAllTenantsData(path, start, end);
    }
    
    private static void exportDataByTenantId(String[] args, ConfigurableApplicationContext context) {
        if(args.length < 4){
            throw new RobotException(INVALID_PARAM);
        }
        String path = args[0];
        String start = args[1];
        String end = args[2];
        for (int i = 3; i < args.length; i++) {
            String tenantIdStr = args[i];
            int tenantId = Integer.parseInt(tenantIdStr);
            
            ExportDataService exportService = context.getBean(ExportDataService.class);
            exportService.exportDataByTenantId(path, start, end, tenantId);
        }
    }

    
}
