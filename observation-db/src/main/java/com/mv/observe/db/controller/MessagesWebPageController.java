package com.mv.observe.db.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mv.observe.db.model.FilterCriteria;

import lombok.extern.java.Log;
import net.vesilik.fakeobservationgen.domain.Message;

/** Passages to web pages via Thymeleaf templates */
@Controller
@Log
public class MessagesWebPageController {
	
	@Autowired
	private MessageRepository repo;
    
    @GetMapping(path= "/")    
    public String prepare(Model model) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat();
		model.addAttribute("serverTime", dateFormat.format(new Date()));
		
    	packMessages(model);
		
		return "index"; // into src/main/resources/templates/index.html
    }
    
    @PostMapping(path="/")
    public String query(@ModelAttribute FilterCriteria filterCriteria, BindingResult errors, Model model)
    {
    	// TODO: validate criteria posted from html form
    	packMessages(model);
		return "messages";
    }

	private void packMessages(Model model) {
		List<Message> messagesList = repo.findAll();
		log.info(messagesList.size() + " messages in repo.");
		model.addAttribute("messages", messagesList);
	}

}
