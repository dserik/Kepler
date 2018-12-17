package com.kepler.db;

import com.kepler.db.dialects.Dialect;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.kepler.db.Utils.replaceNull;

/**
 * @author     Daryn Serikbayev <daryn.serikbayev@gmail.com>
 */
//@SuppressWarnings("UnusedDeclaration")
public class KeplerManager implements EntityManager {

    public enum DefaultAction{
        CREATE, THROW_EXCEPTION
    }

    HashMap<String, ArrayList<DbField>> tables = new HashMap<>();
    HashMap<Integer, String> sqlTypeRDBMS = new HashMap<>();
    private HashMap<Class, EntityImage> images = new HashMap<>();
    final DefaultAction defaultAction;
    Supplier<Connection> getConnection;
    Consumer<Connection> returnConnAction;
    Dialect dialect;

    public KeplerManager(DefaultAction action, Supplier<Connection> getConnAction, Consumer<Connection> returnConnAction) throws KeplerException {
        this.defaultAction = action;
        this.getConnection = getConnAction;
        this.returnConnAction = returnConnAction;
        Connection conn = getConnection();
        dialect = Utils.getDialect(conn);
        this.sqlTypeRDBMS = dialect.getTypeMap(conn);
        returnConnection(conn);
        for (String tableName : getTables()) {
            tables.put(tableName.toUpperCase(), getFields(tableName));
        }
    }

    public <T> void update(T object) throws KeplerException{
        update(object, (Condition.Clause)null);
    }

    public <T> void update(T object, Condition condition) throws KeplerException {
        update(object, new Condition.Clause(null,  new Condition[]{condition}, Condition.UnaryOperator.NULL));
    }

    public <T> void update(T object, Condition.Clause conditions) throws KeplerException {
        EntityImage<T> image = images.get(object.getClass());
        if (image == null)
            image = processEntity(object.getClass());

        try {
            image.execute(EntityImage.Operation.UPDATE, Collections.singletonList(object), conditions);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new KeplerException(e);
        }
    }

    public <T> ArrayList<T> select(Class<T> entityClass) throws KeplerException{
        return select((Condition.Clause) null, entityClass);
    }

    public <T>ArrayList<T> select(Condition condition, Class<T> entityClass) throws KeplerException {
        return select(new Condition.Clause(null, new Condition[]{condition}, Condition.UnaryOperator.NULL), entityClass);
    }

    public <T> ArrayList<T> select(Condition.Clause conditions, Class<T> entityClass) throws KeplerException {

        EntityImage<T> image = images.get(entityClass);
        if (image == null)
            image = processEntity(entityClass);

        try {
            ArrayList<T> object = new ArrayList<>();
            image.execute(EntityImage.Operation.SELECT, object, conditions);
            return object;
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new KeplerException(e);
        }
    }

    public <T> void insert(T object) throws KeplerException {
        EntityImage image = images.get(object.getClass());
        if (image == null)
            image = processEntity(object.getClass());

        try {
            image.execute(EntityImage.Operation.INSERT, Collections.singletonList(object), (Condition.Clause)null);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new KeplerException(e);
        }
    }

    private <T> EntityImage processEntity(Class<T> entity) throws KeplerException {
        EntityImage image = new EntityImage<>(this, entity);
        images.put(entity, image);
        return image;
    }

    public Connection getConnection(){
        return getConnection.get();
    }

    public void returnConnection(Connection conn){
        returnConnAction.accept(conn);
    }

    private List<String> getTables() throws KeplerException {
        ArrayList<String> resultList = new ArrayList<>();
        Connection conn = getConnection();

        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            String[] types = {"TABLE"};
            try (ResultSet rs = dbmd.getTables(null, null, "%", types)) {
                while (rs.next()) {
                    resultList.add(rs.getString("table_name"));
                }
            }
        } catch (SQLException e) {
            throw new KeplerException(e);
        }

        returnConnection(conn);
        return resultList;
    }


    ArrayList<DbField> getFields(String tableName) throws KeplerException {
        ArrayList<DbField> resultList = new ArrayList<>();
        Connection conn = getConnection();

        try{
            DatabaseMetaData dbmd = conn.getMetaData();
            try(
                    ResultSet rsColumns = dbmd.getColumns(null, null, tableName, "%");
                    ResultSet rsPk = dbmd.getPrimaryKeys(null, null, tableName);
                    ResultSet rsFk = dbmd.getImportedKeys(null, null, tableName)) {

                HashMap<String, DbField.FKData> fkList = new HashMap<>();
                while (rsFk.next()) {
                    fkList.put(
                            replaceNull(rsFk.getString("FKCOLUMN_NAME")).toUpperCase(),
                            new DbField.FKData(
                                    replaceNull(rsFk.getString("PKTABLE_NAME")).toUpperCase(),
                                    replaceNull(rsFk.getString("PKCOLUMN_NAME")).toUpperCase()));
                }

                Set<String> pkList = new HashSet<>();
                while (rsPk.next()) {
                    pkList.add(replaceNull(rsPk.getString("COLUMN_NAME")).toUpperCase());
                }

                while (rsColumns.next()) {
                    resultList.add(
                            new DbField(
                                    replaceNull(rsColumns.getString("COLUMN_NAME")).toUpperCase(),
                                    JDBCType.valueOf(rsColumns.getInt("DATA_TYPE")),
                                    pkList.contains(replaceNull(rsColumns.getString("COLUMN_NAME")).toUpperCase()),
                                    "YES".equals(rsColumns.getString("IS_NULLABLE")),
                                    "YES".equals(rsColumns.getString("IS_AUTOINCREMENT")),
                                    rsColumns.getInt("COLUMN_SIZE"),
                                    fkList.get(replaceNull(rsColumns.getString("COLUMN_NAME")).toUpperCase())
                            ));
                }
            }
        } catch (SQLException e) {
            throw new KeplerException(e);
        }

        return resultList;
    }


    @Override
    public void persist(Object o) {

    }

    @Override
    public <T> T merge(T t) {
        return null;
    }

    @Override
    public void remove(Object o) {

    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return null;
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {

    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {

    }

    @Override
    public void refresh(Object o) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Query createQuery(String s) {
        return null;
    }

    @Override
    public Query createNamedQuery(String s) {
        return null;
    }

    @Override
    public Query createNativeQuery(String s) {
        return null;
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return null;
    }

    @Override
    public void joinTransaction() {

    }

    @Override
    public Object getDelegate() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public EntityTransaction getTransaction() {
        return null;
    }
}
