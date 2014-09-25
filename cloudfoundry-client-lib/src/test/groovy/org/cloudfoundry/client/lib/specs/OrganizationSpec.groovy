package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.domain.Organization
import org.cloudfoundry.client.lib.domain.PagedResource

class OrganizationSpec extends CloudFoundryClientSpecification {
	def "organizations can be listed"() {
		given:
		clientIsConnected()

		when:
		PagedResource<Organization> orgResources = organizationRepository.getAll()

		then:
		assertResourcesSet(orgResources)

		def orgs = orgResources.resources

		orgs.find { it.name == config.organization }

		orgs.each { Organization org ->
			assertMetaIsSet(org)
		}
	}
}