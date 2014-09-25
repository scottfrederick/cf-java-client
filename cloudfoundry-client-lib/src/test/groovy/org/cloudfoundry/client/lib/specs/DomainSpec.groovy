package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.domain.Domain
import org.cloudfoundry.client.lib.domain.PagedResource

class DomainSpec extends CloudFoundryClientSpecification {
	def "shared domains can be listed"() {
		given:
		clientIsConnected()

		when:
		PagedResource<Domain> domainResources = domainRepository.getSharedDomains()

		then:
		assertResourcesSet(domainResources)

		def domains = domainResources.resources

		domains.each { Domain domain ->
			assertMetaIsSet(domain)
			domain.owningOrganizationGuid == null
			domain.owningOrganizationUrl == null
			domain.isShared()
		}
	}
}