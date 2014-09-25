package org.cloudfoundry.client.lib.specs.model

import static org.junit.Assert.fail


class TestConfiguration {
	String targetApiUrl = System.getProperty("cf.target", "http://api.run.pivotal.io")
	String userEmail = System.getProperty("cf.email", "java-authenticatedClient-test-user@vmware.com")
	String userPassword = System.getProperty("cf.password")
	String organization = System.getProperty("cf.org", "gopivotal.com")
	String space = System.getProperty("cf.space", "test")
	String testNamespace = System.getProperty("vcap.test.namespace", defaultNamespace(userEmail))
	String testDomain = System.getProperty("vcap.test.domain", defaultNamespace(userEmail) + ".com")

	boolean trustSelfSignedCerts = Boolean.getBoolean("cf.trustSelfSignedCerts")

	String httpProxyHost = System.getProperty("http.proxyHost", null)
	int httpProxyPort = Integer.getInteger("http.proxyPort", 80)

	TestConfiguration() {
		validate()
	}

	private void validate() {
		validateProperty targetApiUrl, "cf.target"
		validateProperty userEmail, "cf.email"
		validateProperty userPassword, "cf.password"
		validateProperty organization, "cf.org"
		validateProperty space, "cf.space"
	}

	private static void validateProperty(String value, String systemProperty) {
		if (!value) {
			fail("System property ${systemProperty} must be specified, supply -D${systemProperty}=<value>")
		}
	}

	private static String defaultNamespace(String email) {
		return email.substring(0, email.indexOf('@')).replaceAll("\\.", "-").replaceAll("\\+", "-")
	}
}
