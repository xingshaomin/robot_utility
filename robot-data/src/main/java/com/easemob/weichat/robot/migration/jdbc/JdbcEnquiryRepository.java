package com.easemob.weichat.robot.migration.jdbc;

import com.easemob.weichat.models.entity.Enquiry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 */
@Slf4j
@Repository
public class JdbcEnquiryRepository {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public List<Enquiry> findByTenantIdAndServiceSessionId(int tenantId, String serviceSessionId) {
        BeanPropertyRowMapper<Enquiry> dataMapper = new BeanPropertyRowMapper<>(Enquiry.class);
        String sql = "select * from enquiry where tenantId = ? AND serviceSessionId = ?";
        return jdbcTemplate.query(sql, dataMapper, tenantId, serviceSessionId);
    }
}
