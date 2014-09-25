package org.cloudfoundry.client.lib.repository;

import feign.RequestLine;
import org.cloudfoundry.client.lib.domain.CloudInfo;

public interface InfoRepository {
	@RequestLine("GET /v2/info")
	public CloudInfo getCloudInfo();
}
