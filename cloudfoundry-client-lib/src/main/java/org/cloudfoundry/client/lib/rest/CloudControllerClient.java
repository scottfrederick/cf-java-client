/*
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.lib.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.cloudfoundry.client.lib.CloudFoundryClientConfiguration;
import org.cloudfoundry.client.lib.domain.Organization;
import org.cloudfoundry.client.lib.domain.PagedResource;
import org.cloudfoundry.client.lib.domain.Space;
import org.cloudfoundry.client.lib.oauth2.OAuthClient;
import org.cloudfoundry.client.lib.oauth2.OAuthRequestInterceptor;
import org.cloudfoundry.client.lib.repository.DomainRepository;
import org.cloudfoundry.client.lib.repository.InfoRepository;
import org.cloudfoundry.client.lib.repository.OrganizationRepository;
import org.cloudfoundry.client.lib.repository.SpaceRepository;

public class CloudControllerClient {
	private final CloudFoundryClientConfiguration config;

	private final OAuthClient oauthClient;

	private final ObjectMapper objectMapper;

	private Space sessionSpace = null;

	public CloudControllerClient(CloudFoundryClientConfiguration config, OAuthClient oauthClient) {
		this.config = config;

		this.oauthClient = oauthClient;

		this.objectMapper = createObjectMapper();
	}

	public void validate() {
		this.sessionSpace = validateOrgAndSpace(config.getDefaultOrgName(), config.getDefaultSpaceName());
	}

	public InfoRepository getInfoRepository() {
		return createRepository(InfoRepository.class);
	}

	public OrganizationRepository getOrganizationRepository() {
		return createRepository(OrganizationRepository.class);
	}

	public SpaceRepository getSpaceRepository() {
		return createRepository(SpaceRepository.class);
	}

	public DomainRepository getDomainRepository() {
		return createRepository(DomainRepository.class);
	}

	private <T> T createRepository(Class<T> repositoryType) {
		return Feign.builder()
				.requestInterceptor(new OAuthRequestInterceptor(oauthClient))
				.encoder(new JacksonEncoder(objectMapper))
				.decoder(new JacksonDecoder(objectMapper))
				.target(repositoryType, config.getCloudControllerUrl().toString());
	}

	private ObjectMapper createObjectMapper() {
		return new ObjectMapper()
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
				.configure(SerializationFeature.INDENT_OUTPUT, true)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.setDateFormat(new ISO8601DateFormat());
	}

	private Space validateOrgAndSpace(String orgName, String spaceName) {
		SpaceRepository spaceRepository = getSpaceRepository();

		PagedResource<Space> spaces = spaceRepository.getAll();

		for (Space space : spaces.getResources()) {
			if (space.getName().equals(spaceName)) {
				Organization org = space.getOrganization();
				if (orgName == null || org.getName().equals(orgName)) {
					return space;
				}
			}
		}

		throw new IllegalArgumentException("No matching organization and space found for org: " + orgName + " space: " + spaceName);
	}
}
