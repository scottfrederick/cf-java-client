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
import org.cloudfoundry.client.lib.rest.CloudControllerClientFactory;
import org.cloudfoundry.client.lib.repository.InfoRepository;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class CloudFoundryClient {

	private CloudControllerClient cc;

	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName) {
		this(credentials, cloudControllerUrl, orgName, spaceName, null, false);
	}

	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName,
	                          boolean trustSelfSignedCerts) {
		this(credentials, cloudControllerUrl, orgName, spaceName, null, trustSelfSignedCerts);
	}

	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName,
	                          HttpProxyConfiguration httpProxyConfiguration) {
		this(credentials, cloudControllerUrl, orgName, spaceName, httpProxyConfiguration, false);
	}

	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName,
	                          HttpProxyConfiguration httpProxyConfiguration, boolean trustSelfSignedCerts) {
		CloudControllerClientFactory cloudControllerClientFactory =
				new CloudControllerClientFactory(httpProxyConfiguration, trustSelfSignedCerts);
		this.cc = cloudControllerClientFactory.newCloudController(cloudControllerUrl, credentials, orgName, spaceName);
	}

	public OAuth2AccessToken login() {
		return cc.login();
	}

	public void logout() {
		cc.logout();
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
