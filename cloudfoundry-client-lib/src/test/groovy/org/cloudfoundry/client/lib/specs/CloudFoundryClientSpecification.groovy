package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.CloudCredentials
import org.cloudfoundry.client.lib.CloudFoundryClient
import org.cloudfoundry.client.lib.domain.Metadata
import org.cloudfoundry.client.lib.domain.PagedResource
import org.cloudfoundry.client.lib.domain.Resource
import org.cloudfoundry.client.lib.repository.DomainRepository
import org.cloudfoundry.client.lib.repository.InfoRepository
import org.cloudfoundry.client.lib.repository.OrganizationRepository
import org.cloudfoundry.client.lib.repository.SpaceRepository
import org.cloudfoundry.client.lib.specs.model.TestConfiguration
import spock.lang.Shared
import spock.lang.Specification

class CloudFoundryClientSpecification extends Specification {
	@Shared TestConfiguration config
	@Shared CloudFoundryClient client = null

	def setupSpec() {
		config = new TestConfiguration()

		cleanupTarget()
	}

	def cleanupSpec() {
		cleanupTarget()
	}

	private void cleanupTarget() {
		clientIsConnected()
	}

	void clientIsConnected() {
		if (client == null) {
			URL cloudControllerUrl = new URL(config.targetApiUrl)
			client = new CloudFoundryClient(new CloudCredentials(config.userEmail, config.userPassword),
					cloudControllerUrl, config.organization, config.space, config.trustSelfSignedCerts)
			client.login()
		}
	}

	InfoRepository getInfoRepository() {
		return client.infoRepository
	}

	OrganizationRepository getOrganizationRepository() {
		return client.organizationRepository
	}

	SpaceRepository getSpaceRepository() {
		return client.spaceRepository
	}

	DomainRepository getDomainRepository() {
		return client.domainRepository
	}

	void assertResourcesSet(PagedResource pagedResource) {
		pagedResource.resources != null
		pagedResource.resources.size() != null
		pagedResource.resources.size() == pagedResource.totalResults
	}

	void assertMetaIsSet(Resource resource) {
		Metadata meta = resource.metadata

		assert meta.guid != null
		assert meta.url != null

		def now = new Date()
		assert meta.createdAt != null
		assert meta.createdAt.before(now)

		if (meta.updatedAt != null) {
			assert meta.updatedAt.before(now)
		}
	}
}