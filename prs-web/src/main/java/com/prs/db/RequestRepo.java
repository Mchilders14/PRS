package com.prs.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.Request;
import com.prs.business.User;

public interface RequestRepo extends CrudRepository<Request, Integer>{	// If interface uses interface, MUST extend.
	
	List<Request> findAllByUserIdNotAndStatus(int id, String status);

}