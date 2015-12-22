package com.easemob.weichat.robot.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.easemob.weichat.models.entity.robot.RobotRuleGroup;
import com.easemob.weichat.service.robot.RobotRuleGroupService;

/**
 * 
 * @author shawn
 *
 */
@Service
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
}