package com.mv.observe.db.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mv.observe.db.model.FilterCriteria;

import lombok.extern.java.Log;
import net.vesilik.fakeobservationgen.domain.Message;
import net.vesilik.fakeobservationgen.domain.MsgLevel;

@RestController
@RequestMapping(path = "/consumer")
@Log
public class MessagesRESTController {
	
	@Autowired
	private MessageRepository repo;

    @PostMapping(path= "/", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> consumeMessage(@RequestBody Message message) {
        log.info(message.toString());
        
        repo.save(message);
        
        return new ResponseEntity<>("processed", HttpStatus.OK);
    }
    
    @GetMapping(path= "/messages", produces = "application/json")
    public List<Message> query() {
    	List<Message> result = repo.findAll();
    	
		return result;
    }
    
    @PostMapping(path= "/messages", consumes = "application/json", produces = "application/json")    
    public List<Message> query(@RequestBody FilterCriteria criteria) {
    	log.info(criteria.toString());
    	List<Message> result = repo.findBy(criteria);
    	
		return result;
    }


}
