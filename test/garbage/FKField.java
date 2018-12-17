package garbage;

public class FKField<T> {
    T value;

    public FKField(T value, boolean abortIfIncorrect){
        this.value = value;
        this.abortIfIncorrect = abortIfIncorrect;
    }

    public T getValue() {
        return value;
    }

    protected boolean abortIfIncorrect;
}
