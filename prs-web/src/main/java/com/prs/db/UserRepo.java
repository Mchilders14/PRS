package com.prs.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.User;

public interface UserRepo extends CrudRepository<User, Integer>{	// If interface uses interface, MUST extend.
	
	Optional<User> findAllByUsernameAndPassword(String username, String password);

}