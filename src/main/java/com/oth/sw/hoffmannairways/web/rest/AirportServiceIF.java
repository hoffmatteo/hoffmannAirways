package com.oth.sw.hoffmannairways.web.rest;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.service.exception.FlightException;

public interface AirportServiceIF {
    public Flight createFlight(Flight f) throws FlightException;

    public boolean cancelFlight(Flight f) throws FlightException;

    public Flight editFlight(Flight oldFlight, Flight newFlight) throws FlightException;
}
