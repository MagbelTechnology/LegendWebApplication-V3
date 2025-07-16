package com.magbel.admin.objects;

public class TableColumn {
private String tableName;
private String columnName;
public TableColumn(String tableName, String columnName) {
	super();
	this.tableName = tableName;
	this.columnName = columnName;
}

public TableColumn() {
	super();
}

public String getTableName() {
	return tableName;
}
public void setTableName(String tableName) {
	this.tableName = tableName;
}
public String getColumnName() {
	return columnName;
}
public void setColumnName(String columnName) {
	this.columnName = columnName;
}


}
