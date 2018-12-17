package garbage;

import java.sql.JDBCType;

public @interface Field {
    String columnName();
    JDBCType type();
    /** предотвращение ввода более длинного текста */
    int length();
}
