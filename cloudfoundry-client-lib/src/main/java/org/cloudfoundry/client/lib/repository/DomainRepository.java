package org.cloudfoundry.client.lib.repository;

import feign.RequestLine;
import org.cloudfoundry.client.lib.domain.Domain;
import org.cloudfoundry.client.lib.domain.Organization;
import org.cloudfoundry.client.lib.domain.PagedResource;

import javax.inject.Named;

public interface DomainRepository {
	@RequestLine("GET /v2/shared_domains")
	public PagedResource<Domain> getSharedDomains();

	@RequestLine("GET /v2/organizations/{org}/domains")
	public PagedResource<Domain> getDomainsForOrg(@Named("org") Organization org);
}
