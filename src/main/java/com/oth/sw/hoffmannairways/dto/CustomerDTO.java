package com.oth.sw.hoffmannairways.dto;

import com.oth.sw.hoffmannairways.entity.Order;

public class CustomerDTO {
    //TODO split up in two queues?
    Message message; //e.g. request flights, request flightoperations, create order, ...?
    //TODO authentification?
    Order order; //if a new order should be created

    public enum Message {
        CREATE_ORDER,
        UPDATE_INFO
    }
}
