package com.oth.sw.hoffmannairways.web.rest;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ProxyService implements tempAirportIF {
    private HashMap<FlightConnection, HashMap<Date, Flight>> airport = new HashMap<>();

    @Override
    public Flight createFlight(Flight f) {
        Date proposedTime = f.getDepartureTime();
        Calendar proposedCalendar = Calendar.getInstance();
        Date roundedTime = DateUtils.round(proposedTime, Calendar.HOUR);

        Date proposedSlot = DateUtils.addMinutes(roundedTime, 5 * (proposedCalendar.get(Calendar.MINUTE) / 5));
        System.out.println(proposedSlot);

        Date correctDate = findSlot(proposedSlot, f);

        f.setDepartureTime(correctDate);
        f.setArrivalTime(DateUtils.addMinutes(correctDate, (int) f.getConnection().getFlightTimeHours() * 60));

        return f;
    }

    @Override
    public boolean cancelFlight(Flight f) {
        return false;
    }

    @Override
    public Flight editFlight(Flight oldFlight, Flight newFlight) {
        return null;
    }

    private Date findSlot(Date slot, Flight f) {

        if (airport.containsKey(f.getConnection())) {
            while (airport.get(f.getConnection()).containsKey(slot)) {
                slot = DateUtils.addMinutes(slot, 5);
            }
        }
        HashMap<Date, Flight> currSlot = new HashMap<>();
        currSlot.put(slot, f);
        System.out.println(airport.toString());
        airport.put(f.getConnection(), currSlot);
        return slot;

    }
}
