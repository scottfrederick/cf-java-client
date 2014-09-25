package org.cloudfoundry.client.lib.domain;

import java.util.UUID;

public class Space extends Resource<Space.SpaceEntity> {

	public Space() {
	}

	public Organization getOrganization() {
		return getEntity().getOrganization();
	}

	public UUID getOrganizationGuid() {
		return getEntity().getOrganizationGuid();
	}

	public UUID getSpaceQuotaDefinitionGuid() {
		return getEntity().getSpaceQuotaDefinitionGuid();
	}

	class SpaceEntity extends Entity {
		private UUID organizationGuid;
		private UUID spaceQuotaDefinitionGuid;

		private Organization organization;

		public SpaceEntity() {
		}

		public UUID getOrganizationGuid() {
			return organizationGuid;
		}

		public UUID getSpaceQuotaDefinitionGuid() {
			return spaceQuotaDefinitionGuid;
		}

		public Organization getOrganization() {
			return organization;
		}
	}
}



