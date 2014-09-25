package org.cloudfoundry.client.lib.repository;

import feign.RequestLine;
import org.cloudfoundry.client.lib.domain.Organization;
import org.cloudfoundry.client.lib.domain.PagedResource;

public interface OrganizationRepository {
	@RequestLine("GET /v2/organizations?inline-relations-depth=0")
	public PagedResource<Organization> getAll();
}