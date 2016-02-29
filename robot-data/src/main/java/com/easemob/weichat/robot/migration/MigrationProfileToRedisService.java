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

import com.easemob.weichat.models.entity.robot.RobotPersonalInfo;
import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.persistence.jpa.robot.RobotPersonalInfoRepository;
import com.easemob.weichat.service.robot.ai.impl.EasemobRobot;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shawn
 *
 */
@Slf4j
@Service
public class MigrationProfileToRedisService {

    @Autowired
    private EasemobRobot easemobRobot;
    
    @Autowired
    private RobotPersonalInfoRepository provider;
    /**
     * @param tenantId
     */
    public void migrationProfileToChatServer(int tenantId) {
        List<RobotPersonalInfo> list = provider.findByTenantId(tenantId);
        if(list != null && !list.isEmpty()){
            for (RobotPersonalInfo robotPersonalInfo : list) {
                JsonNode info = JSONUtil.getObjectMapper().convertValue(robotPersonalInfo, JsonNode.class);
                if(info!= null){
                    log.info("converted personalinfo into json {} for tenant {}", info, tenantId);
                    JsonNode savedInfo = easemobRobot.createOrUpdatePersonalInfo(tenantId, info, robotPersonalInfo.getRobotId());
                    log.info("saved personalinfo json {} for tenant {} to chatserver", savedInfo, tenantId);
                } else {
                    log.info("failed to convert personalinfo into json for tenant {}", tenantId);
                }
            }
        } else {
            log.info("no profile for tenant {}", tenantId);
        }
    }

}
