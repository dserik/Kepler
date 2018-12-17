package com.kepler.db.dialects;

import java.sql.Connection;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Postgresql implements Dialect {

    static HashMap<Integer, List<String>> typeMap = new HashMap<>();
    static {
        typeMap.put(Types.VARCHAR, Collections.singletonList("varchar"));
        typeMap.put(Types.DECIMAL, Collections.singletonList("numeric"));
        typeMap.put(Types.BOOLEAN, Collections.singletonList("boolean"));
        typeMap.put(Types.TINYINT, Collections.singletonList("smallint"));
        typeMap.put(Types.SMALLINT, Collections.singletonList("smallint"));
        typeMap.put(Types.INTEGER, Collections.singletonList("integer"));
        typeMap.put(Types.BIGINT, Collections.singletonList("bigint"));
        typeMap.put(Types.REAL, Collections.singletonList("real"));
        typeMap.put(Types.DOUBLE, Collections.singletonList("double precision"));
        typeMap.put(Types.FLOAT, Collections.singletonList("double precision"));
        typeMap.put(Types.DATE, Collections.singletonList("date"));
        typeMap.put(Types.TIME, Collections.singletonList("time"));
        typeMap.put(Types.TIMESTAMP, Collections.singletonList("timestamp"));
    }

    @Override
    public HashMap<Integer, String> getTypeMap(Connection ignored) {
        HashMap<Integer, String> result = new HashMap<>();
        typeMap.forEach((i, arr) -> result.put(i, arr.get(0)));
        return result;
    }

    @Override
    public String getAutoIncrementType(int type) {
        switch (type) {
            case Types.BIGINT:
                return "bigserial";
            default: return "integer AUTOINCREMENT";
        }
    }


}
