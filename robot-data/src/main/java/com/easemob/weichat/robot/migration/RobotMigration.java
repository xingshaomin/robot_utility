/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2015. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package com.easemob.weichat.robot.migration;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.easemob.weichat.service.robot.exception.RobotException;


/**
 * 用于做版本升级时机器人数据migration
 * 
 * @author shawn
 * 
 */
@SpringBootApplication
@ComponentScan("com.easemob.weichat")
public class RobotMigration {
	private static final String COMMAND_LINE = "Java -jar kefu-robot-migration.jar -v 29";
	private static final String INVALID_PARAM = "invalid parameter, the command should be like " + COMMAND_LINE;
	
	public static void main(String[] args){
	    // 启动spring application
		SpringApplication sa = new SpringApplication(RobotMigration.class);
		// disable jetty
		sa.setWebEnvironment(false);
		// get application context
		ConfigurableApplicationContext context = sa.run(args);
		RobotMenuMigrationSerivce migrationService = context.getBean(RobotMenuMigrationSerivce.class);

		if(args.length != 2){
			throw new RobotException(INVALID_PARAM);
		}
		String option = args[0];
		String currentVersion = args[1];
		// only do migration when the version is 29
		if("-v".equals(option) && "29".equals(currentVersion)){
			migrationService.doMigrationFrom23To29();
		} else {
			throw new RobotException(INVALID_PARAM);
		}
		
		// exit
		SpringApplication.exit(context, new ExitCodeGenerator(){
			@Override
			public int getExitCode() {
				return 0;
			}
		});
    }
}
