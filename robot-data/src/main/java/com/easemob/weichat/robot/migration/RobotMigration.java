/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2015. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package com.easemob.weichat.robot.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

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
	
	private static ConfigurableApplicationContext context;

    public static void main(String[] args){
	    // 启动spring application
		SpringApplication sa = new SpringApplication(RobotMigration.class);
		// disable jetty
		sa.setWebEnvironment(false);
		context = sa.run(args);
		
		// 1. migrate all menu
//		migrateMenu(args);
		
		// 2. delete all rules except menu
//		deleteAllRules();
		
		// 3. export data
		// "Java -jar kefu-robot-migration.jar /user/local/ 20151101 20151130 1441"
//      exportAllTenantsData(args);
		
		// 4. export data by tenantId
//      exportDataByTenantId(args);
		
		// 5. migrate xiaoi to ES
//		migrateToES(args);
		
		// 6. all in one migration for 29
		migrateAllDataInRobotDbByTenantId(args);
		migrateMenuByTenantId(args);
		migrateToES(args);
		
    }
	
	/**
     * @param args
     */
    private static void migrateAllDataInRobotDbByTenantId(String[] args) {
        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-data.jar 1441";
        if(args.length < 1){
            throw new RobotException(INVALID_PARAM);
        }
        
        RobotMigrateAllDataService dataService = context.getBean(RobotMigrateAllDataService.class);
        for (int i = 0; i < args.length; i++) {
            String tenantIdStr = args[i];
            int tenantId = Integer.parseInt(tenantIdStr);
            dataService.migrateAlldata(tenantId);
        }
    }

    private static void migrateToES(String[] args){
	    String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-data.jar 1441";
	    if(args.length < 1){
            throw new RobotException(INVALID_PARAM);
        }
	    MigrateXiaoIToES moveService = context.getBean(MigrateXiaoIToES.class);
        for (int i = 0; i < args.length; i++) {
            String tenantIdStr = args[i];
            int tenantId = Integer.parseInt(tenantIdStr);
            moveService.migrateXiaoIToES(tenantId);
        }
	}

    private static void deleteAllRules() {
        RobotRulesDataSerivce dataService = context.getBean(RobotRulesDataSerivce.class);
		dataService.deleteAllRules(1441);
    }

    private static void migrateMenuByTenantId(String[] args) {
        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-migration.jar 1441";
        RobotMenuMigrationSerivce migrationService = context.getBean(RobotMenuMigrationSerivce.class);

		if(args.length < 1){
			throw new RobotException(INVALID_PARAM);
		}
		for (int i = 0; i < args.length; i++) {
		    String tenantIdStr = args[0];
		    int tenantId = Integer.parseInt(tenantIdStr);
		    migrationService.doMigrationFrom23To29ByTenantId(tenantId);
		}
    }
    
    private static void exportAllTenantsData(String[] args) {
        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-migration.jar /path/for/data '2015-11-01 00:00:00' '2015-11-30 23:59:59' ";
        if(args.length != 3){
            throw new RobotException(INVALID_PARAM);
        }
        String path = args[0];
        String start = args[1];
        String end = args[2];
        
        ExportDataService exportService = context.getBean(ExportDataService.class);
        exportService.exportAllTenantsData(path, start, end);
    }
    
    private static void exportDataByTenantId(String[] args) {
        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-migration.jar /path/for/data '2015-11-01 00:00:00' '2015-11-30 00:59:59' 1441 ... ";
        if(args.length < 4){
            throw new RobotException(INVALID_PARAM);
        }
        String path = args[0];
        String start = args[1];
        String end = args[2];
        ExportDataService exportService = context.getBean(ExportDataService.class);
        for (int i = 3; i < args.length; i++) {
            String tenantIdStr = args[i];
            int tenantId = Integer.parseInt(tenantIdStr);
            
            exportService.exportDataByTenantId(path, start, end, tenantId);
        }
    }
}
