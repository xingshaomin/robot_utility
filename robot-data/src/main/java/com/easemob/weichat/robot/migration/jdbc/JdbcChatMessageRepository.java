package com.easemob.weichat.robot.migration.jdbc;

import com.easemob.weichat.models.entity.ChatMessage;
import com.easemob.weichat.models.entity.User;
import com.easemob.weichat.models.enums.UserType;
import com.easemob.weichat.models.message.EasemobMessage;
import com.easemob.weichat.models.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dongwentao on 16/9/27.
 */
@Slf4j
@Repository
public class JdbcChatMessageRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcChatMessageRepository.class);

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public static final RowMapper<ChatMessage> rowMapper = (resultSet,i) -> {
            ChatMessage item = new ChatMessage();
            JdbcChatMessageRepository.initChatMessage(resultSet, item);
            return item;
    };

    private static void initChatMessage(ResultSet resultSet, ChatMessage item) throws SQLException {
        item.setMsgId(resultSet.getString("msgId"));
        item.setTenantId(resultSet.getInt("tenantId"));
        String bodyString = resultSet.getString("body");
        if(bodyString != null) {
            try {
                item.setBody(JSONUtil.getObjectMapper().readValue(bodyString, EasemobMessage.class));
            } catch (IOException var5) {
                logger.error("Failed to deserialize json {}", bodyString);
            }
        }

        item.setChatGroupSeqId(resultSet.getLong("chatGroupSeqId"));
        item.setSessionServiceSeqId(resultSet.getLong("sessionServiceSeqId"));
        item.setCreateDateTime(resultSet.getTimestamp("createDateTime"));
        item.setTimestamp(resultSet.getTimestamp("timestamp").getTime());
        item.setChatGroupId(resultSet.getLong("chatGroupId"));
        item.setSessionServiceId(resultSet.getString("sessionServiceId"));
        User fromUser = new User();
        fromUser.setTenantId(item.getTenantId());
        fromUser.setUserId(resultSet.getString("fromUserId"));
        fromUser.setUserType(resultSet.getString("fromuser_userType") != null? UserType.valueOf(resultSet.getString("fromuser_userType")):null);
        fromUser.setNicename(resultSet.getString("fromuser_nicename"));
        item.setFromUser(fromUser);
        User toUser = new User();
        toUser.setTenantId(item.getTenantId());
        toUser.setUserId(resultSet.getString("toUserId"));
        toUser.setUserType(resultSet.getString("touser_userType") != null?UserType.valueOf(resultSet.getString("touser_userType")):null);
        toUser.setNicename(resultSet.getString("touser_nicename"));
        item.setToUser(toUser);
    }

    public List<ChatMessage> getAllMessageRecordByServiceSessionId(int tenantId, String sessionServiceId) {
        String sql = "SELECT * FROM chatmessage WHERE tenantId = ? and sessionServiceId = ? ORDER by createDateTime;";
        return this.jdbcTemplate.query(sql, rowMapper, new Object[]{Integer.valueOf(tenantId), sessionServiceId});
    }
}
