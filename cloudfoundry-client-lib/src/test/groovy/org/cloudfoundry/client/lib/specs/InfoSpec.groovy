package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.domain.CloudDomain
import org.cloudfoundry.client.lib.domain.CloudInfo
import org.cloudfoundry.client.lib.domain.CloudOrganization
import org.cloudfoundry.client.lib.domain.CloudSpace

class InfoSpec extends CloudFoundryClientSpecification {

	def "info is available"() {
		given:
		clientIsConnected()

		when:
		CloudInfo info = client.getCloudInfo()

		then:
		info != null
		info.user == config.userEmail
		info.version == "2"
		info.description != null
		info.name != null
		info.support != null
		info.authorizationEndpoint != null
		// todo: fix parsing of build
		// info.build > 0
	}

	def "limits are available"() {
		given:
		clientIsConnected()

		when:
		CloudInfo info = client.getCloudInfo()

		then:
		info.limits != null
		info.limits.maxApps > 0
		info.limits.maxServices > 0
		info.limits.maxTotalMemory > 0
		info.limits.maxUrisPerApp > 0
	}

	def "usage is available"() {
		given:
		clientIsConnected()

		when:
		CloudInfo info = client.getCloudInfo()

		then:
		info.usage != null
		info.usage.apps >= 0
		info.usage.totalMemory >= 0
		info.usage.urisPerApp >= 0
		info.usage.services >= 0
	}

	def "spaces are available"() {
		given:
		clientIsConnected()

		when:
		List<CloudSpace> spaces = client.getSpaces()

		then:
		spaces != null
		spaces.size() > 0
		spaces.collect { it.name }.contains(config.space)
		spaces.each { space ->
			assertMetaIsSet(space.meta)
			space.organization != null
		}
	}

	def "organizations are available"() {
		given:
		clientIsConnected()

		when:
		List<CloudOrganization> orgs = client.getOrganizations()

		then:
		orgs != null
		orgs.size() > 0
		orgs.collect { it.name }.contains(config.organization)
		orgs.each { org ->
			assertMetaIsSet(org.meta)
		}
	}

	def "domains are available"() {
		given:
		clientIsConnected()

		when:
		List<CloudDomain> domains = client.getDomains()

		then:
		domains != null
		domains.size() > 0
		domains.each { domain ->
			assertMetaIsSet(domain.meta)
			domain.owner != null
		}
	}
}