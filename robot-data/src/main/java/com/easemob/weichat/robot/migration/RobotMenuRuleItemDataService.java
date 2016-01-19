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

import com.easemob.weichat.models.entity.robot.RobotMenuRuleItem;
import com.easemob.weichat.persistence.jpa.robot.RobotMenuRuleItemRepository;

/**
 * @author shawn
 *
 */
@Service
public class RobotMenuRuleItemDataService {
    @Autowired
    private RobotMenuRuleItemRepository menuRuleItemRepo;
    
    public List<RobotMenuRuleItem> getMenuRuleItemByTenantId(int tenantId) {
        return menuRuleItemRepo.findByTenantId(tenantId);
    }
}
