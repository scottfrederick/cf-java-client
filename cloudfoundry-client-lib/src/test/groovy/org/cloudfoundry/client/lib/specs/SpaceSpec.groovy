package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.domain.PagedResource
import org.cloudfoundry.client.lib.domain.Space

class SpaceSpec extends CloudFoundryClientSpecification {
	def "spaces can be listed"() {
		given:
		clientIsConnected()

		when:
		PagedResource<Space> spaceResources = spaceRepository.getAll()

		then:
		assertResourcesSet(spaceResources)

		def spaces = spaceResources.resources

		Space configSpace = spaces.find { it.name == config.space }
		configSpace != null
		configSpace.organization.name == config.organization

		spaces.each { Space space ->
			assertMetaIsSet(space)

			space.organizationGuid != null
			space.spaceQuotaDefinitionGuid != null

			space.organization != null
			assertMetaIsSet(space.organization)
		}
	}
}