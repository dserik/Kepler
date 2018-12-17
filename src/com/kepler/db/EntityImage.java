package com.kepler.db;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EntityImage<T> {

//    private Database db;
    private String tableName;
    private HashMap<String, DbField> dbFields = new HashMap<>();
    private HashMap<String, Field> classFields = new HashMap<>();
    private Class<T> entityClass;
    private java.lang.reflect.Field idField;
    private String idColumnName;

    private HashMap<Operation, Pool> poolTemplates = new HashMap<>();
    private Action<ResultSet, Object> getSelectAction;
    private HashMap<String, Pool> pool = new HashMap<>();

    private String addDbField(Field field, boolean isIdField) {
        javax.persistence.Column columnProperties = field.getDeclaredAnnotation(javax.persistence.Column.class);
        String columnName = columnProperties != null && !columnProperties.name().trim().isEmpty()
                ? columnProperties.name()
                : Utils.generateDbFieldName(field.getName());
        int maxLength = columnProperties != null ? columnProperties.length() : 0;

        dbFields.put(field.getName(), new DbField(
                columnName,
                JDBCType.valueOf(Utils.javaClassToSqlType.get(field.getType())[0]),
                isIdField,
                columnProperties == null || columnProperties.nullable(),
                isIdField || (columnProperties != null && !columnProperties.insertable()),
                maxLength,
                null
        ));

        return columnName;
    }

    private Action<PreparedStatement, T> setActions(Field field, Action<PreparedStatement, T> insertUpdateAction, String columnName, int idx){

        switch(((Class)field.getGenericType()).getCanonicalName()){

            case "java.lang.StringBuilder" :
            case "java.lang.StringBuffer" :
            case "java.lang.String" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getString(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setString(idx, String.valueOf(field.get(e))));
            case "java.math.BigDecimal" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getBigDecimal(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setBigDecimal(idx, (BigDecimal) field.get(e)));
            case "java.lang.Boolean" :
            case "boolean" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getBoolean(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setBoolean(idx, (Boolean) field.get(e)));
            case "java.lang.Byte" :
            case "byte" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getByte(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setByte(idx, (Byte) field.get(e)));
            case "java.lang.Short" :
            case "short" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getShort(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setShort(idx, (Short) field.get(e)));
            case "java.lang.Integer" :
            case "int" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getInt(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setInt(idx, (Integer) field.get(e)));
            case "java.lang.Long" :
            case "long" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getLong(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setLong(idx, (Long) field.get(e)));
            case "java.lang.Float" :
            case "float" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getFloat(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setFloat(idx, (Float) field.get(e)));
            case "java.lang.Double" :
            case "double" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getDouble(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setDouble(idx, (Double) field.get(e)));
            case "java.util.Date" :
                getSelectAction = getSelectAction.andThen((r, o) -> {
                    java.sql.Date date = r.getDate(columnName);
                    field.set(o, date != null ? new java.util.Date(date.getTime()) : null);
                });
                return insertUpdateAction.andThen((p, e) -> {
                    java.util.Date date = (java.util.Date) field.get(e);
                    p.setDate(idx, date != null ? new java.sql.Date(date.getTime()) : null);
                });
            case "java.sql.Date" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getDate(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setDate(idx, (java.sql.Date) field.get(e)));
            case "java.sql.Time" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getTime(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setTime(idx, (Time) field.get(e)));
            case "java.sql.Timestamp" :
                getSelectAction = getSelectAction.andThen((r, o) -> field.set(o, r.getTimestamp(columnName)));
                return insertUpdateAction.andThen((p, e) -> p.setTimestamp(idx, (Timestamp) field.get(e)));
                    
        }
        return insertUpdateAction;//todo deprecated
    }

    EntityImage(KeplerManager db, Class<T> entity) throws KeplerException {
        if(!entity.isAnnotationPresent(javax.persistence.Entity.class))
            throw new KeplerException("Class " + entity.getSimpleName() + " not an entity");

        idField = Arrays.asList(entity.getFields()).stream().filter(f -> f.isAnnotationPresent(Id.class)).findFirst().orElse(null);
        if(idField == null)
            throw new KeplerException("Id field not found for class " + entity.getSimpleName());

        String canonicalName = ((Class)idField.getGenericType()).getCanonicalName();
        if(!(canonicalName.equals("long") || canonicalName.equals("java.lang.Long")))
            throw new KeplerException("Id field must be type of long for class " + entity.getSimpleName());

//        this.db = db;
        this.entityClass = entity;
        Arrays.asList(entity.getFields()).stream().filter(f ->
                !(f.isAnnotationPresent(javax.persistence.Transient.class) || f.equals(idField)))
                .forEach(f -> classFields.put(f.getName(), f));

        javax.persistence.Table tableAnnotation = entity.getDeclaredAnnotation(javax.persistence.Table.class);
        this.tableName = tableAnnotation != null && !tableAnnotation.name().trim().isEmpty()
                ? tableAnnotation.name()
                : Utils.generateDbFieldName(entity.getSimpleName());


        StringBuilder sbInsert = new StringBuilder("INSERT INTO ").append(tableName).append("(");
        StringBuilder sbInsertClosing = new StringBuilder(") VALUES (");
        StringBuilder sbUpdate = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        StringBuilder sbSelect = new StringBuilder("SELECT ");

        Action<PreparedStatement, T> insertUpdateAction = (p, e) -> {}; //todo need to optimize
        Action<PreparedStatement, T> selectAction = (p, e) -> {};  //todo need to optimize

        idColumnName = addDbField(idField, true);
        getSelectAction = (r, o) -> idField.set(o, r.getLong(idColumnName));
        sbSelect.append(idColumnName).append(", ");
        int idx = 0;
        for (Field field : classFields.values()) {
            String columnName = addDbField(field, false);
            insertUpdateAction = setActions(field, insertUpdateAction, columnName, ++idx);
            sbInsert.append(columnName).append(", ");
            sbInsertClosing.append("?, ");
            sbUpdate.append(columnName).append(" = ?, ");
            sbSelect.append(columnName).append(", ");
        }
        sbInsertClosing.delete(sbInsertClosing.lastIndexOf(", "), sbInsertClosing.length());
        sbInsert.delete(sbInsert.lastIndexOf(", "), sbInsert.length()).append(sbInsertClosing).append(")").append(";");
        sbUpdate.delete(sbUpdate.lastIndexOf(", "), sbUpdate.length());
        sbSelect.delete(sbSelect.lastIndexOf(", "), sbSelect.length()).append(" from ").append(tableName);


        ArrayList<DbField> columns = db.tables.get(tableName);
        if(columns == null) { //table does not exist
            if(db.defaultAction.equals(KeplerManager.DefaultAction.THROW_EXCEPTION))
                throw new KeplerException("Table " + tableName + " not exist");

            StringBuilder createTableSql = new StringBuilder("CREATE TABLE " + tableName + " (\n");
            Connection conn = db.getConnection();
            try (Statement statement = conn.createStatement()){
                for (DbField dbField : dbFields.values()) {
                    createTableSql.append(dbField.name).append(" ").append(dbField.isAutincrement ? db.dialect.getAutoIncrementType(dbField.jdbcType.getVendorTypeNumber()) : db.sqlTypeRDBMS.get(dbField.jdbcType.getVendorTypeNumber())).append(", ");
                }
                createTableSql.append("CONSTRAINT ").append(tableName).append("_pkey ").append("PRIMARY KEY (").append(dbFields.get(idField.getName()).name).append("));");
                statement.executeUpdate(createTableSql.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            db.returnConnection(conn);
        } else {
            StringBuilder sbUpdateTableSql = new StringBuilder();
            dbFields.values().stream().filter(dbField -> columns.stream().filter(df -> df.name.equals(dbField.name)).count() == 0)
                    .forEach(dbField -> sbUpdateTableSql.append("ALTER TABLE ").append(tableName)
                    .append(" ADD COLUMN ").append(dbField.name).append(" ").append(db.sqlTypeRDBMS.get(dbField.jdbcType.getVendorTypeNumber())).append(dbField.isAutincrement ? " AUTO_INCREMENT" : "").append("; "));
            if (sbUpdateTableSql.length() > 0) {
                if(db.defaultAction.equals(KeplerManager.DefaultAction.THROW_EXCEPTION))
                    throw new KeplerException("Table " + tableName + " not exist");

                Connection conn = db.getConnection();
                try (Statement statement = conn.createStatement()){
                    statement.executeUpdate(sbUpdateTableSql.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        poolTemplates.put(Operation.INSERT, new Pool<>(sbInsert.toString(), insertUpdateAction, db, idx, 0));
        poolTemplates.put(Operation.UPDATE, new Pool<>(sbUpdate.toString(), insertUpdateAction, db, idx, 0));
        poolTemplates.put(Operation.SELECT, new Pool<>(sbSelect.toString(), selectAction, db, 0, 0));
    }



    void execute(Operation op, List<T> entity, Condition condition) throws KeplerException, SQLException, IllegalAccessException, InstantiationException {
        execute(op, entity, condition == null ? null : new Condition.Clause(null, new Condition[]{condition}, Condition.UnaryOperator.NULL));
    }

    private String extractClause(Condition.Clause clause) {
        StringBuilder result = new StringBuilder("(");
        if (clause.clauses != null && clause.clauses.size() > 0) {
            result.append(clause.clauses.stream()
                    .map(this::extractClause)
                    .collect(Collectors.joining(clause.unaryOperator.operator)));
        }

        if (clause.conditions != null && clause.conditions.size() > 0) {
            result.append(result.length() > 1 ? clause.unaryOperator.operator : "");
            for (Condition condition : clause.conditions) {
                result.append(dbFields.get(condition.field)).append(condition.sign.signMark).append(" ? ").append(clause.unaryOperator.operator);
            }
            result.delete(result.lastIndexOf(clause.unaryOperator.operator), result.length());
        }

        return result.append(")").toString();
    }

    int suplementAction(Action<PreparedStatement, T> psAction/*, Action<ResultSet, Object> rsAction*/, Condition.Clause clause/*, Operation op*/, int lastActionIndex) throws KeplerException {

        int[] lastIdx = new int[]{lastActionIndex};
        if (clause.clauses != null && clause.clauses.size() > 0) {
            for (Condition.Clause c : clause.clauses) {
                lastIdx[0] = suplementAction(psAction, c, lastIdx[0]);
            }
        }

        if (clause.conditions != null && clause.conditions.size() > 0) {
            for (Condition condition : clause.conditions) {
                switch (condition.value.getClass().getSimpleName()) {
                    case "String" :
                        psAction.andThen((p, e) -> p.setString(++lastIdx[0], (String)classFields.get(condition.field).get(e)));
                        break;
                    case "int" : case "Integer" :
                        psAction.andThen((p, e) -> p.setInt(++lastIdx[0], (Integer) classFields.get(condition.field).get(e)));
                        break;
                    case "boolean" : case "Boolean" :
                        psAction.andThen((p, e) -> p.setBoolean(++lastIdx[0], (Boolean) classFields.get(condition.field).get(e)));
                        break;
                    default: throw new KeplerException("unknown type " + condition.value.getClass().getSimpleName());
                }
            }
        }

        return lastIdx[0];
    }

    void execute(Operation op, List<T> entity, Condition.Clause conditions) throws KeplerException, SQLException, IllegalAccessException, InstantiationException {
        StringBuilder sql = new StringBuilder(poolTemplates.get(op).sql);
        boolean conditionExists = !op.equals(Operation.INSERT) && conditions != null;
        if (conditionExists) {
            sql.append(" WHERE ").append(extractClause(conditions)).append(";");
        }

        Pool p = pool.get(sql.toString());
        if (p == null) {
            Action<PreparedStatement, T> action = poolTemplates.get(op).psAction::accept;
            int lastIdx = 0;
            if (conditionExists) {
                lastIdx = suplementAction(action, conditions, poolTemplates.get(op).lastActionIndex);
            }
//            p = new Pool(this, sql.toString(), action, lastIdx, 2);
            pool.put(sql.toString(), p);
        }


        PreparedStatement ps = p.getStatement();
        p.psAction.accept(ps, entity.get(0));
        switch (op) {
            case INSERT:
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    idField.set(entity.get(0), rs.getInt(idColumnName));
                }
                break;
            case UPDATE:
                ps.executeUpdate();
                break;
            case SELECT:
                ResultSet rsSelect = ps.executeQuery();
                while (rsSelect.next()) {
                    T obj = entityClass.newInstance();
                    getSelectAction.accept(rsSelect, obj);
                    entity.add(obj);
                }
                break;
        }
        p.returnStatement(ps);
    }

    enum Operation{
        INSERT, UPDATE, SELECT
    }
}
