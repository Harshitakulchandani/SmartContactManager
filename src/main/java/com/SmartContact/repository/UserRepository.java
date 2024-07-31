package com.SmartContact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SmartContact.entities.UserEntities;

public interface UserRepository extends JpaRepository<UserEntities, Integer>{

	
	@Query("SELECT u FROM UserEntities u WHERE u.email = :email")
	public UserEntities getUserByUserName(@Param("email") String email);
	
	
	
	

}
