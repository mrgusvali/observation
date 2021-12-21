package com.mv.observe.db.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.mv.observe.db.model.FilterCriteria;

import net.vesilik.fakeobservationgen.domain.Message;
import net.vesilik.fakeobservationgen.domain.MsgLevel;

/** Operations with messages business objects using JDBC template */
@Service
public class MessageRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
    
	public void save(Message msg) {
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("id", msg.getId())
				.addValue("timestamp", msg.getTimestamp())
				.addValue("level", msg.getLevel().name())
				.addValue("senderCode", msg.getSenderCode())
				.addValue("coordLat", msg.getCoordLat())
				.addValue("coordLon", msg.getCoordLon())
				.addValue("message", msg.getMessage());
		
		jdbcTemplate.update(
				"INSERT INTO msg (id,time,level,senderCode,coordLat,coordLon,message) VALUES "
				+ "  (:id,:timestamp,:level,:senderCode,:coordLat,:coordLon,:message)",
				namedParameters); 
	}
	
	static class MessageRowMapper implements RowMapper<Message> {

		@Override
		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			Message m = new Message();
			m.setId(rs.getString("id"));
			m.setTimestamp(rs.getTimestamp("time"));
			m.setLevel(MsgLevel.valueOf(rs.getString("level")));
			m.setSenderCode(rs.getString("senderCode"));
			m.setCoordLat(rs.getFloat("coordLat"));
			m.setCoordLon(rs.getFloat("coordLon"));
			m.setMessage(rs.getString("message"));
			return m;
		}
		
	}

	public List<Message> findAll() {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("level", MsgLevel.SECRET.name());
		return jdbcTemplate.query("select * from msg WHERE level <> :level", params, new MessageRowMapper());
	}

	public List<Message> findBy(FilterCriteria c) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		StringBuilder sql = new StringBuilder("select * from msg WHERE level <> :level");
		params.put("level", MsgLevel.SECRET.name());
		if (c.getLevel()!=null) {
			sql.append(" AND level=:level2");
			params.put("level2", c.getLevel().name());
		}
		sql.append(" AND time>:timestamp");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -c.getAgeMinutes());
		params.put("timestamp", new Timestamp(cal.getTimeInMillis()));
		
		return jdbcTemplate.query(sql.toString(), params, new MessageRowMapper());
	}
}
