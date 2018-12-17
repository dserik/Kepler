package garbage.guild;

/**
 * @author     Daryn Serikbayev <daryn.serikbayev@gmail.com>
 */
public class Field<T> {
    private T value;

    public Field(T value, boolean modified) {
        setValue(value);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
