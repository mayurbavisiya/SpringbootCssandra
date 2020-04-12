package com.dubaipolice.tinyurlhub.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.utils.UUIDs;
import com.dubaipolice.tinyurlhub.model.URLAliasMappingDTO;
import com.dubaipolice.tinyurlhub.model.URLAliasMappingEntity;
import com.dubaipolice.tinyurlhub.repository.URLAliasRepository;
import com.dubaipolice.tinyurlhub.service.URLAliasService;
import com.dubaipolice.tinyurlhub.util.Constants;
import com.dubaipolice.tinyurlhub.util.ExceptionUtil;
import com.dubaipolice.tinyurlhub.util.ExceptionValidationsConstants;
import com.dubaipolice.tinyurlhub.util.Utils;

@Service
@PropertySource("classpath:application.properties")
public class URLAliasServiceImpl implements URLAliasService {

//	private static Logger logger = LoggerFactory.getLogger(URLAliasController.class);

	@Autowired
	URLAliasRepository repository;

	@Value("${urlConstantDomain}")
	private String urlConstantDomain;

	@Value("${defaultExpirayTime}")
	private int defaultExpirayTime;

	@Value("${defaultNoOfHits}")
	private int defaultNoOfHits;

	@Override
	@Async
	public CompletableFuture<String> createAlias(URLAliasMappingDTO dto) throws Exception {

		Utils.validateRequest(dto);
		List<URLAliasMappingEntity> list = new ArrayList<URLAliasMappingEntity>();
		// Check If Real URL is already exist
		repository.findByRealurl(dto.getRealurl().trim()).forEach(list::add);
		if (!list.isEmpty()) {
			list.get(0).setDate(Utils.getDateFromUUID(list.get(0).getCreated_date(), Constants.ddmmyyyyHHmmss));
			list.get(0).setAlias(urlConstantDomain + list.get(0).getAlias());
			return CompletableFuture.completedFuture(list.get(0).getAlias());
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
					(dto.getExpirytime() > 0) ? dto.getExpirytime() : defaultExpirayTime,
					(dto.getUrlAccessLimit() > 0) ? dto.getUrlAccessLimit() : defaultNoOfHits, sysDate, 0));

			String date = Utils.getDateFromUUID(sysDate, Constants.ddmmyyyyHHmmss);
			enity.setDate(date);
			enity.setAlias(urlConstantDomain + enity.getAlias());
			return CompletableFuture.completedFuture(enity.getAlias());
		}

	}

	@Override
	@Async
	public CompletableFuture<String> getRealURL(String alias) throws Exception {

		List<URLAliasMappingEntity> list = new ArrayList<URLAliasMappingEntity>();
		repository.findByAlias(alias.trim()).forEach(list::add);
		if (!list.isEmpty()) {
			int expiryDays = list.get(0).getExpirytime();
			UUID createdDate = list.get(0).getCreated_date();
			if (!Utils.isLinkExpired(createdDate, expiryDays)) {
				if (list.get(0).getNoOfHit() < list.get(0).getUrlAccessLimit()) {

					// Pointer reach here means its valid and not expired url.
					// Hence we will increament counter
					list.get(0).setNoOfHit(list.get(0).getNoOfHit() + 1);
					repository.save(list.get(0));

					list.get(0).setDate(Utils.getDateFromUUID(list.get(0).getCreated_date(), Constants.ddmmyyyyHHmmss));// Changing
																														// date
																														// format
																														// in
																														// readable
																														// format
					return CompletableFuture.completedFuture(list.get(0).getRealurl());
				} else
					ExceptionUtil.throwException(ExceptionValidationsConstants.EXCEEDS_NO_OF_HIT,
							ExceptionUtil.EXCEPTION_VALIDATION);
			} else
				ExceptionUtil.throwException(ExceptionValidationsConstants.LINK_EXPIRED,
						ExceptionUtil.EXCEPTION_VALIDATION);

		} else
			ExceptionUtil.throwException(ExceptionValidationsConstants.ALIAS_NOT_FOUND,
					ExceptionUtil.EXCEPTION_VALIDATION);

		return null;

	}

}
