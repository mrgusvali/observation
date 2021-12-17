package com.mv.observe.db.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;
import net.vesilik.fakeobservationgen.domain.Message;

@RestController
@RequestMapping(path = "/consumer")
@Log
public class MsgConsumerController {
	
	@Autowired
	private MessageRepository repo;

    @PostMapping(path= "/", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> consumeMessage(@RequestBody Message message) {
        log.info(message.toString());
        
        repo.save(message);
        
        return new ResponseEntity<>("processed", HttpStatus.OK);
    }
    
    @GetMapping(path= "/messages", produces = "application/json")    
    public List<Message> queryForAll() {
    	return repo.findAll();
    }


}
