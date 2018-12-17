package com.kepler.db;

public class KeplerException extends Exception {
    public KeplerException(Exception e) {
        super(e);
    }

    public KeplerException(String message) {
        super(message);
    }
}
