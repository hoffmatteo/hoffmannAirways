package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findOrdersByFlight_FlightID(int flightID);
}
