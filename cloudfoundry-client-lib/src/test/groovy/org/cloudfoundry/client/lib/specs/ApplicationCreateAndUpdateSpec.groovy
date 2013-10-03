package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.domain.CloudApplication
import org.cloudfoundry.client.lib.domain.Staging
import spock.lang.Ignore
import spock.lang.Stepwise

import static org.cloudfoundry.client.lib.domain.CloudApplication.AppState.STOPPED

@Stepwise // run tests in the order they are defined
class ApplicationCreateAndUpdateSpec extends CloudFoundryClientSpecification {
	def "application is created with defaults"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "app-with-defaults"
		}.isCreated()

		when:
		CloudApplication app = client.getApplication("app-with-defaults")

		then:
		assertMetaIsSet(app.meta)

		app.name == "app-with-defaults"
		app.instances == 1
		app.runningInstances == 0
		app.state == STOPPED
		app.staging.command == null
		app.staging.buildpackUrl == null
		app.uris.isEmpty()
		app.services.isEmpty()
		app.env.isEmpty()
		app.envAsMap.isEmpty()
	}

	def "application instances are updated"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "app-with-defaults"
		}.isCreated()

		and:
		client.updateApplicationInstances("app-with-defaults", 3)

		when:
		CloudApplication app = client.getApplication("app-with-defaults")

		then:
		app.meta.updated != null

		app.instances == 3
	}

	def "application memory is updated"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "app-with-defaults"
		}.isCreated()

		and:
		client.updateApplicationMemory("app-with-defaults", 512)

		when:
		CloudApplication app = client.getApplication("app-with-defaults")

		then:
		app.memory == 512
	}

	def "application env vars are updated as list"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "app-with-defaults"
		}.isCreated()

		and:
		client.updateApplicationEnv("app-with-defaults", ["test1=value1", "test2=value2", "test3=value3"])

		when:
		CloudApplication app = client.getApplication("app-with-defaults")

		then:
		assertEnvVarsContainExpected(app)
	}

	def "application env vars are updated as map"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "app-with-defaults"
		}.isCreated()

		and:
		client.updateApplicationEnv("app-with-defaults", ["test1":"value1", "test2":"value2", "test3":"value3"])

		when:
		CloudApplication app = client.getApplication("app-with-defaults")

		then:
		assertEnvVarsContainExpected(app)
	}

	private void assertEnvVarsContainExpected(CloudApplication app) {
		assert app.env.contains("test1=value1")
		assert app.env.contains("test2=value2")
		assert app.env.contains("test3=value3")
		assert app.envAsMap.get("test1") == "value1"
		assert app.envAsMap.get("test2") == "value2"
		assert app.envAsMap.get("test3") == "value3"
	}

	def "application is created with values"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "app-with-values"
			memory = 512
			uris = [ "uri1-${namespace}.${defaultDomainName}".toString(), "uri2-${namespace}.${defaultDomainName}".toString() ]
			command = "custom command"
			buildpackUrl = "http://git.example.com/custom-buildpack"
		}.isCreated()

		when:
		CloudApplication app = client.getApplication("app-with-values")

		then:
		assertMetaIsSet(app.meta)

		app.name == "app-with-values"
		app.memory == 512
		app.uris.size() == 2
		app.uris[0] == "uri1-${namespace}.${defaultDomainName}"
		app.uris[1] == "uri2-${namespace}.${defaultDomainName}"
		app.staging.command == "custom command"
		app.staging.buildpackUrl == "http://git.example.com/custom-buildpack"
		app.services.isEmpty()
		app.env.isEmpty()
		app.envAsMap.isEmpty()
	}

	def "application URIs are updated"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "app-with-values"
		}.isCreated()

		and:
		client.updateApplicationUris("app-with-values", [ "uri3-${namespace}.${defaultDomainName}".toString() ])

		when:
		CloudApplication app = client.getApplication("app-with-values")

		then:
		app.name == "app-with-values"
		app.uris.size() == 1
		app.uris[0] == "uri3-${namespace}.${defaultDomainName}"
	}

	def "application command is updated"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "app-with-values"
		}.isCreated()

		and:
		client.updateApplicationStaging("app-with-values", new Staging("new command", null))

		when:
		CloudApplication app = client.getApplication("app-with-values")

		then:
		app.name == "app-with-values"
		app.staging.command == "new command"
	}

	def "application buildpack is updated"() {
		given:
		clientIsConnected()

		and:
		application() {
			name = "app-with-values"
		}.isCreated()

		and:
		client.updateApplicationStaging("app-with-values", new Staging(null, "http://git.example.com/new-buildpack"))

		when:
		CloudApplication app = client.getApplication("app-with-values")

		then:
		app.name == "app-with-values"
		app.staging.buildpackUrl == "http://git.example.com/new-buildpack"
	}
}