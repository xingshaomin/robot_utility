package com.easemob.weichat.robot.migration;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.easemob.weichat.models.entity.robot.RobotMenu;
import com.easemob.weichat.persistence.jpa.robot.RobotMenuRepository;
import com.easemob.weichat.service.robot.RobotMenuService;

/**
 * 29版本robot_menu表添加了level和rootMenuId两个字段，默认为null，这个service可以把这两个新字段具体的值填上
 * level的值来源于递归遍历时候，层级计数，rootMenuId就是parentMenuId为0的menu的menuId。
 * 数据修复只是填补新增字段的值，不会影响其他已有的值
 * 
 * @author shawn
 *
 */
@Service
public class RobotMenuMigrationSerivce{
    private static final Logger logger = LoggerFactory.getLogger(RobotMenuMigrationSerivce.class);
    
	@Autowired
	private RobotMenuService robotMenuService;
	@Autowired
    private RobotMenuRepository provider;
	
	/**
	 * 计算29版本中robot_menu的新字段level和rootMenuId的值并更新
	 */
	public void doMigrationFrom23To29() {
	    logger.info("start migration from 23 to 29");
		List<Integer> tenants = robotMenuService.getTanentHasMenu();
		logger.info("find {} tenants", tenants.size());
		for (Integer tenantId : tenants) {
		    logger.info(" ");
		    logger.info("================start ==========");
		    logger.info("fix tenant {}", tenantId);
			int totalCount = robotMenuService.getRootMenuTotalCount(tenantId);
			logger.info("tenant {} total menu count is {}", tenantId, totalCount);
			Page<RobotMenu> menus = robotMenuService.getRootRobotMenu(tenantId, new PageRequest(0, totalCount));
			for (RobotMenu robotMenu : menus) {
				robotMenu.setRootMenuId(robotMenu.getMenuId());
				robotMenu.setLevel(0);
				int affected = provider.update(robotMenu.getMenuName(), robotMenu.getParentMenuId(), robotMenu.getLevel(), robotMenu.getRootMenuId(), robotMenu.getMenuDesc(), robotMenu.getMenuId());
				if(affected > 0){
				    logger.info("successfully updated menu {} for level and rootMenuId", robotMenu);
				} else {
				    logger.info("no change for menu {}", robotMenu);
				}
				doInChildren(tenantId, robotMenu.getMenuId(), robotMenu.getMenuId(), 1);
			}
			logger.info("================end==========");
		}
	}
	
	public void doMigrationFrom23To29ByTenantId(int tenantId) {
	    logger.info("start migration from 23 to 29 for tenant {}", tenantId);
	    logger.info(" ");
	    logger.info("================start ==========");
	    logger.info("fix tenant {}", tenantId);
	    int totalCount = robotMenuService.getRootMenuTotalCount(tenantId);
	    logger.info("tenant {} total menu count is {}", tenantId, totalCount);
	    Page<RobotMenu> menus = provider.findRootMenuByParentId(tenantId, new PageRequest(0, 1000));
	    for (RobotMenu robotMenu : menus) {
	        robotMenu.setRootMenuId(robotMenu.getMenuId());
	        robotMenu.setLevel(0);
	        int affected = provider.update(robotMenu.getMenuName(), robotMenu.getParentMenuId(), robotMenu.getLevel(), robotMenu.getRootMenuId(), robotMenu.getMenuDesc(), robotMenu.getMenuId());
	        if(affected > 0){
	            logger.info("successfully updated menu {} for level and rootMenuId", robotMenu);
	        } else {
	            logger.info("no change for menu {}", robotMenu);
	        }
	        doInChildren(tenantId, robotMenu.getMenuId(), robotMenu.getMenuId(), 1);
	    }
	    logger.info("================end==========");
	}

	/**
     * 计算29版本robot_menu中子菜单的新字段level和rootMenuId的值并更新
     */
	private void doInChildren(int tenantId, String parentMenuId, String rootMenuId, int level) {
		List<RobotMenu> children = robotMenuService.getChildRobotMenu(parentMenuId);
		for (RobotMenu robotMenu : children) {
			robotMenu.setRootMenuId(rootMenuId);
			robotMenu.setLevel(level);
			int affected = provider.update(robotMenu.getMenuName(), robotMenu.getParentMenuId(), robotMenu.getLevel(), robotMenu.getRootMenuId(), robotMenu.getMenuDesc(), robotMenu.getMenuId());
			if(affected > 0){
                logger.info("successfully updated menu {} for level and rootMenuId", robotMenu);
            } else {
                logger.info("no change for menu {}", robotMenu);
            }
			doInChildren(tenantId, robotMenu.getMenuId(), rootMenuId, level+1);
		}
	}
}