package com.kepler.db.dialects;

import java.sql.Connection;
import java.util.HashMap;

public interface Dialect {

    HashMap<Integer, String> getTypeMap(Connection connection);

    String getAutoIncrementType(int sqlType);
}
