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

package org.cloudfoundry.client.lib;

import java.net.URL;

import org.cloudfoundry.client.lib.repository.DomainRepository;
import org.cloudfoundry.client.lib.repository.OrganizationRepository;
import org.cloudfoundry.client.lib.repository.SpaceRepository;
import org.cloudfoundry.client.lib.rest.CloudControllerClient;
import org.cloudfoundry.client.lib.repository.InfoRepository;
import org.cloudfoundry.client.lib.rest.CloudControllerClientFactory;

public class CloudFoundryClient {

	private CloudFoundryClientConfiguration config;

	private CloudControllerClient cc;

	public CloudFoundryClient(URL cloudControllerUrl) {
		this.config = new CloudFoundryClientConfiguration();
		config.setCloudControllerUrl(cloudControllerUrl);
	}

	public CloudFoundryClient setCredentials(CloudCredentials credentials) {
		config.setCredentials(credentials);
		return this;
	}

	public CloudFoundryClient setDefaultOrgSpace(String orgName, String spaceName) {
		config.setDefaultOrgName(orgName);
		config.setDefaultSpaceName(spaceName);
		return this;
	}

	public CloudFoundryClient setHttpProxyConfiguration(HttpProxyConfiguration proxyConfiguration) {
		config.setProxyConfiguration(proxyConfiguration);
		return this;
	}

	public CloudFoundryClient setTrustSelfSignedCerts(boolean trustSelfSignedCerts) {
		config.setTrustSelfSignedCerts(trustSelfSignedCerts);
		return this;
	}

	public CloudFoundryClient connect() {
		this.cc = new CloudControllerClientFactory().newCloudControllerClient(config);
		return this;
	}

	public InfoRepository getInfoRepository() {
		return cc.getInfoRepository();
	}

	public OrganizationRepository getOrganizationRepository() {
		return cc.getOrganizationRepository();
	}

	public SpaceRepository getSpaceRepository() {
		return cc.getSpaceRepository();
	}

	public DomainRepository getDomainRepository() {
		return cc.getDomainRepository();
	}

}
