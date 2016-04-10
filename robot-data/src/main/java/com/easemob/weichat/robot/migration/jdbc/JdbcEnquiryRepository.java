package com.easemob.weichat.robot.migration.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.easemob.weichat.models.entity.Enquiry;

/**
 */
@Repository
public class JdbcEnquiryRepository {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public List<Enquiry> findByTenantIdAndServiceSessionId(int tenantId, String serviceSessionId) {
        BeanPropertyRowMapper<Enquiry> dataMapper = new BeanPropertyRowMapper<Enquiry>(Enquiry.class);
        String sql = "select * from enquiry where tenantId = ? AND serviceSessionId = ?";
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("tenantId", tenantId);
//        map.put("serviceSessionId", serviceSessionId);
        return jdbcTemplate.query(sql, dataMapper, tenantId, serviceSessionId);
    }
}
