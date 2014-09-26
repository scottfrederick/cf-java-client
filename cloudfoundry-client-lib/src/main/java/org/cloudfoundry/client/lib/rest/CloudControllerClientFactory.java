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

import org.cloudfoundry.client.lib.CloudFoundryClientConfiguration;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.oauth2.OAuthClient;

public class CloudControllerClientFactory {
	public CloudControllerClientFactory() {
	}

	public CloudControllerClient newCloudControllerClient(CloudFoundryClientConfiguration config) {
		OAuthClient oauthClient = createOauthClient(config);

		CloudControllerClient cloudControllerClient = createCloudControllerClient(config, oauthClient);

		String authorizationEndpoint = getAuthorizationEndpoint(cloudControllerClient);

		oauthClient.login(authorizationEndpoint, config.getCredentials());

		cloudControllerClient.validate();

		return cloudControllerClient;
	}

	private OAuthClient createOauthClient(CloudFoundryClientConfiguration config) {
		return new OAuthClient(config.getProxyConfiguration(), config.isTrustSelfSignedCerts());
	}

	private CloudControllerClient createCloudControllerClient(CloudFoundryClientConfiguration config, OAuthClient oauthClient) {
		return new CloudControllerClient(config, oauthClient);
	}

	private String getAuthorizationEndpoint(CloudControllerClient cloudControllerClient) {
		CloudInfo cloudInfo = cloudControllerClient.getInfoRepository().getCloudInfo();
		return cloudInfo.getAuthorizationEndpoint();
	}
}
