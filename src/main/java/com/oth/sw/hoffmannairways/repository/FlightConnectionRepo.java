package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.FlightConnection;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlightConnectionRepo extends CrudRepository<FlightConnection, String> {

    List<FlightConnection> findAllByOrderByDepartureAirport();
}
