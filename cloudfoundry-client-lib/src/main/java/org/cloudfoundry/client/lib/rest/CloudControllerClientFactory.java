/*
 * Copyright 2009-2012 the original author or authors.
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

import java.net.MalformedURLException;
import java.net.URL;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.HttpProxyConfiguration;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.oauth2.OauthClient;
import org.cloudfoundry.client.lib.repository.FeignRepositoryFactory;
import org.cloudfoundry.client.lib.repository.InfoRepository;

public class CloudControllerClientFactory {
	private final HttpProxyConfiguration httpProxyConfiguration;
	private final boolean trustSelfSignedCerts;

	public CloudControllerClientFactory(HttpProxyConfiguration httpProxyConfiguration, boolean trustSelfSignedCerts) {
		this.httpProxyConfiguration = httpProxyConfiguration;
		this.trustSelfSignedCerts = trustSelfSignedCerts;
	}

	public CloudControllerClient newCloudController(URL cloudControllerUrl, CloudCredentials cloudCredentials,
	                                                String orgName, String spaceName) {
		OauthClient oauthClient = createOauthClient(cloudControllerUrl);

		return new CloudControllerClient(cloudControllerUrl, oauthClient, cloudCredentials, orgName, spaceName);
	}

	private OauthClient createOauthClient(URL cloudControllerUrl) {
		FeignRepositoryFactory factory = new FeignRepositoryFactory(cloudControllerUrl);
		InfoRepository infoRepository = factory.createUnauthenticatedRepository(InfoRepository.class);
		CloudInfo cloudInfo = infoRepository.getCloudInfo();
		return new OauthClient(getAuthorizationUrl(cloudInfo.getAuthorizationEndpoint()), httpProxyConfiguration, trustSelfSignedCerts);
	}

	private URL getAuthorizationUrl(String authorizationEndpoint) {
		try {
			return new URL(authorizationEndpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Error creating authorization endpoint URL for endpoint " + authorizationEndpoint, e);
		}
	}
}
