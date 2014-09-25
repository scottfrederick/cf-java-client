package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.domain.CloudInfo

class InfoSpec extends CloudFoundryClientSpecification {

	def "info is available"() {
		given:
		clientIsConnected()

		when:
		CloudInfo info = infoRepository.getCloudInfo()

		then:
		info != null
		info.version == "2"
		info.description != null
		info.name != null
		info.support != null
		info.authorizationEndpoint != null
		info.loggingEndpoint != null
		info.build != null
	}

}