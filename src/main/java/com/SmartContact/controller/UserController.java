package com.SmartContact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.SmartContact.entities.ContactEntities;
import com.SmartContact.entities.UserEntities;
import com.SmartContact.helper.Mymessage;
import com.SmartContact.repository.ContactRepository;
import com.SmartContact.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PreAuthorize("hasRole('USER')")
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ContactRepository contactRepository;

	// commaon method for all handlers
	@ModelAttribute
	public void commonData(Model model, Principal principal) {
		// principal is used for unique identifier --> email/id
		String username = principal.getName();
		System.out.println(username);
        
		
		UserEntities user = userRepository.getUserByUserName(username);
		//user.setImageURL("static/img/OIP.jpeg");
		model.addAttribute("user", user);

	}

	// dashboard-home
	@RequestMapping("/index")
	public String index(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";

	}

	// add contact
	@GetMapping("/contact")
	public String getMethodName(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new ContactEntities());
		return "normal/addContact";
	}

	// SAVE CONTACT AND SUCCESSFUL MESSAGE SHOW//
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute("contact") ContactEntities contact,
			@RequestParam("profileimage") MultipartFile file, Principal principal, HttpServletRequest request) {
		HttpSession session = request.getSession();
		try {

			String username = principal.getName();
			UserEntities user = this.userRepository.getUserByUserName(username);

			// profile processing and uploading
			if (file.isEmpty()) {
				System.out.println("file empty");
				contact.setImage("profile-user.png");
			} else {
				// file upload in img folder
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				// file contact -->add
				contact.setImage(file.getOriginalFilename());

				// message -->show

				session.setAttribute("message", new Mymessage("Your Contact is added!!Add more", "alert-success"));

				System.out.println("file uploaded");
			}

			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println("contact" + contact);
			System.out.println("added to database");

		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
			session.setAttribute("message",
					new Mymessage("Something went Wrong try again!!" + e.getMessage(), "alert-danger"));
			e.printStackTrace();

		}
		return "normal/addContact";
	}

	// V I E W CONTACT //
	// pagination two things-
	// currentpage-->page
	// contactssperpage-->2
	@GetMapping("/viewContacts/{page}")
	public String viewContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "View Contacts");

		// principal gives unique identifier

		String username = principal.getName();

		UserEntities user = this.userRepository.getUserByUserName(username);

		// fetch all the contacts having same userid

		Pageable pageable = PageRequest.of(page, 2);
		Page<ContactEntities> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);

		// send list of contacts to the view

		model.addAttribute("Contacts", contacts);
		model.addAttribute("CurrentPage", page);
		System.out.println(contacts.getTotalPages());
		model.addAttribute("TotalPages", contacts.getTotalPages());

		return "/normal/show_contacts";
	}

	// SHOW CONTACT DETAILS

	@GetMapping("/{cID}/contact")
	public String showContactDetail(@PathVariable("cID") Integer cID, Model model, Principal principal) {
		System.out.println(cID);

		model.addAttribute("title", "Conatct Details" + cID);

		Optional<ContactEntities> contactOptional = this.contactRepository.findById(cID);
		ContactEntities contacts = contactOptional.get();

		// principal gives unique identifier

		String username = principal.getName();

		UserEntities user = this.userRepository.getUserByUserName(username);

		if (user.getId() == contacts.getUser().getId()) {
			model.addAttribute("ContactDetails", contacts);
			model.addAttribute("title", "Contact Details " + contacts.getName());
		}

		return "normal/contactDetail";
	}

	/// TO DELETE ANY CONTACT NUMBER
	@GetMapping("/delete/{cID}")
	public String getMethodName(@PathVariable("cID") Integer cID, Model model, Principal principal,
			HttpServletRequest request) {
		HttpSession session = request.getSession();

		Optional<ContactEntities> contactOptional = this.contactRepository.findById(cID);
		ContactEntities contacts = contactOptional.get();

		// principal gives unique identifier

		String username = principal.getName();

		UserEntities user = this.userRepository.getUserByUserName(username);

		if (user.getId() == contacts.getUser().getId()) {
			contacts.setUser(null);
			this.contactRepository.delete(contacts);
			session.setAttribute("message", new Mymessage("Contact deleted succesfully!!", "alert-success"));
		}

		return "redirect:/user/viewContacts/0";
	}

	@PostMapping("/update/{cID}")
	public String updateContact(@PathVariable("cID") Integer cID, Model model) {
		model.addAttribute("title", "Update Contact Details");

		Optional<ContactEntities> contactOptional = this.contactRepository.findById(cID);
		ContactEntities contacts = contactOptional.get();
		model.addAttribute("Contacts", contacts);

		return "normal/updateContact";
	}

	// to process the update deatils in backend
	@PostMapping("/process-update")
	public String processUpdateeForm(@ModelAttribute("Conatcts") ContactEntities contacts,
			@RequestParam("profileimage") MultipartFile file, Model model, HttpServletRequest request, Principal principal) {
		HttpSession session = request.getSession();
		try {
		//FETCH OLD CONATCT LIST TO GET THE OLD IMAGE TO DELETE
		ContactEntities oldcontacts=this.contactRepository.findById(contacts.getcID()).get();
		
		
		if(!file.isEmpty()) {
			//delete old file
			File deleteFile = new ClassPathResource("static/img").getFile();
			File file1=new File(deleteFile,oldcontacts.getImage());
			file1.delete();
			
			//add new file
			// file upload in img folder
			File saveFile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			// file contact -->add
			contacts.setImage(file.getOriginalFilename());

			
			
		}else {
			
			contacts.setImage(oldcontacts.getImage());
		}
		
		//set the user
		String username=principal.getName();
		UserEntities user=this.userRepository.getUserByUserName(username);
		contacts.setUser(user);
		this.contactRepository.save(contacts);
		
		session.setAttribute("message", new Mymessage("Successfully updated!!","alert-success"));
		
		}catch(Exception e){
			e.printStackTrace();
			session.setAttribute("message", new Mymessage("Something went wrong!!","alert-danger"));
			
		}
		System.out.println(contacts.getName());
		return "redirect:/user/"+contacts.getcID()+"/contact";
	}
	
	//view profile of user
	@GetMapping("/profile")
	public String userProfile(Model model) {
		
		model.addAttribute("title", "User profile");
		return "normal/userProfile";
	}
	

}
