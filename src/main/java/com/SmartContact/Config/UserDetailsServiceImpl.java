package com.SmartContact.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.SmartContact.entities.UserEntities;
import com.SmartContact.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntities user=userRepository.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("could not found user");
		}
		
		
		CustomUserDetails customDetails=new CustomUserDetails(user);
		return customDetails;
	}

}
