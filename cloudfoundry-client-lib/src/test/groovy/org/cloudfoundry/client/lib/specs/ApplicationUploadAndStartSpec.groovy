package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.domain.CloudApplication
import spock.lang.Stepwise

import static org.cloudfoundry.client.lib.domain.CloudApplication.AppState.STARTED

@Stepwise // run tests in the order they are defined
class ApplicationUploadAndStartSpec extends CloudFoundryClientSpecification {
	def "application is created from directory and started"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "ruby-app"
			path = "ruby-app"
			uris = [ "${namespace}-ruby-app.${defaultDomainName}".toString() ]
		}.isCreated().isUploaded().isStarted()

		when:
		CloudApplication app = client.getApplication("ruby-app")

		then:
		assertMetaIsSet(app.meta)

		app.name == "ruby-app"
		app.instances == 1
		app.runningInstances == 0
		app.state == STARTED
	}
}