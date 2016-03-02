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
import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.service.robot.RobotMenuService;
import com.easemob.weichat.service.robot.RobotRuleGroupService;
import com.easemob.weichat.service.robot.RobotUserService;
import com.easemob.weichat.service.robot.ai.impl.EasemobRobot;
import com.easemob.weichat.service.robot.ai.impl.XiaoIRobot;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shawn
 *
 */
@Slf4j
@Service
public class MigrateXiaoIToESService {
    @Autowired
    private EasemobRobot easemobRobot;
    
    @Autowired
    private XiaoIRobot xiaoiRobot;
    
    @Autowired
    private RobotUserService robotUserService;
    
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
        log.info("get {} root menus in tenant {}", menus.getTotalElements(), tenantId);
        for (RobotMenu robotMenu : menus) {
            log.info("prepare to migrate {} in tenant {} in es", robotMenu, robotMenu.getTenantId());
            moveKBToES(tenantId, robotId, robotMenu.getMenuId(), robotMenu);
            handleChildren(tenantId, robotId, robotMenu);
        }
    }

    private void handleChildren(int tenantId, final String robotId, RobotMenu robotMenu) {
        List<RobotMenu> children = robotMenuService.getChildRobotMenu(robotMenu.getMenuId());
        if(children != null && children.size() > 0){
            log.info("get {} child menus under root menu {} in tenant {}", children.size(), robotMenu, tenantId);
            for (RobotMenu child : children) {
                log.info("prepare to migrate {} in tenant {} in es", child.getMenuName(), child.getTenantId());
                moveKBToES(tenantId, robotId, child.getMenuId(), child);
                handleChildren(tenantId, robotId, child);
            }
        }
    }
    
    private void moveKBToES(int tenantId, final String robotId, String kbId, Object robotRule) {
        try {
            JsonNode json = xiaoiRobot.getCustomKB(tenantId, robotId, kbId);
            if(json != null){
                if(json.get("code").asInt() == 0){
                    JsonNode data = json.get("data");
                    if(data == null){
                        log.error("the kb {} is null from xiaoi", kbId);
                        return;
                    }
                    ArrayNode questions = (ArrayNode) data.get("questions");
                    if(questions == null){
                        log.error("the questions in kb {} is null from xiaoi", kbId);
                        return;
                    }
                    ArrayNode questionKeyArray = JSONUtil.getObjectMapper().createArrayNode();
                    for (JsonNode jsonNode : questions) {
                        JsonNode question = jsonNode.get("question");
                        questionKeyArray.add(question.asText());
                    }
                    ArrayNode answerArray = (ArrayNode) data.get("answers");
                    ObjectNode newJson = JSONUtil.getObjectMapper().createObjectNode();
                    newJson.replace("questions", questionKeyArray);
                    newJson.replace("answers", answerArray);
                    if(!easemobRobot.isCustomKBExist(tenantId, kbId)){
                        log.info("start to create {} in es", newJson);
                        JsonNode result = easemobRobot.createCustomKB(tenantId, newJson, robotId, kbId);
                        log.info("finish creating {} in es", newJson);
                        if(result != null){
                            log.info("successfully migrate {} into es", robotRule);
                        } else {
                            log.error("failed to migrate {} into es", robotRule);
                        }
                    } else {
                        log.info("tenant {} rule {} already exists", tenantId, kbId);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("{} does not exist in xiaoi", robotRule);
        }
    }

}
