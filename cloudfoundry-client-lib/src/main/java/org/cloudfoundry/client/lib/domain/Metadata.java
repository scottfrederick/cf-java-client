package org.cloudfoundry.client.lib.domain;

import java.util.Date;
import java.util.UUID;

public class Metadata {
	private UUID guid;
	private String url;
	private Date createdAt;
	private Date updatedAt;

	public Metadata() {
	}

	public Metadata(UUID guid, String url, Date created, Date updated) {
		this.guid = guid;
		this.url = url;
		this.createdAt = created;
		this.updatedAt = updated;
	}

	public UUID getGuid() {
		return guid;
	}

	public String getUrl() {
		return url;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}
}