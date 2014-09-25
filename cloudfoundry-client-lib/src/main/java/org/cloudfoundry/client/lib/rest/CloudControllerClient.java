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

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.domain.Organization;
import org.cloudfoundry.client.lib.domain.PagedResource;
import org.cloudfoundry.client.lib.domain.Space;
import org.cloudfoundry.client.lib.oauth2.OauthClient;
import org.cloudfoundry.client.lib.repository.DomainRepository;
import org.cloudfoundry.client.lib.repository.FeignRepositoryFactory;
import org.cloudfoundry.client.lib.repository.InfoRepository;
import org.cloudfoundry.client.lib.repository.OrganizationRepository;
import org.cloudfoundry.client.lib.repository.SpaceRepository;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.net.URL;

public class CloudControllerClient {

	private OauthClient oauthClient;

	protected CloudCredentials cloudCredentials;

	protected Space sessionSpace;

	private FeignRepositoryFactory repositoryFactory;

	public CloudControllerClient(URL cloudControllerUrl, OauthClient oauthClient,
	                             CloudCredentials cloudCredentials, String orgName, String spaceName) {
		initialize(cloudControllerUrl, oauthClient, cloudCredentials);

		this.sessionSpace = validateSpaceAndOrg(spaceName, orgName);
	}

	private void initialize(URL cloudControllerUrl, OauthClient oauthClient, CloudCredentials cloudCredentials) {
		oauthClient.init(cloudCredentials);

		this.cloudCredentials = cloudCredentials;

		this.oauthClient = oauthClient;

		this.repositoryFactory = new FeignRepositoryFactory(cloudControllerUrl, oauthClient);
	}

	private Space validateSpaceAndOrg(String spaceName, String orgName) {
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

	public InfoRepository getInfoRepository() {
		return repositoryFactory.createRepository(InfoRepository.class);
	}

	public OrganizationRepository getOrganizationRepository() {
		return repositoryFactory.createRepository(OrganizationRepository.class);
	}

	public SpaceRepository getSpaceRepository() {
		return repositoryFactory.createRepository(SpaceRepository.class);
	}

	public DomainRepository getDomainRepository() {
		return repositoryFactory.createRepository(DomainRepository.class);
	}

	public OAuth2AccessToken login() {
		oauthClient.init(cloudCredentials);
		return oauthClient.getToken();
	}

	public void logout() {
		oauthClient.clear();
	}
}
