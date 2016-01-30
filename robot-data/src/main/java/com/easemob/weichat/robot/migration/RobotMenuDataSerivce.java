/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2016. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package com.easemob.weichat.robot.migration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easemob.weichat.models.entity.robot.RobotMenu;
import com.easemob.weichat.models.enums.RobotAnswerForm;
import com.easemob.weichat.persistence.jpa.robot.RobotMenuRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shawn
 *
 */
@Service
@Slf4j
public class RobotMenuDataSerivce {
    @Autowired
    private RobotMenuRepository provider;
    
    public void migrateMenuByTennantId(int tenantId) {
        List<RobotMenu> leafs = provider.findByTenantIdAndLevel(tenantId, 3);
        if(leafs != null) {
            for (RobotMenu robotMenu : leafs) {
                robotMenu.setMenuAnswerType(RobotAnswerForm.TEXT.getValue());
                provider.saveAndFlush(robotMenu);
                log.info("change type from menu to text for leaf menu {} for tenant {} ", robotMenu, tenantId);
            }
        }
    }
    
    public RobotMenu getRobotMenuById(String menuId){
       return provider.findByMenuId(menuId);
    }

}
