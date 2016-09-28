/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2015. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package com.easemob.weichat.robot.migration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

//import com.easemob.weichat.models.entity.robot.RobotMenuRuleItem;


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
	
	private static ConfigurableApplicationContext context;

    public static void main(String[] args){
	    // 启动spring application
		SpringApplication sa = new SpringApplication(RobotMigration.class);
		// disable jetty
		sa.setWebEnvironment(false);
		context = sa.run(args);
        exportSatisfactionDataByTenantId(args);
    }
	

    private static void exportSatisfactionDataByTenantId(String[] args) {
        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-migration.jar /path/for/data json '2015-11-01 00:00:00' '2015-11-30 00:59:59' 1441 ... ";
        if(args.length < 4){
            throw new RuntimeException(INVALID_PARAM);
        }
        String path = args[0];
        String style = args[1];
        String start = args[2];
        String end = args[3];
        ExportDataService exportService = context.getBean(ExportDataService.class);
        
        if(args.length==4){
            for (int i = 1; i < 30000; i++) {
                log.info("for TenantId is {}", i);
                exportService.exportSatisfactionDataByTenantId(path, style,start, end, i);
            }
        } else {
            for (int i = 4; i < args.length; i++) {
                String tenantIdStr = args[i];
                int tenantId = Integer.parseInt(tenantIdStr);
                
                exportService.exportSatisfactionDataByTenantId(path, style,start, end, tenantId);
            }
        }
    }
}
