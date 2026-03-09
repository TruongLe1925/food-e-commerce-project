package com.myproject.e_commerce.constants;

public enum StatusOrder {
    PENDING,
    CONFIRMED,
    PROCESSING,
    COMPLETED,
    CANCELLED;
    public StatusOrder next() {
        return switch (this) {
            case PENDING    -> CONFIRMED;
            case CONFIRMED  -> PROCESSING;
            case PROCESSING -> COMPLETED;
            default         -> this;
        };
    }
}
