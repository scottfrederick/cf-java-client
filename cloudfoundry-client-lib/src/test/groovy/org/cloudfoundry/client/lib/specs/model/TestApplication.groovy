package org.cloudfoundry.client.lib.specs.model

import org.cloudfoundry.client.lib.CloudFoundryClient
import org.cloudfoundry.client.lib.CloudFoundryException
import org.cloudfoundry.client.lib.domain.Staging
import org.springframework.http.HttpStatus

class TestApplication {
	private static final String TEST_APP_DIR = "src/test/resources/apps";

	private CloudFoundryClient client

	private String name
	private int memory = 128
	private String path
	private String command
	private String buildpackUrl
	private List<String> uris = []
	private List<String> services = []

	TestApplication(CloudFoundryClient client) {
		this.client = client
	}

	TestApplication isCreated() {
		try {
			client.getApplicationInstances(name)
			// application exists, don't re-create it
		} catch (CloudFoundryException cfe) {
			if (cfe.statusCode == HttpStatus.NOT_FOUND) {
				Staging staging = new Staging(command, buildpackUrl)
				client.createApplication(name, staging, memory, uris, services);
			} else {
				throw cfe
			}
		}
		this
	}

	TestApplication isUploaded() {
		File file = new File(TEST_APP_DIR + File.separator + path);
		if (!file.exists()) {
			throw new RuntimeException("Application not found at ${file.absolutePath}")
		}
		client.uploadApplication(name, file);
		this
	}

	TestApplication isStarted() {
		client.startApplication(name);
		this
	}
}
