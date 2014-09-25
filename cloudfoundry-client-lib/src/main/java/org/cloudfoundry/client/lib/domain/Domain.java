package org.cloudfoundry.client.lib.domain;

import java.util.UUID;

public class Domain extends Resource<Domain.DomainEntity> {
	public Domain() {
	}

	public UUID getOwningOrganizationGuid() {
		return getEntity().getOwningOrganizationGuid();
	}

	public String getOwningOrganizationUrl() {
		return getEntity().getOwningOrganizationUrl();
	}

	public boolean isShared() {
		return getOwningOrganizationGuid() == null;
	}

	class DomainEntity extends Entity {
		private UUID owningOrganizationGuid;
		private String owningOrganizationUrl;

		public DomainEntity() {
		}

		public UUID getOwningOrganizationGuid() {
			return owningOrganizationGuid;
		}

		public String getOwningOrganizationUrl() {
			return owningOrganizationUrl;
		}
	}
}



