package com.SmartContact.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SmartContact.entities.ContactEntities;

public interface ContactRepository extends JpaRepository<ContactEntities, Integer>{
	
	//PAGINATION USE
	//pageable is a class --->consist of two variables(page , size)
	
	@Query("from ContactEntities as c where c.userEntities.id =:userEntitiesID")
	public Page<ContactEntities> findContactsByUser(@Param("userEntitiesID") int userEntitiesID , Pageable pageable);
	
	
	

	

}
