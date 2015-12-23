/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2015. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package com.easemob.weichat.robot.migration.jdbc;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.easemob.weichat.models.entity.ServiceSession;
import com.easemob.weichat.persistence.jdbc.JdbcServiceSessionRepositoryProvider;
import com.easemob.weichat.persistence.jdbc.ServiceSessionRowMappers;

/**
 * @author shawn
 *
 */
@Repository
public class JdbcServiceSessionRepositoryExtProvider extends JdbcServiceSessionRepositoryProvider {

    public List<ServiceSession> getServiceSessionList(int page, int pageSize, String startDate, String endDate) {
        String sql = "select * from servicesession where createDateTime>=? and createDateTime<=? limit " + page + "," + pageSize +";";
        try {
            return jdbcTemplate.query(sql, ServiceSessionRowMappers.simpleRowMapper, startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    public List<ServiceSession> getServiceSessionListByTenantId(int tenantId, int page, int pageSize, String startDate, String endDate) {
        String sql = "select * from servicesession where tenantId=? and createDateTime>=? and createDateTime<=? limit " + page + "," + pageSize +";";
        try {
            return jdbcTemplate.query(sql, ServiceSessionRowMappers.simpleRowMapper, tenantId, startDate, endDate);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    public int getTotalCount(String startDate, String endDate) {
        String sql = "SELECT count(*) FROM  `servicesession` WHERE createDateTime>=? and createDateTime<=?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, startDate, endDate);
    }
    
    public int getTotalCountByTenantId(int tenantId, String startDate, String endDate) {
        String sql = "SELECT count(*) FROM  `servicesession` WHERE tenantId=? and createDateTime>=? and createDateTime<=?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, tenantId, startDate, endDate);
    }
}
