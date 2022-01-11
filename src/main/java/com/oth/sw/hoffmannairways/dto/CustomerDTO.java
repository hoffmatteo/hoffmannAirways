package com.oth.sw.hoffmannairways.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oth.sw.hoffmannairways.entity.Order;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO implements Serializable {
    //TODO split up in two queues?
    private Message message; //e.g. request flights, request flightoperations, create order, ...?
    //TODO authentification?
    private Order order; //if a new order should be created
    private UserDTO userInfo;

    public enum Message {
        CREATE_ORDER,
        UPDATE_INFO
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public CustomerDTO(Message message) {
        this.message = message;
    }

    public CustomerDTO(Message message, Order order) {
        this.message = message;
        this.order = order;
    }

    public UserDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserDTO userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "message=" + message +
                ", order=" + order +
                '}';
    }

    public CustomerDTO() {

    }
}
