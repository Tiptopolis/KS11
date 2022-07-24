package com.uchump.prime.Core.Data.Util;

import static com.uchump.prime.Core.uAppUtils.*;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.uchump.prime.Core.Primitive.Struct.aDictionary;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aDictionary;

public abstract class _SQL {
	public static _SQL CORE;
	public static aDictionary<Object> TYPES = new aDictionary<Object>();

	static {
		if (TYPES == null)
			TYPES = new aDictionary<Object>();
		int s = TYPES.size();
		String SQL = _SQL.class.toString();
		String C = (SQL + "$");
		String T = (SQL + "#");
		String S = (SQL + ".");

		TYPES.put(SQL, ".", "CLAUSE", "DATA_TYPE");

		TYPES.put(C, "CLAUSE", "WHERE", "HAVING", "FROM", "GROUP BY", "ORDER BY", "USING", "WHERE CURRENT OF");


		TYPES.put(C, "DATA_TYPE", "BIGINT", "BLOB", "BIGINT", "BIGINT", "BOOLEAN", "CHAR", "CLOB", "DATE", "DECIMAL",
				"DOUBLE", "FLOAT", "INTEGER", "NUMERIC", "NULL", "OBJECT", "REAL", "SMALLINT", "STRING", "TEXT", "TIME",
				"TIMESTAMP", "VARCHAR", "STRUCT", "ARRAY");

		TYPES.put(T, "DATA_TYPE.NUMERIC", "NUMERIC", "SMALLINT", "INTEGER", "BIGINT", "REAL", "DECIMAL", "FLOAT",
				"DOUBLE", "TIME", "TIMESTAMP", "DATE");
		TYPES.put(T, "DATA_TYPE.TEXT", "TEXT", "VARCHAR", "STRING", "CHAR", "CLOB");
		TYPES.put(T, "DATA_TYPE.STRUCT", "STRUCT", "BLOB", "OBJECT", "ARRAY");
		
		TYPES.put(T, "DATA_TYPE.NULL", "NULL", "ASS");
		
		//Log(1/0);
	}

	private _SQL() {
		CORE = this;
	}

	public static _SQL init() {
		_SQL x = new _SQL() {

		};
		return CORE;
	}

	private static boolean tableExists(Connection connection, String tableName) throws SQLException {
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet resultSet = meta.getTables(null, null, tableName, new String[] { "TABLE" });

		return resultSet.next();
	}

	public static ResultSet getTableEntries(Connection connection, String tableName) {

		try {
			Statement s = connection.createStatement();
			ResultSet result;
			result = s.executeQuery("SELECT * FROM " + tableName);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ResultSet newTable(Connection connection, String tableName) {

		// Log("_Filling[Accounts]");
		try {
			if (tableExists(connection, tableName)) {
				String sql = "DROP TABLE IF EXISTS " + tableName + ";";
				connection.createStatement().executeUpdate(sql);
			}
			if (!tableExists(connection, tableName)) {

				String sql = "CREATE TABLE " + tableName + "()"; //

				connection.createStatement().executeUpdate(sql);
				// Log(".creating ACCOUNTS Table");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static aList<String> getColumnNames(Connection connection, String ofTable) {
		ResultSet rs = getTableEntries(connection, ofTable);
		aList<String> columns = new aList<String>();
		try {

			ResultSetMetaData rsMetaData = rs.getMetaData();
			int count = rsMetaData.getColumnCount();
			for (int i = 1; i <= count; i++) {
				String s = rsMetaData.getColumnName(i);
				columns.append(s);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return columns;
	}

}
