package com.mv.observe.db.controller;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.vesilik.fakeobservationgen.domain.Message;
import net.vesilik.fakeobservationgen.domain.MsgLevel;

import java.rmi.ServerException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = "/consumer")
@Log
public class MsgConsumerController {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

    @PostMapping(path= "/", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> consumeMessage(@RequestBody Message message) {
        log.info(message.toString());
        
        save(message);
        
        return new ResponseEntity<>("processed", HttpStatus.OK);
    }
    
    @GetMapping(path= "/messages", produces = "application/json")    
    public List<Message> queryForAll() {
    	return jdbcTemplate.query("select * from msg", new MessageRowMapper());
    }

	private void save(Message msg) {
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
}
