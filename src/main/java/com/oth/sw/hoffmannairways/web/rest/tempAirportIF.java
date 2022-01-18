package com.oth.sw.hoffmannairways.web.rest;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.service.exception.FlightException;

public interface tempAirportIF {

    public Flight createFlight(Flight f) throws FlightException;

    public boolean cancelFlight(Flight f) throws FlightException;

    Flight editFlight(Flight oldFlight, Flight newFlight) throws FlightException;
}
