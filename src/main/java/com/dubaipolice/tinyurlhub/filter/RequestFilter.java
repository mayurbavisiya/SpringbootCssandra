package com.dubaipolice.tinyurlhub.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.dubaipolice.tinyurlhub.service.URLAliasService;
import com.dubaipolice.tinyurlhub.util.Utils;

@Component
public class RequestFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(RequestFilter.class);

	@Autowired
	URLAliasService service;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String uri = httpRequest.getRequestURI();
		// if (httpRequest != null && uri != null && uri.contains("/alias")) {}
		// else
		if (httpRequest != null && uri != null && uri.contains("/generateAlias")) {
			Map<String, String> map = new HashMap<String, String>();

			Enumeration<?> headerNames = httpRequest.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = httpRequest.getHeader(key);
				map.put(key, value);
			}
			logger.info("Generate Alias for : " + httpRequest.getRequestURI());
			logger.info("Generate Alias header info : " + map);
			logger.info("---------------------------------------------------");
			filterchain.doFilter(request, response);

			// System.out.println(map);
		} else {
			final String referer = Utils.getReferer(httpRequest);
			final String fullURL = Utils.getFullURL(httpRequest);
			final String clientIpAddr = Utils.getClientIpAddr(httpRequest);
			final String clientOS = Utils.getClientOS(httpRequest);
			final String clientBrowser = Utils.getClientBrowser(httpRequest);
			final String userAgent = Utils.getUserAgent(httpRequest);

			logger.info("---------------------------------------------------");

			String ClientInfo = "\n" + "User Agent \t" + userAgent + "\n" + "Operating System\t" + clientOS + "\n"
					+ "Browser Name\t" + clientBrowser + "\n" + "IP Address\t" + clientIpAddr + "\n" + "Full URL\t"
					+ fullURL + "\n" + "Referrer\t" + referer;

			int index = uri.indexOf("/", uri.indexOf("/") + 1) + 1;
			String alias = httpRequest.getRequestURI().substring(index).trim();

			CompletableFuture<String> entity;
			try {
				entity = service.getRealURL(alias);
				String redirectionURL = entity.get();
				logger.info(ClientInfo + "\n" + "Response Status\t" + HttpStatus.OK.value() + "\n" + "Redirection URL\t"
						+ redirectionURL);
				httpResponse.sendRedirect(redirectionURL);

			} catch (Exception e) {
				if (e.getMessage().contains("Alias not found")) {
					logger.info(ClientInfo + "\n" + "Response Status\t" + HttpStatus.NOT_FOUND.value());
					httpResponse.sendError(HttpStatus.NOT_FOUND.value());
				} else {
					if (e.getMessage().contains("exceeds no of hits")) {
						logger.info(ClientInfo + "\n" + "Response Status\t" + HttpStatus.BAD_REQUEST.value() + "\n"
								+ "Error Message\t Exceeds no of hits");
					} else {
						logger.info(ClientInfo + "\n" + "Response Status\t" + HttpStatus.BAD_REQUEST.value() + "\n"
								+ "Error Message\t Link is expired");
					}
					httpResponse.sendError(HttpStatus.BAD_REQUEST.value());
				}
			}

		}
		// else {
		// filterchain.doFilter(request, response);
		// }
	}

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
	}

}
