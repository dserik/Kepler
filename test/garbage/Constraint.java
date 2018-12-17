package garbage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Constraint {
    boolean isPrimaryKey() default false;
    boolean isForeignKey() default false;
    boolean isUnique() default false;
    boolean isNullable() default true;
}
