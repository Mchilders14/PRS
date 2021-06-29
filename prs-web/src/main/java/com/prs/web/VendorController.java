package com.prs.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.Vendor;
import com.prs.db.VendorRepo;


@CrossOrigin
@RestController
@RequestMapping("/api/vendor")
public class VendorController {

	@Autowired
	private VendorRepo vendorRepo;

	@GetMapping("/")
	public Iterable<Vendor> getAll() {
		return vendorRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Vendor> get(@PathVariable int id) {
		return vendorRepo.findById(id);
	}

	@PostMapping("/")
	public Vendor add(@RequestBody Vendor vendor) {
		return vendorRepo.save(vendor);
	}

	@PutMapping("/")
	public Vendor update(@RequestBody Vendor vendor) {
		return vendorRepo.save(vendor);
	}

	@DeleteMapping("/{id}")
	public Optional<Vendor> delete(@PathVariable int id) {
		Optional<Vendor> vendor = vendorRepo.findById(id);
		if (vendor.isPresent()) {
			try {
				vendorRepo.deleteById(id);
			}
			catch (DataIntegrityViolationException dive) {
				// catch dive when movie exists as fk on another table
				System.err.println(dive.getRootCause().getMessage());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Foreign Key Constraint Issue - vendor id: "+id+ " is "
								+ "referred to elsewhere.");
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
						"Exception caught during vendor delete.");
			}
		}
		else {
			System.err.println("Vendor delete error - no vendor found for id:"+id);
		}
		return vendor;
	}

}

