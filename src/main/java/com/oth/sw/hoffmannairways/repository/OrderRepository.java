package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findOrdersByFlight_FlightID(int flightID);

    List<Order> findAll();

    List<Order> findOrdersByCustomer_UsernameAndFlight_DepartureTimeAfterOrderByFlight_DepartureTime(String customer_name, Date flight_departureTime);

    List<Order> findOrdersByCustomer_UsernameAndFlight_DepartureTimeBeforeOrderByFlight_DepartureTime(String customer_name, Date flight_departureTime);

    List<Order> findOrdersByFlight_DepartureTimeAfterOrderByFlight_DepartureTime(Date flight_departureTime);

    List<Order> findOrdersByFlight_DepartureTimeBeforeOrderByFlight_DepartureTime(Date flight_departureTime);

}
