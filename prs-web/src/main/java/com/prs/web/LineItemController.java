package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.LineItem;
import com.prs.db.LineItemRepo;


@CrossOrigin
@RestController
@RequestMapping("/api/lineitem")
public class LineItemController {

	@Autowired
	private LineItemRepo lineItemRepo;

	@GetMapping("/")
	public Iterable<LineItem> getAll() {
		return lineItemRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<LineItem> get(@PathVariable int id) {
		return lineItemRepo.findById(id);
	}
	
	// Get all LineItem(s) by RequestID
	@GetMapping("/lines-for-pr/{id}")
	public List<LineItem> getByRequestId(@PathVariable int id) {	// Returns List of type <LineItem>
		return lineItemRepo.findAllByRequestId(id);
	}

	@PostMapping("/")
	public LineItem add(@RequestBody LineItem lineItem) {
		return lineItemRepo.save(lineItem);
	}

	@PutMapping("/")
	public LineItem update(@RequestBody LineItem lineItem) {
		return lineItemRepo.save(lineItem);
	}

	@DeleteMapping("/{id}")
	public Optional<LineItem> delete(@PathVariable int id) {
		Optional<LineItem> lineItem = lineItemRepo.findById(id);
		if (lineItem.isPresent()) {
			try {
				lineItemRepo.deleteById(id);
			}
			catch (DataIntegrityViolationException dive) {
				// catch dive when movie exists as fk on another table
				System.err.println(dive.getRootCause().getMessage());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Foreign Key Constraint Issue - lineItem id: "+id+ " is "
								+ "referred to elsewhere.");
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
						"Exception caught during request delete.");
			}
		}
		else {
			System.err.println("LineItem delete error - no lineItem found for id:"+id);
		}
		return lineItem;
	}

}


