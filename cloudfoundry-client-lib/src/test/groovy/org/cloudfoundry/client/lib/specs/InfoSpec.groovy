package org.cloudfoundry.client.lib.specs

import org.cloudfoundry.client.lib.CloudCredentials
import org.cloudfoundry.client.lib.CloudFoundryClient
import org.cloudfoundry.client.lib.domain.CloudDomain
import org.cloudfoundry.client.lib.domain.CloudEntity
import org.cloudfoundry.client.lib.domain.CloudInfo
import org.cloudfoundry.client.lib.domain.CloudOrganization
import org.cloudfoundry.client.lib.domain.CloudSpace
import spock.lang.*

import static org.junit.Assert.fail

class InfoSpec extends Specification {
    private static final String CF_API_URL = System.getProperty("cf.target", "http://api.run.pivotal.io");
    private static final String CF_API_PROXY_HOST = System.getProperty("http.proxyHost", null);
    private static final int CF_API_PROXY_PORT = Integer.getInteger("http.proxyPort", 80);
    private static final String CF_USER_EMAIL = System.getProperty("cf.email", "java-authenticatedClient-test-user@vmware.com");
    private static final String CF_USER_PASS = System.getProperty("cf.password");
    private static final String CF_USER_ORG = System.getProperty("cf.org", "gopivotal.com");
    private static final String CF_USER_SPACE = System.getProperty("cf.space", "test");
    private static final String TEST_NAMESPACE = System.getProperty("vcap.test.namespace", defaultNamespace(CF_USER_EMAIL));
    private static final String TEST_DOMAIN = System.getProperty("vcap.test.domain", defaultNamespace(CF_USER_EMAIL) + ".com");

    @Shared CloudFoundryClient client = null

    def setupSpec() {
        validateSetup CF_API_URL, "cf.target"
        validateSetup CF_USER_EMAIL, "cf.email"
        validateSetup CF_USER_PASS, "cf.password"
        validateSetup CF_USER_ORG, "cf.org"
        validateSetup CF_USER_SPACE, "cf.space"
    }

    def "info is available"() {
        given:
        clientIsConnected()

        when:
        CloudInfo info = client.getCloudInfo()

        then:
        info != null
        info.with {
            user == CF_USER_EMAIL
            version == "2"
            description != null
            name != null
            support != null
            authorizationEndpoint != null
            // todo: fix parsing of build
            // build > 0
        }
    }

    def "limits are available"() {
        given:
        clientIsConnected()

        when:
        CloudInfo info = client.getCloudInfo()

        then:
        info.limits != null
        info.limits.with {
            maxApps > 0
            maxServices > 0
            maxTotalMemory > 0
            maxUrisPerApp > 0
        }
    }

    def "usage is available"() {
        given:
        clientIsConnected()

        when:
        CloudInfo info = client.getCloudInfo()

        then:
        info.usage != null
        info.usage.with {
            apps >= 0
            totalMemory >= 0
            urisPerApp >= 0
            services >= 0
        }
    }

    def "spaces are available"() {
        given:
        clientIsConnected()

        when:
        List<CloudSpace> spaces = client.getSpaces()

        then:
        spaces != null
        spaces.size() > 0
        spaces.collect { it.name }.contains(CF_USER_SPACE)
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
        orgs.collect { it.name }.contains(CF_USER_ORG)
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

    def clientIsConnected() {
        if (client == null) {
            URL cloudControllerUrl = new URL(CF_API_URL)
            client = new CloudFoundryClient(new CloudCredentials(CF_USER_EMAIL, CF_USER_PASS),
                    cloudControllerUrl, CF_USER_ORG, CF_USER_SPACE)
            client.login()
        }
    }

    private void assertMetaIsSet(CloudEntity.Meta meta) {
        assert meta.guid != null
        assert meta.created != null
    }


    private static void validateSetup(String value, String systemProperty) {
        if (!value) {
            fail("System property ${systemProperty} must be specified, supply -D${systemProperty}=<value>");
        }
    }

    private static String defaultNamespace(String email) {
        return email.substring(0, email.indexOf('@')).replaceAll("\\.", "-").replaceAll("\\+", "-");
    }
}