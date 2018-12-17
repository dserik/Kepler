package com.kepler.db.dialects;

import com.kepler.db.Action;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Unknown implements Dialect {

    static HashMap<Class, Integer[]> javaClassToSqlType = new HashMap<>();
    static {
        javaClassToSqlType.put(String.class, new Integer[]{Types.VARCHAR});
        javaClassToSqlType.put(BigDecimal.class, new Integer[]{ Types.DECIMAL});
        javaClassToSqlType.put(Boolean.class, new Integer[]{ Types.BOOLEAN, Types.BIT});
        javaClassToSqlType.put(boolean.class, new Integer[]{ Types.BOOLEAN, Types.BIT});
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

    @Override
    public HashMap<Integer, String> getTypeMap(Connection conn) {

        HashMap<Integer, String> map = new HashMap<>();

        HashMap<Integer, ArrayList<String>> similarities = new HashMap<>();
        try (ResultSet rs = conn.getMetaData().getTypeInfo()) {
            StringBuilder tableSql = new StringBuilder("CREATE TABLE SYSTEM_DBTYPES_TABLE (\n");
            boolean idExists = false;
            HashSet<Integer> supportedTypes = new HashSet<>();
            javaClassToSqlType.values().forEach(t -> Collections.addAll(supportedTypes, t));

            while (rs.next()) {
                int sqlType = rs.getInt("DATA_TYPE");
                if (!supportedTypes.contains(sqlType))
                    continue;

                String typeName = rs.getString("TYPE_NAME");
                if (!idExists && sqlType == Types.INTEGER) {
                    tableSql.append("SYS_ID_FIELD ").append(typeName).append(", ");
                    idExists = true;
                }

                ArrayList<String> jdbcTypes = similarities.get(sqlType);
                if (jdbcTypes == null) {
                    jdbcTypes = new ArrayList<>();
                    jdbcTypes.add(typeName);
                    similarities.put(sqlType, jdbcTypes);
                } else {
                    jdbcTypes.add(typeName);
                }

                tableSql.append(removeSpaces(typeName)).append("_FIELD ").append(typeName).append(", ");
            }

            tableSql.deleteCharAt(tableSql.length() - 2).append(");");
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(tableSql.toString());

                stmt.executeUpdate("INSERT INTO SYSTEM_DBTYPES_TABLE(SYS_ID_FIELD) VALUES (1)");

                for (Integer sqlType : javaClassToSqlType.get(String.class)) {
                    String similarType = getSimilarity(sqlType, "123,ABC-abc, ÀÁÂ-àáâ", (ps, val) -> ps.setString(1, (String) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(boolean.class)) {
                    String similarType = getSimilarity(sqlType, false, (ps, val) -> ps.setBoolean(1, (boolean) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(byte.class)) {
                    String similarType = getSimilarity(sqlType, Byte.MIN_VALUE, (ps, val) -> ps.setByte(1, (byte) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(short.class)) {
                    String similarType = getSimilarity(sqlType, Short.MIN_VALUE, (ps, val) -> ps.setShort(1, (short) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(int.class)) {
                    String similarType = getSimilarity(sqlType, Integer.MIN_VALUE, (ps, val) -> ps.setInt(1, (int) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(long.class)) {
                    String similarType = getSimilarity(sqlType, Long.MIN_VALUE, (ps, val) -> ps.setLong(1, (long) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(float.class)) {
                    String similarType = getSimilarity(sqlType, Float.MIN_VALUE, (ps, val) -> ps.setFloat(1, (float) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(double.class)) {
                    String similarType = getSimilarity(sqlType, Double.MIN_VALUE, (ps, val) -> ps.setDouble(1, (double) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(Date.class)) {
                    Date testDateVal = new Date(new java.util.Date().getTime());
                    String similarType = getSimilarity(sqlType, testDateVal, (ps, val) -> ps.setDate(1, (Date) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(Time.class)) {
                    String similarType = getSimilarity(sqlType, new Time(new java.util.Date().getTime()), (ps, val) -> ps.setTime(1, (Time) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                for (Integer sqlType : javaClassToSqlType.get(Timestamp.class)) {
                    String similarType = getSimilarity(sqlType, new Timestamp(new java.util.Date().getTime()), (ps, val) -> ps.setTimestamp(1, (Timestamp) val), similarities, conn);
                    if (similarType != null)
                        map.put(sqlType, similarType);
                }

                stmt.executeUpdate("DROP TABLE SYSTEM_DBTYPES_TABLE CASCADE;");
            }

        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }

        return map;
    }

    @Override
    public String getAutoIncrementType(int sqlType) {
        return "integer AUTOINCREMENT";
    }

    private static String getSimilarity(int sqlType, Object testVal, Action<PreparedStatement, Object> setterAction, HashMap<Integer, ArrayList<String>> similarities, Connection conn) {

        ArrayList<String> dbTypes = similarities.get(sqlType);
        if (dbTypes == null) return null;

        for (String dbType : dbTypes) {
            String columnName = removeSpaces(dbType) + "_FIELD";
            try(PreparedStatement ps = conn.prepareStatement(
                    "UPDATE SYSTEM_DBTYPES_TABLE SET " + columnName + " = ? WHERE SYS_ID_FIELD = 1;")){
                setterAction.accept(ps, testVal);
                ps.execute();
                try(Statement stmt = conn.createStatement();
                    ResultSet rsSelect = stmt.executeQuery("SELECT " + columnName + " FROM SYSTEM_DBTYPES_TABLE WHERE SYS_ID_FIELD = 1;")) {
                    if (rsSelect.next() && rsSelect.getObject(columnName)/*String(columnName)*/.equals(testVal)){
                        return dbType;
                    }
                }

            } catch (Exception ignored){
//                ignored.printStackTrace();
            }
        }

        return null;
    }

    private static String removeSpaces(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value.charAt(0));
        for (int i = 1; i < value.length(); i++) {
            if(" ".equals(value.substring(i, i + 1))){
                sb.append("_");
                continue;
            }
            if(value.substring(i, i + 1).equals(value.substring(i, i + 1).toUpperCase()))
                sb.append("_");
            sb.append(value.charAt(i));
        }

        return sb.toString().toUpperCase();
    }
}
