package com.SmartContact.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ContactEntities {
  
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cID;
	private String name;
	private String secondName;
	@Column(unique = true)
	private String email;
	private String work;
	private String phone;
	
	private String image;

	@Column(length = 1000)
	private String descriptionOfContact;
	
	@ManyToOne
	private UserEntities userEntities;
	
	
	
	@Override
	public String toString() {
		return "contactEntities [cID=" + cID + ", name=" + name + ", secondName=" + secondName + ", email=" + email
				+ ", work=" + work + ", phone=" + phone + ", image=" + image + ", descriptionOfContact="
				+ descriptionOfContact + ", user=" + userEntities + "]" ;
	}


	public ContactEntities() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	public ContactEntities(int cID, String name, String secondName, String email, String work, String phone,
			String image, String descriptionOfContact, UserEntities userEntities) {
		super();
		this.cID = cID;
		this.name = name;
		this.secondName = secondName;
		this.email = email;
		this.work = work;
		this.phone = phone;
		this.image = image;
		this.descriptionOfContact = descriptionOfContact;

this.userEntities = userEntities;
	}


	public int getcID() {
		return cID;
	}


	public void setcID(int cID) {
		this.cID = cID;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSecondName() {
		return secondName;
	}


	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getWork() {
		return work;
	}


	public void setWork(String work) {
		this.work = work;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public String getDescriptionOfContact() {
		return descriptionOfContact;
	}


	public void setDescriptionOfContact(String descriptionOfContact) {
		this.descriptionOfContact = descriptionOfContact;
	}


	

public UserEntities getUser() {
		return userEntities;
	}


	public void setUser(UserEntities user) {
		this.userEntities = user;
	}


	
	
}