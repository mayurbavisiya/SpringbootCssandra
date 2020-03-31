package com.dubaipolice.tinyurlhub.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.utils.UUIDs;
import com.dubaipolice.tinyurlhub.model.URLAliasMappingDTO;
import com.dubaipolice.tinyurlhub.model.URLAliasMappingEntity;
import com.dubaipolice.tinyurlhub.repository.URLAliasRepository;
import com.dubaipolice.tinyurlhub.util.GenerateAlias;

@RestController
@RequestMapping("/tinyurl")
public class URLAliasController {

	@Autowired
	URLAliasRepository tutorialRepository;

	@GetMapping("/alias")
	public ResponseEntity<List<URLAliasMappingEntity>> getAllData() {
		try {
			List<URLAliasMappingEntity> data = new ArrayList<URLAliasMappingEntity>();

			tutorialRepository.findAll().forEach(data::add);

			if (data.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/alias")
	public ResponseEntity<URLAliasMappingEntity> createData(@RequestBody URLAliasMappingDTO dto) {
		try {
			boolean isValidAlias = false;
			List<URLAliasMappingEntity> list = new ArrayList<URLAliasMappingEntity>();
			String alias = "";
			while (!isValidAlias) {
				alias = GenerateAlias.generateUniqueAlias();
				tutorialRepository.findByAlias(alias).forEach(list::add);
				if (list.isEmpty()) {
					isValidAlias = true;
				}
			}

			URLAliasMappingEntity _tutorial = tutorialRepository.save(new URLAliasMappingEntity(dto.getRealurl(), alias,
					(dto.getExpirytime() > 0) ? dto.getExpirytime() : 30,
					(dto.getUrlAccessLimit() > 0) ? dto.getUrlAccessLimit() : 5, UUIDs.timeBased()));
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
}
