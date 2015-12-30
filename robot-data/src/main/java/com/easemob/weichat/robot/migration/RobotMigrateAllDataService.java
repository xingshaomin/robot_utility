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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.easemob.weichat.models.entity.robot.Robot;
import com.easemob.weichat.models.entity.robot.RobotMenu;
import com.easemob.weichat.models.entity.robot.RobotMenuRuleItem;
import com.easemob.weichat.models.entity.robot.RobotPersonalInfo;
import com.easemob.weichat.models.entity.robot.RobotPredefinedReply;
import com.easemob.weichat.models.entity.robot.RobotRuleGroup;
import com.easemob.weichat.models.entity.robot.RobotRuleItem;
import com.easemob.weichat.persistence.jpa.robot.RobotMenuRepository;
import com.easemob.weichat.persistence.jpa.robot.RobotMenuRuleItemRepository;
import com.easemob.weichat.persistence.jpa.robot.RobotPersonalInfoRepository;
import com.easemob.weichat.persistence.jpa.robot.RobotPredefinedReplyRepository;
import com.easemob.weichat.persistence.jpa.robot.RobotRepository;
import com.easemob.weichat.persistence.jpa.robot.RobotRuleGroupRepository;
import com.easemob.weichat.persistence.jpa.robot.RobotRuleItemRepository;
import com.easemob.weichat.persistence.jpa.robot_old.RobotMenuRepositoryOld;
import com.easemob.weichat.persistence.jpa.robot_old.RobotMenuRuleItemRepositoryOld;
import com.easemob.weichat.persistence.jpa.robot_old.RobotPersonalInfoRepositoryOld;
import com.easemob.weichat.persistence.jpa.robot_old.RobotPredefinedReplyRepositoryOld;
import com.easemob.weichat.persistence.jpa.robot_old.RobotRepositoryOld;
import com.easemob.weichat.persistence.jpa.robot_old.RobotRuleGroupRepositoryOld;
import com.easemob.weichat.persistence.jpa.robot_old.RobotRuleItemRepositoryOld;

import lombok.extern.slf4j.Slf4j;
/**
 * @author shawn
 *
 */
@Slf4j
@Service
public class RobotMigrateAllDataService {
    @Autowired
    private RobotMenuRepository menuRepo;
    @Autowired
    private RobotRuleGroupRepository ruleGroupRepo;
    @Autowired
    private RobotRuleItemRepository ruleItemRepo;
    @Autowired
    private RobotMenuRuleItemRepository menuRuleItemRepo;
    @Autowired
    private RobotPersonalInfoRepository personalInfoRepo;
    @Autowired
    private RobotPredefinedReplyRepository predefinedReplyRepo;
    @Autowired
    private RobotRepository robotRepo;
    
    
    @Autowired
    private RobotMenuRepositoryOld menuRepoOld;
    @Autowired
    private RobotMenuRuleItemRepositoryOld menuRuleItemRepoOld;
    @Autowired
    private RobotRuleGroupRepositoryOld ruleGroupRepoOld;
    @Autowired
    private RobotRuleItemRepositoryOld ruleItemRepoOld;
    @Autowired
    private RobotPersonalInfoRepositoryOld personalInfoRepoOld;
    @Autowired
    private RobotPredefinedReplyRepositoryOld predefinedReplyRepoOld;
    @Autowired
    private RobotRepositoryOld robotRepoOld;

    PageRequest pageRequest = new PageRequest(0, 1000, Direction.DESC, "createdTime");
    /**
     * @param tenantId
     */
    public void migrateAlldata(int tenantId) {
        migrateMenu(tenantId);
        migrateMenuRuleItem(tenantId);
        migrateRuleGroup(tenantId);
        migrateRuleItem(tenantId);
        migratePersonalInfo(tenantId);
        migratePredefinedReply(tenantId);
        migrateRobot(tenantId);
    }
    /**
     * @param tenantId
     */
    private void migrateRobot(int tenantId) {
        int result = robotRepo.deleteByTenantId(tenantId);
        log.info("deleted {} predefined reply in tenant {}", result, tenantId);
        List<Robot> list = robotRepoOld.findByTenantId(tenantId);
        for (Robot item : list) {
            robotRepo.saveAndFlush(item);
            log.info("saved {} in new robot_predefined_reply for tenant {} ", item, tenantId);
        }
    }
    /**
     * @param tenantId
     */
    private void migratePredefinedReply(int tenantId) {
        int result = predefinedReplyRepo.deleteByTenantId(tenantId);
        log.info("deleted {} predefined reply in tenant {}", result, tenantId);
        List<RobotPredefinedReply> list = predefinedReplyRepoOld.findByTenantId(tenantId);
        for (RobotPredefinedReply item : list) {
            predefinedReplyRepo.saveAndFlush(item);
            log.info("saved {} in new robot_predefined_reply for tenant {} ", item, tenantId);
        }
    }
    /**
     * @param tenantId
     */
    private void migratePersonalInfo(int tenantId) {
        int result = personalInfoRepo.deleteByTenantId(tenantId);
        log.info("deleted {} personal info in tenant {}", result, tenantId);
        List<RobotPersonalInfo> list = personalInfoRepoOld.findByTenantId(tenantId);
        for (RobotPersonalInfo item : list) {
            personalInfoRepo.saveAndFlush(item);
            log.info("saved {} in new robot_personal_info for tenant {} ", item, tenantId);
        }
    }
    /**
     * @param tenantId
     */
    private void migrateRuleItem(int tenantId) {
        int result = ruleItemRepo.deleteByTenantId(tenantId);
        log.info("deleted {} rule item in tenant {}", result, tenantId);
        List<RobotRuleItem> list = ruleItemRepoOld.findByTenantId(tenantId);
        for (RobotRuleItem robotMenuRuleItem : list) {
            ruleItemRepo.saveAndFlush(robotMenuRuleItem);
            log.info("saved {} in new robot_rule_item for tenant {} ", robotMenuRuleItem, tenantId);
        }
    }
    /**
     * @param tenantId
     */
    private void migrateRuleGroup(int tenantId) {
        int result = ruleGroupRepo.deleteByTenantId(tenantId);
        log.info("deleted {} rule group in tenant {}", result, tenantId);
        List<RobotRuleGroup> list = ruleGroupRepoOld.findByTenantId(tenantId);
        for (RobotRuleGroup item : list) {
            ruleGroupRepo.saveAndFlush(item);
            log.info("saved {} in new robot_rule_group for tenant {} ", item, tenantId);
        }
        
    }
    /**
     * @param tenantId
     */
    private void migrateMenuRuleItem(int tenantId) {
        int result = menuRuleItemRepo.deleteByTenantId(tenantId);
        log.info("deleted {} menu rule item in tenant {}", result, tenantId);
        List<RobotMenuRuleItem> list = menuRuleItemRepoOld.findByTenantId(tenantId);
        for (RobotMenuRuleItem robotMenuRuleItem : list) {
            menuRuleItemRepo.saveAndFlush(robotMenuRuleItem);
            log.info("saved {} in new robot_menu_rule_item for tenant {} ", robotMenuRuleItem, tenantId);
        }
    }
    
    private void migrateMenu(int tenantId) {
        int result = menuRepo.deleteByTenantId(tenantId);
        log.info("deleted {} menus in tenant {}", result, tenantId);
        List<RobotMenu> menus = menuRepoOld.findByTenantId(tenantId);
        for (RobotMenu robotMenu : menus) {
            menuRepo.saveAndFlush(robotMenu);
            log.info("saved {} in new robot_menu for tenant {} ", robotMenu, tenantId);
        }
    }
}
