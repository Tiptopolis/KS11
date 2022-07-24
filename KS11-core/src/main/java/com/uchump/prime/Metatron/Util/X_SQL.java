package com.uchump.prime.Metatron.Util;

import java.lang.reflect.Constructor;

//import static com.Rev.Core.AppUtils.Log;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iConstructor;
import com.uchump.prime.Core.Primitive.A_I.iEnum;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.A_I.iPrimitive;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class X_SQL {

	public static aMultiMap<String, Class> SQL_JAVA = new aMultiMap<String, Class>();
	public static aMap<String, Supplier> SQL_TYPES = new aMap<String, Supplier>();
	public static aList<Texture> UNMANAGED_IMAGES = new aList<Texture>();
	
	static {

		SQL_JAVA.put("SMALLINT", Short.class);
		SQL_JAVA.put("INTEGER", Integer.class);
		SQL_JAVA.put("BIGINT", Long.class);
		SQL_JAVA.put("DECFLOAT(n)", BigDecimal.class);
		SQL_JAVA.put("REAL", Float.class);
		SQL_JAVA.put("DOUBLE", Double.class);

		SQL_JAVA.put("CHAR(n)", String.class);
		SQL_JAVA.put("VARCHAR(n)", String.class);

		SQL_JAVA.put("CHAR(n) FOR BIT DATA", byte[].class);
		SQL_JAVA.put("VARCHAR(n) FOR BIT DATA", byte[].class);
		SQL_JAVA.put("BINARY(n)", byte[].class);

		SQL_JAVA.put("GRAPHIC(m)", String.class);
		SQL_JAVA.put("VARGRAPHIC(m)", String.class);

		SQL_JAVA.put("CLOB(n)", java.sql.Clob.class);
		SQL_JAVA.put("BLOB(n)", java.sql.Blob.class);
		SQL_JAVA.put("DBCLOB(m)", java.sql.Clob.class);

		SQL_JAVA.put("SMALROWIDLINT", java.sql.RowId.class);
		SQL_JAVA.put("XML", java.sql.SQLXML.class);
		SQL_JAVA.put("DATE", java.sql.Date.class);
		SQL_JAVA.put("TIME", java.sql.RowId.class);
		SQL_JAVA.put("TIMESTAMP", java.sql.Timestamp.class);
		SQL_JAVA.put(" TIMESTAMP(p)", java.sql.Timestamp.class);
		SQL_JAVA.put("TIMESTAMP WITH TIME ZONE", java.sql.Timestamp.class);
		SQL_JAVA.put("TIMESTAMP(p) WITH TIME ZONE", java.sql.Timestamp.class);
	}

	public static enum Type implements iEnum<Type>, iPrimitive {
		/*
		 * SMALLINT(() -> { return (short) 0; }), INTEGER(() -> { return 0; }),
		 * BIGINT(() -> { return (long) 0; }),DECFLOAT(() -> { return (float) 0; });
		 */

		SMALLINT(Short.class, _Predicates.instanceOf(Short.class, Integer.class, Long.class), (() -> {
			return (short) 0;
		}), "EXACT", "NUMERIC"), //
		INTEGER(Integer.class, _Predicates.instanceOf(Integer.class, Long.class, Short.class), (() -> {
			return 0;
		}), "EXACT", "NUMERIC"), //
		BIGINT(Long.class,
				_Predicates.instanceOf(Long.class, Short.class, Integer.class, long.class, short.class, Integer.class),
				(() -> {
					return (long) 0;
				}), "EXACT", "NUMERIC"),
		DECFLOAT(Float.class, _Predicates.instanceOf(Float.class, Double.class, double.class, float.class), (() -> {
			return (float) 0;
		}), "APROX", "NUMERIC"),
		DOUBLE(Double.class, _Predicates.instanceOf(Double.class, Float.class, double.class, float.class), (() -> {
			return (double) 0;
		}), "APROX", "NUMERIC"),
		CHAR(Character.class, _Predicates.instanceOf(String.class, CharSequence.class, char.class), (() -> {
			return ' ';
		}), "CHARACTER", "STRING"), VARCHAR(String.class,
				_Predicates.instanceOf(String.class, CharSequence.class, Character[].class, char[].class), (() -> {
					return " ";
				}), "CHARACTER", "STRING", "TEXT"),
		BINARY(byte[].class, _Predicates.instanceOf(String.class, CharSequence.class, Character[].class, char[].class,
				Integer[].class, int[].class), (() -> {
					return new byte[0];
				}), "BINARY", "STRING"),
		GRAPHIC(String.class, _Predicates.instanceOf(String.class, CharSequence.class, Character[].class, char[].class,
				Integer[].class, int[].class, Pixmap.class, Texture.class, TextureRegion.class), (() -> {
					return new Texture(new Pixmap(64, 64, Format.RGBA8888));
				}), "GRAPHIC", "BINARY", "STRING")

		;

		// NAME, DefaultJavaClass, Compatible, DefaultValue, Tags

		public Class Primitive;
		public Predicate Includes;
		public Supplier GetNew;
		public static aMultiMap<Type, String> Tags;

		private Type(Class is, Predicate inc, Supplier New, String... tags) {

			this.Primitive = is;
			this.Includes = inc;
			this.GetNew = New;
			this.reg(this, tags);
		}

		public static void reg(Type y, String... tags) {
			if (X_SQL.SQL_TYPES == null)
				X_SQL.SQL_TYPES = new aMap<String, Supplier>();

			if (Tags == null)
				Tags = new aMultiMap<Type, String>();

			for (String s : tags)
				Tags.put(y, s);

			iEnum._ALL.ENUMS.put("SQL.TYPE", Tags);
			SQL_TYPES.put(y.name(), y.GetNew);
		}

		@Override
		public aMultiMap<Type, String> getTags() {
			return Tags;
		}

		@Override
		public Object createNew() {

			return this.GetNew.get();
		}

		@Override
		public Type getNew(Entry<String, Object>... args) {

			return null;
		}

		@Override
		public Type get(Integer index) {

			index = this.resolveIndex(index);
			return this.values()[index];
		}

		@Override
		public Integer indexOf(Object member) {
			aSet S = new aSet(this.values());

			return S.indexOf(member);
		}

		@Override
		public int size() {

			return this.values().length;
		}

		@Override
		public Type[] toArray() {
			return this.values();
		}

		@Override
		public void dispose() {
			if (!Tags.isEmpty())
				Tags.clear();
			Tags = null;
			SQL_TYPES.clear();
			SQL_JAVA.clear();
			for(Texture T : UNMANAGED_IMAGES)
				T.dispose();
			UNMANAGED_IMAGES.clear();
		}

		@Override
		public <N, X> iMap<N, X> toMap() {
			// TODO Auto-generated method stub
			return null;
		}

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

				String sql = "CREATE TABLE " + tableName + "(" //
						+ "account_ID INT NOT NULL AUTO_INCREMENT," //
						+ "type VARCHAR(16),"//
						+ "balance DECIMAL (10, 2)," //
						+ "owner_ID INT NOT NULL," //
						+ "account_num INT NOT NULL,"//
						+ "CONSTRAINT accounts_pk PRIMARY KEY (account_ID)" //
						+ ")";//

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
