package garbage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;


@Target(FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface ForeignKey {
    String tableName();
    String fieldName();
}
