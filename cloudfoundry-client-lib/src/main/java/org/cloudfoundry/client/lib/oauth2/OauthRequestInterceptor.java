package org.cloudfoundry.client.lib.oauth2;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class OAuthRequestInterceptor implements RequestInterceptor {
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";

	private OAuthClient oauthClient;

	public OAuthRequestInterceptor(OAuthClient oauthClient) {
		this.oauthClient = oauthClient;
	}

	@Override
	public void apply(RequestTemplate template) {
		if (oauthClient.isLoggedIn()) {
			template.header(AUTHORIZATION_HEADER_KEY, oauthClient.getAuthorizationHeader());
		}
	}
}
