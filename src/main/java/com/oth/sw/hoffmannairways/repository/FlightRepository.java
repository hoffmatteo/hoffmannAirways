package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface FlightRepository extends CrudRepository<Flight, Integer> {

    Flight findFlightByAirplane_PlaneID(int planeID);

    List<Flight> getAllByDepartureTimeAfter(Date date);

    List<Flight> getAllByConnection(FlightConnection connection);


}
