package com.kepler.db;

import java.sql.SQLException;

/**BiFunction*/
@FunctionalInterface
public interface Action<T, U> extends Cloneable {

    void accept(T t, U u) throws SQLException, IllegalAccessException;

    default Action<T, U> andThen(Action<T, U> after){
        return (p, o) -> {
            accept(p, o);
            after.accept(p, o);
        };
    }
//
//    default Action<T, U> copy(){
//        return this::accept;
//    }
}
