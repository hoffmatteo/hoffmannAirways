package com.oth.sw.hoffmannairways.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO implements Serializable {
    int messageID; //shipment ID
    private Message message;
    private FlightConnection connection;
    private Order order; //if a new order should be created
    private UserDTO userInfo;

    public enum Message {
        CREATE_ORDER,
        UPDATE_CONNECTIONS,
        UPDATE_FLIGHTS
    }

    public FlightConnection getConnection() {
        return connection;
    }

    public void setConnection(FlightConnection connection) {
        this.connection = connection;
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

    public CustomerDTO(int messageID, Message message, Order order, UserDTO userInfo) {
        this.messageID = messageID;
        this.message = message;
        this.order = order;
        this.userInfo = userInfo;
    }

    public CustomerDTO(int messageID, Message message, FlightConnection connection, Order order, UserDTO userInfo) {
        this.messageID = messageID;
        this.message = message;
        this.connection = connection;
        this.order = order;
        this.userInfo = userInfo;
    }

    public CustomerDTO(int messageID, Message message, FlightConnection connection, UserDTO userInfo) {
        this.messageID = messageID;
        this.message = message;
        this.connection = connection;
        this.userInfo = userInfo;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
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
                ", order=" + Objects.toString(order, "null") +
                '}';
    }

    public CustomerDTO() {

    }
}
