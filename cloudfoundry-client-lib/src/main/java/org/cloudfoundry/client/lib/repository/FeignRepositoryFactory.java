package org.cloudfoundry.client.lib.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.cloudfoundry.client.lib.oauth2.OauthClient;
import org.cloudfoundry.client.lib.oauth2.OauthRequestInterceptor;

import java.net.URL;

public class FeignRepositoryFactory {
	private final URL cloudControllerUrl;
	private final OauthClient oauthClient;
	private final ObjectMapper objectMapper;

	public FeignRepositoryFactory(URL cloudControllerUrl) {
		this(cloudControllerUrl, null);
	}

	public FeignRepositoryFactory(URL cloudControllerUrl, OauthClient oauthClient) {
		this.cloudControllerUrl = cloudControllerUrl;
		this.oauthClient = oauthClient;

		this.objectMapper = createObjectMapper();
	}

	public <T> T createUnauthenticatedRepository(Class<T> repositoryType) {
		return Feign.builder()
				.encoder(new JacksonEncoder(objectMapper))
				.decoder(new JacksonDecoder(objectMapper))
				.target(repositoryType, cloudControllerUrl.toString());
	}

	public <T> T createRepository(Class<T> repositoryType) {
		return Feign.builder()
				.requestInterceptor(new OauthRequestInterceptor(oauthClient))
				.encoder(new JacksonEncoder(objectMapper))
				.decoder(new JacksonDecoder(objectMapper))
				.target(repositoryType, cloudControllerUrl.toString());
	}

	private ObjectMapper createObjectMapper() {
		return new ObjectMapper()
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
				.configure(SerializationFeature.INDENT_OUTPUT, true)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.setDateFormat(new ISO8601DateFormat());
	}
}
