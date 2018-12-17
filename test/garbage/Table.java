package garbage;

import garbage.guild.Field;

import java.util.HashMap;

/**
 * @author     Daryn Serikbayev <daryn.serikbayev@gmail.com>
 */
public abstract class Table {
    protected HashMap<String, Field> fields = new HashMap<>();

    protected abstract String getTableName();

    protected Object getField(String key) {
        Field field = fields.get(key);
        if (field != null && field.getValue() != null) {
            return field.getValue();
        }
        return null;
    }
}
