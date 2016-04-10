/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2015. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package com.easemob.weichat.robot.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.easemob.weichat.models.entity.robot.RobotMenu;
//import com.easemob.weichat.models.entity.robot.RobotMenuRuleItem;
import com.easemob.weichat.models.entity.robot.RobotRuleGroup;
import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.service.robot.exception.RobotException;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
//		exportDataByTenantId(args);
		
		// 5. migrate xiaoi to ES
//		migrateToES(args);
		
		// 6. all in one migration for 29
//		migrateAllDataInRobotDbByTenantId(args);
//		migrateMenuByTenantId(args);
//		migrateToES(args);
		
		// 7. migrate menu rule item to rules
		// change leaf menu answer type from menu(1) to text(0), level=3
//		migrateMenuRuleItemToRules(args);
		
		// 8. export data by tenantId
        exportSatisfactionDataByTenantId(args);
    }
	
//    private static void migrateMenuRuleItemToRules(String[] args) {
//        RobotMenuRuleItemDataService dataService = context.getBean(RobotMenuRuleItemDataService.class);
//        RobotRulesDataSerivce groupService = context.getBean(RobotRulesDataSerivce.class);
//        RobotMenuDataSerivce menuService = context.getBean(RobotMenuDataSerivce.class);
//        
//        String migrationTenantIdStr = context.getEnvironment().getProperty("kf.robot.migration");
//        log.info("migration tenant list is {}", migrationTenantIdStr);
//        String ignoreMigrationTenantIdStr = context.getEnvironment().getProperty("kf.robot.migration.ignore");
//        log.info("migration ignore tenant list is {}", ignoreMigrationTenantIdStr);
//        String[] tenantIds = migrationTenantIdStr.isEmpty()? null: migrationTenantIdStr.split(",");
//        String[] ignoreTenantIds = ignoreMigrationTenantIdStr.isEmpty() ? null : ignoreMigrationTenantIdStr.split(",");
//        
//        if(tenantIds != null && tenantIds.length > 0) {
//            // by tenant
//            for (int i = 0; i < tenantIds.length; i++) {
//                String tenantIdStr = tenantIds[i];
//                if(tenantIdStr.isEmpty()){
//                    continue;
//                }
//                try {
//                    int tenantId = Integer.parseInt(tenantIdStr);
//                    log.info("start migration for tenant {}", tenantId);
//                    migrateMenuRuleItem(dataService, groupService, menuService, tenantId);
//                } catch (Exception e) {
//                    log.error("tenant {} is not integer", tenantIdStr);
//                    log.error(e.getMessage(), e);
//                }
//            }
//        } else {
//            log.info("start migration all tenants but ignore {}", ignoreMigrationTenantIdStr);
//            List<String> ignoreTenantList = null;
//            if(ignoreTenantIds != null && ignoreTenantIds.length > 0){
//                ignoreTenantList = Arrays.asList(ignoreTenantIds);
//            }
//            // all tenants
//            for (int i = 0; i < 20000; i++) {
//                if(ignoreTenantList != null && ignoreTenantList.contains(String.valueOf(i))){
//                    log.info("ignore migration tenant {}", i);
//                    continue;
//                }
//                migrateMenuRuleItem(dataService, groupService, menuService, i);
//            }
//        }
//    }
//
//    private static void migrateMenuRuleItem(RobotMenuRuleItemDataService dataService,
//            RobotRulesDataSerivce groupService, RobotMenuDataSerivce menuService, int tenantId) {
//        menuService.migrateMenuByTennantId(tenantId);
//        
//        Map<String, List<String>> rules = new HashMap<String, List<String>>();
//        List<RobotMenuRuleItem> list = dataService.getMenuRuleItemByTenantId(tenantId);
//        if(list == null || list.isEmpty()){
//            log.debug("menu rule item is empty for tenant {}", tenantId);
//            return;
//        }
//        for (RobotMenuRuleItem robotMenuRuleItem : list) {
//            String menuId = robotMenuRuleItem.getMenuId();
//            String question = robotMenuRuleItem.getQuestionKey();
//            List<String> questionList = rules.get(menuId);
//            if(questionList == null) {
//                questionList = new ArrayList<String>();
//            }
//            questionList.add(question);
//            rules.put(menuId, questionList);
//        }
//      
//        log.info("find rules {} for tenant {}", rules, tenantId);
//        
//        for (String menuId : rules.keySet()) {
//            List<String> questions = rules.get(menuId);
//            if(questions != null && !questions.isEmpty()) {
//                RobotRuleGroup group = new RobotRuleGroup();
//                group.setTenantId(tenantId);
//                RobotMenu menu = menuService.getRobotMenuById(menuId);
//                if(menu != null){
//                    ObjectNode json = JSONUtil.getObjectMapper().createObjectNode();
//                    json.put("menuId", menuId);
//                    json.put("menuName", menu.getMenuName());
//                    log.info("create rule questions {} and answers {} for tenant {}", questions, json, tenantId);
//                    groupService.createRuleGroup(tenantId, group, questions.toArray(new String[questions.size()]), new String[]{json.toString()});
//                } else {
//                    log.info("menu {} doesn't exist for tenant {}", menuId, tenantId);
//                }
//            }
//        }
//    }

    /**
     * @param args
     */
//    private static void migrateAllDataInRobotDbByTenantId(String[] args) {
//        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-data.jar 1441";
//        if(args.length < 1){
//            throw new RobotException(INVALID_PARAM);
//        }
//        
//        RobotMigrateAllDataService dataService = context.getBean(RobotMigrateAllDataService.class);
//        for (int i = 0; i < args.length; i++) {
//            String tenantIdStr = args[i];
//            int tenantId = Integer.parseInt(tenantIdStr);
//            dataService.migrateAlldata(tenantId);
//        }
//    }
//
//    private static void migrateToES(String[] args){
//	    String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-data.jar 1441";
//	    if(args.length < 1){
//            throw new RobotException(INVALID_PARAM);
//        }
//	    MigrateXiaoIToES moveService = context.getBean(MigrateXiaoIToES.class);
//        for (int i = 0; i < args.length; i++) {
//            String tenantIdStr = args[i];
//            int tenantId = Integer.parseInt(tenantIdStr);
//            moveService.migrateXiaoIToES(tenantId);
//        }
//	}

//    private static void deleteAllRules() {
//        RobotRulesDataSerivce dataService = context.getBean(RobotRulesDataSerivce.class);
//		dataService.deleteAllRules(1441);
//    }

//    private static void migrateMenuByTenantId(String[] args) {
//        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-migration.jar 1441";
//        RobotMenuMigrationSerivce migrationService = context.getBean(RobotMenuMigrationSerivce.class);
//
//		if(args.length < 1){
//			throw new RobotException(INVALID_PARAM);
//		}
//		for (int i = 0; i < args.length; i++) {
//		    String tenantIdStr = args[0];
//		    int tenantId = Integer.parseInt(tenantIdStr);
//		    migrationService.doMigrationFrom23To29ByTenantId(tenantId);
//		}
//    }
    
//    private static void exportAllTenantsData(String[] args) {
//        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-migration.jar /path/for/data '2015-11-01 00:00:00' '2015-11-30 23:59:59' ";
//        if(args.length != 3){
//            throw new RobotException(INVALID_PARAM);
//        }
//        String path = args[0];
//        String start = args[1];
//        String end = args[2];
//        
//        ExportDataService exportService = context.getBean(ExportDataService.class);
//        exportService.exportAllTenantsData(path, start, end);
//    }
    
//    private static void exportDataByTenantId(String[] args) {
//        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-migration.jar /path/for/data '2015-11-01 00:00:00' '2015-11-30 00:59:59' 1441 ... ";
//        if(args.length < 4){
//            throw new RobotException(INVALID_PARAM);
//        }
//        String path = args[0];
//        String start = args[1];
//        String end = args[2];
//        ExportDataService exportService = context.getBean(ExportDataService.class);
//        for (int i = 3; i < args.length; i++) {
//            String tenantIdStr = args[i];
//            int tenantId = Integer.parseInt(tenantIdStr);
//            
//            exportService.exportDataByTenantId(path, start, end, tenantId);
//        }
//    }
    
    private static void exportSatisfactionDataByTenantId(String[] args) {
        String INVALID_PARAM = "Good Examples: " + "Java -jar kefu-robot-migration.jar /path/for/data '2015-11-01 00:00:00' '2015-11-30 00:59:59' 1441 ... ";
        if(args.length < 3){
            throw new RobotException(INVALID_PARAM);
        }
        String path = args[0];
        String start = args[1];
        String end = args[2];
        ExportDataService exportService = context.getBean(ExportDataService.class);
        
        if(args.length==3){
            for (int i = 1; i < 20000; i++) {
                exportService.exportSatisfactionDataByTenantId(path, start, end, i);
            }
        } else {
            for (int i = 3; i < args.length; i++) {
                String tenantIdStr = args[i];
                int tenantId = Integer.parseInt(tenantIdStr);
                
                exportService.exportSatisfactionDataByTenantId(path, start, end, tenantId);
            }
        }
    }
}
