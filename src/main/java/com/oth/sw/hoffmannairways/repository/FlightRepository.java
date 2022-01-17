package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface FlightRepository extends CrudRepository<Flight, Integer> {

    List<Flight> findFlightsByAirplane_PlaneID(int planeID);

    List<Flight> getAllByDepartureTimeAfterOrderByDepartureTime(Date date);

    List<Flight> getAllByConnection(FlightConnection connection);


}
