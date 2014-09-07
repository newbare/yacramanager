package fr.wati.yacramanager.dao;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class CustomNamingStrategy extends ImprovedNamingStrategy {

	private static final long serialVersionUID = 1L;
	private static final String PREFIX = "YACRA_";

	@Override
	public String classToTableName(final String className) {
		return this.addPrefix(super.classToTableName(className));
	}

	/* (non-Javadoc)
	 * @see org.hibernate.cfg.ImprovedNamingStrategy#columnName(java.lang.String)
	 */
	@Override
	public String columnName(String columnName) {
		return this.addPrefix(columnName);
	}

	@Override
	public String collectionTableName(final String ownerEntity,
			final String ownerEntityTable, final String associatedEntity,
			final String associatedEntityTable, final String propertyName) {
		return this.addPrefix(super.collectionTableName(ownerEntity,
				ownerEntityTable, associatedEntity, associatedEntityTable,
				propertyName));
	}

	@Override
	public String logicalCollectionTableName(final String tableName,
			final String ownerEntityTable, final String associatedEntityTable,
			final String propertyName) {
		return this.addPrefix(super.logicalCollectionTableName(tableName,
				ownerEntityTable, associatedEntityTable, propertyName));
	}

	private String addPrefix(final String composedTableName) {
		return PREFIX + composedTableName.toUpperCase();
		// return PREFIX
		// + composedTableName.toUpperCase().replace("_", "");

	}

}