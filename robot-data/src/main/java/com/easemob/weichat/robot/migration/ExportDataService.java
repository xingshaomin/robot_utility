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
import org.springframework.stereotype.Service;

import com.easemob.weichat.models.entity.ChatMessage;
import com.easemob.weichat.models.entity.ServiceSession;
import com.easemob.weichat.persistence.jdbc.JdbcChatMessageRepositoryProvider;
import com.easemob.weichat.persistence.jdbc.JdbcServiceSessionRepositoryProvider;
import com.easemob.weichat.robot.migration.jdbc.JdbcServiceSessionRepositoryExtProvider;


/**
 * @author shawn
 *
 */
@Service
public class ExportDataService {
    @Autowired
    private JdbcChatMessageRepositoryProvider chatMessageRepositoryProvider;
    
    @Autowired
    private JdbcServiceSessionRepositoryExtProvider serviceSessionRepositoryProvider;
    
    public List<ChatMessage> getChatMessageByServiceSessionId(int tenantId, String sessionServiceId){
        return chatMessageRepositoryProvider.getAllMessageRecordByServiceSessionId(tenantId, sessionServiceId);
    }
    
    public List<ServiceSession> getServiceSessions(int page, int pageSize, String startDate, String endDate){
        return serviceSessionRepositoryProvider.getServiceSessionList(page, pageSize, startDate, endDate);
    }
    
    public List<ServiceSession> getServiceSessionsByTenantId(int tenantId, int page, int pageSize, String startDate, String endDate){
        return serviceSessionRepositoryProvider.getServiceSessionListByTenantId(tenantId, page, pageSize, startDate, endDate);
    }
    
    public int getTotalCount(String startDate, String endDate){
        return serviceSessionRepositoryProvider.getTotalCount(startDate, endDate);
    }
    
    public int getTotalCountByTenantId(int tenantId, String startDate, String endDate){
        return serviceSessionRepositoryProvider.getTotalCountByTenantId(tenantId, startDate, endDate);
    }
}
