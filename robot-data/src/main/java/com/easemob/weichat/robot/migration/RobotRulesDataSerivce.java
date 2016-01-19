package com.easemob.weichat.robot.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.easemob.weichat.models.entity.robot.RobotRuleGroup;
import com.easemob.weichat.service.robot.RobotRuleGroupService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author shawn
 *
 */
@Service
@Slf4j
public class RobotRulesDataSerivce{
    
	@Autowired
	private RobotRuleGroupService robotRuleGroupService;
	
	public void deleteAllRules(int tenantId){
	    int pageSize = 1000;
	    int pages = 0;
        // set direction and order field
        PageRequest pageRequest = new PageRequest(pages, pageSize, Direction.DESC, "createdTime");
        Page<RobotRuleGroup> list = robotRuleGroupService.getRobotRuleGroupsByTenantId(tenantId, pageRequest);
        for (RobotRuleGroup robotRuleGroup : list) {
            robotRuleGroupService.delete(robotRuleGroup.getGroupId());
        }
	}
	
	public void createRuleGroup(int tenantId, RobotRuleGroup group, String[] questions, String[] answers) {
	    int id = robotRuleGroupService.insert(group, questions, answers);
	    if(id > 0){
	        log.info("successfully created rule group {}, with question {} and answer {}", group, questions, answers);
	    } else {
	        log.error("failed to create rule group {}, with question {} and answer {}", group, questions, answers);
	    }
	}
}