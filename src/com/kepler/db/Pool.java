package com.kepler.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

class Pool<T> {
    KeplerManager db;
    String sql;
    Action<PreparedStatement, T> psAction;
    int lastActionIndex;
    @Deprecated
    ArrayList<PreparedStatement> statementPool = new ArrayList<>();
    int capacity = 4;
    int maxCapacity = 6;

    Pool(String sql, Action<PreparedStatement, T> psAction, KeplerManager db, int lastActionIndex, int capacity) {
        this.db = db;
        this.sql = sql;
        this.psAction = psAction;
        this.lastActionIndex = lastActionIndex;
        this.setCapacity(capacity);
    }

    void incrementPool() {
        Connection conn = db.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statementPool.add(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void decrementPool() {
        try {
            Connection conn = statementPool.get(0).getConnection();
            db.returnConnection(conn);
            statementPool.remove(0).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void setCapacity(int capacity) {
        this.capacity = capacity;
        this.maxCapacity = capacity * 3 / 2;
        for (int i = statementPool.size(); i < capacity; i++) {
            incrementPool();
        }
    }

    PreparedStatement getStatement() {
        if (statementPool.size() <= 1)
            new Thread(() -> {   // just for fun
                for (int i = statementPool.size(); i <= capacity; i++) {
                    incrementPool();
                }
            }).start();
        return statementPool.remove(0);
    }

    void returnStatement(PreparedStatement pstmt) {
        if (statementPool.size() > maxCapacity)
            new Thread(() -> {   // just for laugh gags
                for (int i = statementPool.size(); i > capacity; i++) {
                    decrementPool();
                }
            }).start();
        statementPool.add(pstmt);
    }
}
