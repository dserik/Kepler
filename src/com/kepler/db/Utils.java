package com.kepler.db;

import com.kepler.db.dialects.Dialect;
import com.kepler.db.dialects.Postgresql;
import com.kepler.db.dialects.Unknown;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;

/**
 * @author     Daryn Serikbayev <daryn.serikbayev@gmail.com>
 */

@SuppressWarnings("SqlDialectInspection")
public class Utils {

    static Dialect getDialect(Connection conn) {
        try{
            String dbName = conn.getMetaData().getDatabaseProductName();
            switch (dbName) {
                case "PostgreSQL" : return new Postgresql();
                default: return new Unknown();
            }
        } catch (SQLException e) {
            return new Unknown();
        }
    }

//    static HashMap<Integer, String> getTypeMap(Connection conn) {
//
//        try{
//            String dbName = conn.getMetaData().getDatabaseProductName();
//            Dialect dialect;
//            switch (dbName) {
//                case "PostgreSQL" : dialect = new Postgresql();
//                    break;
//                default: dialect = new Unknown();
//            }
//
//            return dialect.getTypeMap(conn);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return new HashMap<>();

//        HashMap<Integer, ArrayList<String>> similarities = new HashMap<>();
//        try (ResultSet rs = conn.getMetaData().getTypeInfo()) {
//            StringBuilder tableSql = new StringBuilder("CREATE TABLE SYSTEM_DBTYPES_TABLE (\n");
//            boolean idExists = false;
//            while (rs.next()) {
//                int sqlType = rs.getInt("DATA_TYPE");
//                if(javaClassToSqlType.values().stream().filter(i -> {
//                    for (Integer integer : i)
//                        if (integer == sqlType)
//                            return true;
//                    return false;
//                }).count() == 0)
//                    continue;
//
//                String typeName = rs.getString("TYPE_NAME");
//                if(!idExists && sqlType == Types.INTEGER) {
//                    tableSql.append("SYS_ID_FIELD ").append(typeName).append(", ");
//                    idExists = true;
//                }
//
//                ArrayList<String> jdbcTypes = similarities.get(sqlType);
//                if (jdbcTypes == null) {
//                    jdbcTypes = new ArrayList<>();
//                    jdbcTypes.add(typeName);
//                    similarities.put(sqlType, jdbcTypes);
//                } else {
//                    jdbcTypes.add(typeName);
//                }
//
//                tableSql.append(removeSpaces(typeName)).append("_FIELD ").append(typeName).append(", ");
//            }
//
//            tableSql.deleteCharAt(tableSql.length() - 2).append(");");
//            try(Statement stmt = conn.createStatement()) {
//                stmt.executeUpdate(tableSql.toString());
//
//                stmt.executeUpdate("INSERT INTO SYSTEM_DBTYPES_TABLE(SYS_ID_FIELD) VALUES (1)");
//
//                for (Integer sqlType : javaClassToSqlType.get(String.class)) {
//                    String similarType = getSimilarity(sqlType, "123,ABC-abc, ÀÁÂ-àáâ", (ps, val) -> ps.setString(1, (String) val),similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(boolean.class)) {
//                    String similarType = getSimilarity(sqlType, false, (ps, val) -> ps.setBoolean(1, (boolean) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(byte.class)) {
//                    String similarType = getSimilarity(sqlType, Byte.MIN_VALUE, (ps, val) -> ps.setByte(1, (byte) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(short.class)) {
//                    String similarType = getSimilarity(sqlType, Short.MIN_VALUE, (ps, val) -> ps.setShort(1, (short) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(int.class)) {
//                    String similarType = getSimilarity(sqlType, Integer.MIN_VALUE, (ps, val) -> ps.setInt(1, (int) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(long.class)) {
//                    String similarType = getSimilarity(sqlType, Long.MIN_VALUE, (ps, val) -> ps.setLong(1, (long) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(float.class)) {
//                    String similarType = getSimilarity(sqlType, Float.MIN_VALUE, (ps, val) -> ps.setFloat(1, (float) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(double.class)) {
//                    String similarType = getSimilarity(sqlType, Double.MIN_VALUE, (ps, val) -> ps.setDouble(1, (double) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(Date.class)) {
//                    Date testDateVal = new Date(new java.util.Date().getTime());
//                    String similarType = getSimilarity(sqlType, testDateVal, (ps, val) -> ps.setDate(1, (Date) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(Time.class)) {
//                    String similarType = getSimilarity(sqlType, new Time(new java.util.Date().getTime()), (ps, val) -> ps.setTime(1, (Time) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                for(Integer sqlType : javaClassToSqlType.get(Timestamp.class)) {
//                    String similarType = getSimilarity(sqlType, new Timestamp(new java.util.Date().getTime()), (ps, val) -> ps.setTimestamp(1, (Timestamp) val), similarities, conn);
//                    if(similarType != null)
//                        map.put(sqlType, similarType);
//                }
//
//                stmt.executeUpdate("DROP TABLE SYSTEM_DBTYPES_TABLE CASCADE;");
//            }
//
//        } catch (SQLException ignored) {
//            ignored.printStackTrace();
//        }
//    }

//    private static String getSimilarity(int sqlType, Object testVal, Action<PreparedStatement, Object> setterAction, HashMap<Integer, ArrayList<String>> similarities, Connection conn) {
//
//        ArrayList<String> dbTypes = similarities.get(sqlType);
//        if (dbTypes == null) return null;
//
//        for (String dbType : dbTypes) {
//            String columnName = removeSpaces(dbType) + "_FIELD";
//            try(PreparedStatement ps = conn.prepareStatement(
//                    "UPDATE SYSTEM_DBTYPES_TABLE SET " + columnName + " = ? WHERE SYS_ID_FIELD = 1;")){
//                setterAction.accept(ps, testVal);
//                ps.execute();
//                try(Statement stmt = conn.createStatement();
//                        ResultSet rsSelect = stmt.executeQuery("SELECT " + columnName + " FROM SYSTEM_DBTYPES_TABLE WHERE SYS_ID_FIELD = 1;")) {
//                    if (rsSelect.next() && rsSelect.getObject(columnName)/*String(columnName)*/.equals(testVal)){
//                        return dbType;
//                    }
//                }
//
//            } catch (Exception ignored){
////                ignored.printStackTrace();
//            }
//        }
//
//        return null;
//    }

//    private static String removeSpaces(String value) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(value.charAt(0));
//        for (int i = 1; i < value.length(); i++) {
//            if(" ".equals(value.substring(i, i + 1))){
//                sb.append("_");
//                continue;
//            }
//            if(value.substring(i, i + 1).equals(value.substring(i, i + 1).toUpperCase()))
//                sb.append("_");
//            sb.append(value.charAt(i));
//        }
//
//        return sb.toString().toUpperCase();
//    }

    public static String replaceNull(String value) {
        return value != null ? value.trim() : "";
    }

    public static String generateDbFieldName(String value) {
        if (value == null || value.trim().length() == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        sb.append(value.charAt(0));
        for (int i = 1; i < value.length(); i++) {
            if(value.substring(i, i + 1).equals(value.substring(i, i + 1).toUpperCase()))
                sb.append("_");
            sb.append(value.charAt(i));
        }

        return sb.toString().toUpperCase();
    }

    static HashMap<Class, Integer[]> javaClassToSqlType = new HashMap<>();
    static {
        javaClassToSqlType.put(String.class, new Integer[]{Types.VARCHAR});
        javaClassToSqlType.put(BigDecimal.class, new Integer[]{ Types.DECIMAL});
        javaClassToSqlType.put(Boolean.class, new Integer[]{ Types.BOOLEAN, Types.BIT});
        javaClassToSqlType.put(boolean.class, new Integer[]{ Types.BOOLEAN,Types.BIT});
        javaClassToSqlType.put(Byte.class, new Integer[]{ Types.TINYINT});
        javaClassToSqlType.put(byte.class, new Integer[]{ Types.TINYINT});
        javaClassToSqlType.put(Short.class, new Integer[]{ Types.SMALLINT});
        javaClassToSqlType.put(short.class, new Integer[]{ Types.SMALLINT});
        javaClassToSqlType.put(Integer.class, new Integer[]{ Types.INTEGER});
        javaClassToSqlType.put(int.class, new Integer[]{ Types.INTEGER});
        javaClassToSqlType.put(Long.class, new Integer[]{ Types.BIGINT});
        javaClassToSqlType.put(long.class, new Integer[]{ Types.BIGINT});
        javaClassToSqlType.put(Float.class, new Integer[]{ Types.REAL});
        javaClassToSqlType.put(float.class, new Integer[]{ Types.REAL});
        javaClassToSqlType.put(Double.class, new Integer[]{ Types.DOUBLE});
        javaClassToSqlType.put(double.class, new Integer[]{ Types.DOUBLE});
        javaClassToSqlType.put(java.util.Date.class, new Integer[]{ Types.DATE});
        javaClassToSqlType.put(Date.class, new Integer[]{ Types.DATE});
        javaClassToSqlType.put(Time.class, new Integer[]{ Types.TIME});
        javaClassToSqlType.put(Timestamp.class, new Integer[]{ Types.TIMESTAMP});
//        javaClassToSqlType.put(Clob.class, Types.CLOB);
//        javaClassToSqlType.put(Blob.class, Types.BLOB);
//        javaClassToSqlType.put(Array.class, Types.ARRAY);
//        javaClassToSqlType.put(Struct.class, Types.STRUCT);
//        javaClassToSqlType.put(Ref.class, Types.REF);
    }

    static HashMap<Integer, Class> sqlTypeToJavaClass = new HashMap<>();
    static {
        sqlTypeToJavaClass.put(Types.CHAR, String.class);
        sqlTypeToJavaClass.put(Types.VARCHAR, String.class);
        sqlTypeToJavaClass.put(Types.LONGVARCHAR, String.class);
        sqlTypeToJavaClass.put(Types.NUMERIC, java.math.BigDecimal.class);
        sqlTypeToJavaClass.put(Types.DECIMAL, java.math.BigDecimal.class);
        sqlTypeToJavaClass.put(Types.BIT, Boolean.class);
        sqlTypeToJavaClass.put(Types.TINYINT, Byte.class);
        sqlTypeToJavaClass.put(Types.SMALLINT, Short.class);
        sqlTypeToJavaClass.put(Types.INTEGER, Integer.class);
        sqlTypeToJavaClass.put(Types.BIGINT, Long.class);
        sqlTypeToJavaClass.put(Types.REAL, Float.class);
        sqlTypeToJavaClass.put(Types.FLOAT, Double.class);
        sqlTypeToJavaClass.put(Types.DOUBLE, Double.class);
        sqlTypeToJavaClass.put(Types.BINARY, Byte[].class);
        sqlTypeToJavaClass.put(Types.VARBINARY, Byte[].class);
        sqlTypeToJavaClass.put(Types.DATE, java.sql.Date.class);
        sqlTypeToJavaClass.put(Types.TIME, java.sql.Time.class);
        sqlTypeToJavaClass.put(Types.TIMESTAMP, java.sql.Timestamp.class);
        sqlTypeToJavaClass.put(Types.CLOB, Clob.class);
        sqlTypeToJavaClass.put(Types.BLOB, Blob.class);
        sqlTypeToJavaClass.put(Types.ARRAY, Array.class);
        sqlTypeToJavaClass.put(Types.DISTINCT, String.class);
        sqlTypeToJavaClass.put(Types.STRUCT, Struct.class);
        sqlTypeToJavaClass.put(Types.REF, Ref.class);
        sqlTypeToJavaClass.put(Types.JAVA_OBJECT, String.class);
    }



    static void setPstmtValue(PreparedStatement pstmt, JDBCType type, Object value, int index) throws SQLException {
        switch (type.getVendorTypeNumber()) {
            case Types.CHAR: pstmt.setString(index, (String)value); break;
            case Types.VARCHAR: pstmt.setString(index, (String)value); break;
            case Types.LONGVARCHAR: pstmt.setString(index, (String)value); break;
            case Types.NUMERIC: pstmt.setBigDecimal(index, (BigDecimal)value); break;
            case Types.DECIMAL: pstmt.setBigDecimal(index, (BigDecimal)value); break;
            case Types.BIT: pstmt.setBoolean(index, (Boolean)value); break;
            case Types.TINYINT: pstmt.setByte(index, (Byte)value); break;
            case Types.SMALLINT: pstmt.setShort(index, (Short)value); break;
            case Types.INTEGER: pstmt.setInt(index, (Integer) value); break;
            case Types.BIGINT: pstmt.setLong(index, (Long)value); break;
            case Types.REAL: pstmt.setFloat(index, (Float)value); break;
            case Types.FLOAT: pstmt.setDouble(index, (Double)value); break;
            case Types.DOUBLE: pstmt.setDouble(index, (Double)value); break;
            case Types.BINARY: pstmt.setByte(index, (Byte)value); break;
            case Types.VARBINARY: pstmt.setByte(index, (Byte)value); break;
            case Types.LONGVARBINARY: pstmt.setByte(index, (Byte)value); break;
            case Types.DATE: pstmt.setDate(index, (Date) value); break;
            case Types.TIME: pstmt.setTime(index, (Time) value); break;
            case Types.TIMESTAMP: pstmt.setTimestamp(index, (Timestamp) value); break;
            case Types.CLOB: pstmt.setClob(index, (Clob)value); break;
            case Types.BLOB: pstmt.setBlob(index, (Blob)value); break;
            case Types.ARRAY: pstmt.setArray(index, (Array)value); break;
            case Types.DISTINCT: pstmt.setString(index, (String)value); break;
            case Types.STRUCT: pstmt.setString(index, (String) value); break;
            case Types.REF: pstmt.setRef(index, (Ref)value); break;
            case Types.JAVA_OBJECT: pstmt.setString(index, (String)value); break;
                default: throw new SQLException("Unknown type");
        }
    }

    static String getJavaType(JDBCType type){
        Class result = sqlTypeToJavaClass.get(type.getVendorTypeNumber());
        return result != null ? result.getCanonicalName() : "Object";
    }

    static String javaClassSyntax(String value) {
        String result = javaFieldSyntax(value);
        if (result.length() == 0)
            return "";

        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }

    static String javaConstantSyntax(String value) {
        return value.toUpperCase();
    }

    static String javaFieldSyntax(String value) {
        if (value == null || value.trim().length() == 0)
            return "";

        StringBuilder result = new StringBuilder(value.substring(0, 1).toLowerCase()) ;

        for (int i = 1; i < value.length(); i++) {
            if (value.charAt(i) != '_') {
                result.append(value.charAt(i));
            } else {
                if(++i < value.length() && value.charAt(i) != '_')
                    result.append(value.substring(i, i + 1).toUpperCase());
            }
        }

        return result.toString();
    }

}
