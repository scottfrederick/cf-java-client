package org.cloudfoundry.client.lib.oauth2;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class OauthRequestInterceptor implements RequestInterceptor {
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";

	private OauthClient oauthClient;

	public OauthRequestInterceptor(OauthClient oauthClient) {
		this.oauthClient = oauthClient;
	}

	@Override
	public void apply(RequestTemplate template) {
		template.header(AUTHORIZATION_HEADER_KEY, oauthClient.getAuthorizationHeader());
	}
}
