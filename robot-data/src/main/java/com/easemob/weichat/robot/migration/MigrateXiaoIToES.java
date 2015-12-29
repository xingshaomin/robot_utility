/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2015. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package com.easemob.weichat.robot.migration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.easemob.weichat.models.entity.robot.Robot;
import com.easemob.weichat.models.entity.robot.RobotMenu;
import com.easemob.weichat.models.entity.robot.RobotRuleGroup;
import com.easemob.weichat.service.robot.IRobotUserService;
import com.easemob.weichat.service.robot.RobotMenuService;
import com.easemob.weichat.service.robot.RobotRuleGroupService;
import com.easemob.weichat.service.robot.ai.impl.EasemobRobot;
import com.easemob.weichat.service.robot.ai.impl.XiaoIRobot;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shawn
 *
 */
@Slf4j
@Service
public class MigrateXiaoIToES {
    @Autowired
    private EasemobRobot easemobRobot;
    
    @Autowired
    private XiaoIRobot xiaoiRobot;
    
    @Autowired
    private IRobotUserService robotUserService;
    
    @Autowired
    private RobotMenuService robotMenuService;
    
    @Autowired
    private RobotRuleGroupService robotRuleGroupService;
    
    public void migrateXiaoIToES(int tenantId){
        Robot robot = robotUserService.getRobotUserByTenantId(tenantId);
        if(robot == null){
            log.error("robot is null in tenant {}", tenantId);
            return;
        }
        final String robotId = robot.getRobotId();
        // migrate rules
        int pageSize = 1000;
        int pages = 0;
        // set direction and order field
        PageRequest pageRequest = new PageRequest(pages, pageSize, Direction.DESC, "createdTime");
        Page<RobotRuleGroup> list = robotRuleGroupService.getRobotRuleGroupsByTenantId(tenantId, pageRequest);
        for (RobotRuleGroup robotRuleGroup : list) {
            moveKBToES(tenantId, robotId, robotRuleGroup.getGroupId(), robotRuleGroup);
        }
        
        // migrate menu
        Page<RobotMenu> menus = robotMenuService.getRootRobotMenu(tenantId, pageRequest);
        for (RobotMenu robotMenu : menus) {
            moveKBToES(tenantId, robotId, robotMenu.getMenuId(), robotMenu);
            List<RobotMenu> children = robotMenuService.getChildRobotMenuRecursively(robotMenu.getMenuId());
            for (RobotMenu child : children) {
                moveKBToES(tenantId, robotId, child.getMenuId(), child);
            }
        }
    }
    
    private void moveKBToES(int tenantId, final String robotId, String kbId, Object robotRule) {
        try {
            JsonNode json = xiaoiRobot.getCustomKB(tenantId, robotId, kbId);
            if(json != null){
                if(json.get("code").asInt() == 0){
                    JsonNode data = json.get("data");
                    log.info("start to create {} in es", data);
                    JsonNode result = easemobRobot.createCustomKB(tenantId, data, robotId, false);
                    log.info("finish creating {} in es", data);
                    if(result != null){
                        log.info("successfully migrate {} into es", robotRule);
                    } else {
                        log.error("failed to migrate {} into es", robotRule);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("{} does not exist in xiaoi", robotRule);
        }
    }

}
