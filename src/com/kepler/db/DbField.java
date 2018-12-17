package com.kepler.db;

import java.sql.JDBCType;

/**
 * @author     Daryn Serikbayev <daryn.serikbayev@gmail.com>
 */
public class DbField {

    public String name;
    public JDBCType jdbcType;
    public boolean isPK;
    public boolean isNullable;
    public boolean isAutincrement;
    public FKData foreignKeyData;
    public int length;

    public DbField(String name, JDBCType jdbcType, boolean isPK, boolean isNullable, boolean isAutincrement, int length, FKData foreignKeyData) {
        this.name = name;
        this.jdbcType = jdbcType;
        this.isPK = isPK;
        this.isNullable = isNullable;
        this.isAutincrement = isAutincrement;
        this.length = length;
        this.foreignKeyData = foreignKeyData;
    }

    public static class FKData {
        public String tableName;
        public String fieldName;

        public FKData(String tableName, String fieldName) {
            this.tableName = tableName;
            this.fieldName = fieldName;
        }
    }
}
