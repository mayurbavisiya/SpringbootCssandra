package com.dubaipolice.tinyurlhub.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.utils.UUIDs;
import com.dubaipolice.tinyurlhub.model.Test;
import com.dubaipolice.tinyurlhub.repository.TestRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TinyURLHubController {

	@Autowired
	TestRepository tutorialRepository;

	@GetMapping("/data")
	public ResponseEntity<List<Test>> getAllData() {
		try {
			List<Test> data = new ArrayList<Test>();

			tutorialRepository.findAll().forEach(data::add);

			if (data.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/data")
	public ResponseEntity<Test> createTutorial(@RequestBody Test data) {
		try {
			Test _tutorial = tutorialRepository.save(new Test(UUIDs.timeBased(), data.getName(), data.getDescription()));
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

}
