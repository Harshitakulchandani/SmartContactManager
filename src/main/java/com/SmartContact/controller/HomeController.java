package com.SmartContact.controller;

import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.SmartContact.entities.UserEntities;
import com.SmartContact.helper.Mymessage;
import com.SmartContact.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {
   
	
	

	@Autowired 
	   private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordencoder;
	
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home-Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About-Smart Contact Manager");
		return "about";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("title", "Register");
		model.addAttribute("user", new UserEntities());
		return "register";
	}
	
	//handler for register user
	@PostMapping("/do_register")
	public String doRegister(@Valid @ModelAttribute("user") UserEntities user , BindingResult result ,@RequestParam(value = "agree" , defaultValue = "false") boolean b , Model model, HttpServletRequest request  ) {
		HttpSession session=request.getSession();
		try {
		
		if(b!=true) {
			System.out.println("you have not agreed on terms and conditions");
			throw new Exception("you have not agreed on terms and conditions");
		}
		if(result.hasErrors()) {
			model.addAttribute("user" , user);
			return "register";
		}
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setPassword(passwordencoder.encode(user.getPassword()));
		
		this.userRepository.save(user);
		
		model.addAttribute("user", new UserEntities());
		session.setAttribute("message", new Mymessage("successfully registered!!","alert-success"));
		 
		
		System.out.println("agreement"+ b);
		System.out.println(user);
		
		return "register";
	
	}catch(Exception e) {
		
		     e.printStackTrace();
		     model.addAttribute("user", user);
		     session.setAttribute("message", new Mymessage("something went wrong " + e.getMessage() , "alert-danger"));
		     return "register";
		
	}
	
	}
	   //custom login
	@GetMapping("/signin")
	public String login(Model model) {
		
		return "login";
	}
	
	    }
