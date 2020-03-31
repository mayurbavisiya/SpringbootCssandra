package com.dubaipolice.tinyurlhub.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.utils.UUIDs;
import com.dubaipolice.tinyurlhub.model.CreateAliasDTO;
import com.dubaipolice.tinyurlhub.model.URLAliasMappingEntity;
import com.dubaipolice.tinyurlhub.repository.URLAliasRepository;
import com.dubaipolice.tinyurlhub.util.Utils;

@RestController
@RequestMapping("/tinyurl")
public class URLAliasController {
	
	private static Logger logger = LoggerFactory.getLogger(URLAliasController.class);


	@Autowired
	URLAliasRepository repository;

	@PostMapping("/alias")
	public ResponseEntity<URLAliasMappingEntity> createAlias(@RequestBody CreateAliasDTO dto) {
		try {
			List<URLAliasMappingEntity> list = new ArrayList<URLAliasMappingEntity>();
			// Check If Real URL is already exist
			repository.findByRealurl(dto.getRealurl().trim()).forEach(list::add);
			logger.info("not exist");
			if (!list.isEmpty()) {
				list.get(0).setDate(Utils.getDateFromUUID(list.get(0).getCreated_date(), "dd-MMM-yyyy hh:mm:ss"));
				return new ResponseEntity<>(list.get(0), HttpStatus.FOUND);
			} else {
				boolean isValidAlias = false;
				list = new ArrayList<>();
				String alias = "";
				while (!isValidAlias) {
					alias = Utils.generateUniqueAlias();
					repository.findByAlias(alias).forEach(list::add);
					if (list.isEmpty()) {
						isValidAlias = true;
					}
				}

				UUID sysDate = UUIDs.timeBased();
				URLAliasMappingEntity enity = repository.save(new URLAliasMappingEntity(dto.getRealurl().trim(), alias,
						(dto.getExpirytime() > 0) ? dto.getExpirytime() : 30,
						(dto.getUrlAccessLimit() > 0) ? dto.getUrlAccessLimit() : 5, sysDate, 0));

				String date = Utils.getDateFromUUID(sysDate, "dd-MMM-yyyy hh:mm:ss");
				enity.setDate(date);
				return new ResponseEntity<>(enity, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/realURL")
	public ResponseEntity<URLAliasMappingEntity> getRealURL(@RequestParam String alias) {
		try {
			List<URLAliasMappingEntity> list = new ArrayList<URLAliasMappingEntity>();
			repository.findByAlias(alias.trim()).forEach(list::add);
			if (!list.isEmpty()) {
				int expiryDays = list.get(0).getExpirytime();
				UUID createdDate = list.get(0).getCreated_date();
				if (!Utils.isLinkExpired(createdDate, expiryDays)) {
					if (list.get(0).getNoOfHit() <= list.get(0).getUrlAccessLimit()) {
						list.get(0).setDate(Utils.getDateFromUUID(list.get(0).getCreated_date(), "dd-MMM-yyyy hh:mm:ss"));
						return new ResponseEntity<>(list.get(0), HttpStatus.OK);
					} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
					}
				} else {
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
}
