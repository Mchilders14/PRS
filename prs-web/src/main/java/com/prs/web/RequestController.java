package com.prs.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.Request;
import com.prs.business.User;
import com.prs.db.RequestRepo;
import com.prs.db.UserRepo;


@CrossOrigin
@RestController
@RequestMapping("/api/request")
public class RequestController {

	@Autowired
	private RequestRepo requestRepo;
	
	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public Iterable<Request> getAll() {
		return requestRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Request> get(@PathVariable int id) {
		return requestRepo.findById(id);
	}
	
	// show requests in review status and not assigned to logged in user
	@GetMapping("/list-review/{id}")
	public List<Request> getAllReview(@PathVariable int id) {
		return requestRepo.findAllByUserIdNotAndStatus(id, "Review");
	}

	@PostMapping("/")
	public Request add(@RequestBody Request request) {
		request.setStatus("New");	// Set default Request status to New
		request.setSubmittedDate(LocalDateTime.now());	// Using LocalDate Class to get Current Date && Time
		return requestRepo.save(request);
	}

	@PutMapping("/")
	public Request update(@RequestBody Request request) {
		return requestRepo.save(request);
	}
	
	@PutMapping("/submit-review")
	public Request submitReview(@RequestBody Request request) {
		request.setStatus(request.getTotal() <= 50 ? "Approved" : "Review");
		request.setSubmittedDate(LocalDateTime.now());	// Using LocalDate Class to get Current Date && Time
		return requestRepo.save(request);
	}
	
	@PutMapping("/approve")
	public Request approve(@RequestBody Request request) {
		request.setStatus("Approved");
		return requestRepo.save(request);
	}
	
	@PutMapping("/reject")
	public Request reject(@RequestBody Request request) {
		request.setStatus("Rejected");
		return requestRepo.save(request);
	}

	@DeleteMapping("/{id}")
	public Optional<Request> delete(@PathVariable int id) {
		Optional<Request> request = requestRepo.findById(id);
		if (request.isPresent()) {
			try {
				requestRepo.deleteById(id);
			}
			catch (DataIntegrityViolationException dive) {
				// catch dive when movie exists as fk on another table
				System.err.println(dive.getRootCause().getMessage());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Foreign Key Constraint Issue - request id: "+id+ " is "
								+ "referred to elsewhere.");
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
						"Exception caught during request delete.");
			}
		}
		else {
			System.err.println("Request delete error - no vendor found for id:"+id);
		}
		return request;
	}

}
