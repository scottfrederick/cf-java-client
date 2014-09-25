package org.cloudfoundry.client.lib.domain;

public class Resource<T extends Entity> {
	private Metadata metadata;
	private T entity;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public String getName() {
		return entity.getName();
	}
}
