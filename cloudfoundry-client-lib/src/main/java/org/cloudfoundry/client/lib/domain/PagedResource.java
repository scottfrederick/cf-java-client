package org.cloudfoundry.client.lib.domain;

import java.util.List;

public class PagedResource<T extends Resource> {
	private int totalResults;
	private int totalPages;
	private String prevUrl;
	private String nextUrl;
	private List<T> resources;

	public PagedResource() {
	}

	public int getTotalResults() {
		return totalResults;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public String getPrevUrl() {
		return prevUrl;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public List<T> getResources() {
		return resources;
	}
}
