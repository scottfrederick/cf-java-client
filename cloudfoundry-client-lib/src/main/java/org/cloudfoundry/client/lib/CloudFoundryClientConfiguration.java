package org.cloudfoundry.client.lib;

import java.net.URL;

public class CloudFoundryClientConfiguration {
	private URL cloudControllerUrl;

	private CloudCredentials credentials;

	private String defaultOrgName;
	private String defaultSpaceName;

	private HttpProxyConfiguration proxyConfiguration;
	private boolean trustSelfSignedCerts;

	public URL getCloudControllerUrl() {
		return cloudControllerUrl;
	}

	public void setCloudControllerUrl(URL cloudControllerUrl) {
		this.cloudControllerUrl = cloudControllerUrl;
	}

	public CloudCredentials getCredentials() {
		return credentials;
	}

	public void setCredentials(CloudCredentials credentials) {
		this.credentials = credentials;
	}

	public String getDefaultOrgName() {
		return defaultOrgName;
	}

	public void setDefaultOrgName(String defaultOrgName) {
		this.defaultOrgName = defaultOrgName;
	}

	public String getDefaultSpaceName() {
		return defaultSpaceName;
	}

	public void setDefaultSpaceName(String defaultSpaceName) {
		this.defaultSpaceName = defaultSpaceName;
	}

	public HttpProxyConfiguration getProxyConfiguration() {
		return proxyConfiguration;
	}

	public void setProxyConfiguration(HttpProxyConfiguration proxyConfiguration) {
		this.proxyConfiguration = proxyConfiguration;
	}

	public boolean isTrustSelfSignedCerts() {
		return trustSelfSignedCerts;
	}

	public void setTrustSelfSignedCerts(boolean trustSelfSignedCerts) {
		this.trustSelfSignedCerts = trustSelfSignedCerts;
	}

}
