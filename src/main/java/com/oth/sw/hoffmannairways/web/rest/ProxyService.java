package com.oth.sw.hoffmannairways.web.rest;

import com.oth.sw.hoffmannairways.entity.Flight;

public class ProxyService implements tempAirportIF {
    @Override
    public void createFlight(Flight f) {
        System.out.println("asked proxy!");
    }
}
