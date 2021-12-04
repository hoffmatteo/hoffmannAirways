package com.oth.sw.hoffmannairways.service.exception;

import com.oth.sw.hoffmannairways.entity.Order;

public class FlightException extends Exception {
    public Order order;
    public String errorMessage;

    public FlightException(Order order, String errorMessage) {
        this.order = order;
        this.errorMessage = errorMessage;
    }
}
