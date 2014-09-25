package org.cloudfoundry.client.lib.repository;

import feign.RequestLine;
import org.cloudfoundry.client.lib.domain.PagedResource;
import org.cloudfoundry.client.lib.domain.Space;

public interface SpaceRepository {
	@RequestLine("GET /v2/spaces?inline-relations-depth=1")
	public PagedResource<Space> getAll();
}
