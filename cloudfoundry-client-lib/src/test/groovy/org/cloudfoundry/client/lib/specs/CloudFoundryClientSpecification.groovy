package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.CloudCredentials
import org.cloudfoundry.client.lib.CloudFoundryClient
import org.cloudfoundry.client.lib.domain.CloudDomain
import org.cloudfoundry.client.lib.domain.CloudEntity
import org.cloudfoundry.client.lib.specs.model.TestApplication
import org.cloudfoundry.client.lib.specs.model.TestConfiguration
import spock.lang.Shared
import spock.lang.Specification

class CloudFoundryClientSpecification extends Specification {
	@Shared TestConfiguration config
	@Shared CloudFoundryClient client = null
	@Shared CloudDomain defaultDomain = null

	def setupSpec() {
		config = new TestConfiguration()

		cleanupTarget()
	}

	def cleanupSpec() {
		cleanupTarget()
	}

	private void cleanupTarget() {
		clientIsConnected()
		client.deleteAllApplications()
		client.deleteAllServices()
	}

	void clientIsConnected() {
		if (client == null) {
			URL cloudControllerUrl = new URL(config.targetApiUrl)
			client = new CloudFoundryClient(new CloudCredentials(config.userEmail, config.userPassword),
					cloudControllerUrl, config.organization, config.space)
			client.login()
		}
	}

	TestApplication application(closure) {
		def testApp = new TestApplication(client)
		testApp.with closure
		testApp
	}

	void assertMetaIsSet(CloudEntity.Meta meta) {
		assert meta.guid != null
		assert meta.created != null
	}

	String getNamespace() {
		config.testNamespace
	}

	String getDefaultDomainName() {
		if (defaultDomain == null) {
			defaultDomain = getDefaultDomainForOrg()
		}
		defaultDomain?.getName()
	}

	private CloudDomain getDefaultDomainForOrg() {
		List<CloudDomain> domains = client.getDomainsForOrg()
		for (CloudDomain domain : domains) {
			if (domain.getOwner().getName().equals("none")) {
				return domain;
			}
		}
		return null;
	}
}