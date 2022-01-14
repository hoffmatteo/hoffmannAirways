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
        System.out.println("asked proxy!");
        Date proposedSlot = f.getDepartureTime();
        //round time to 5 minute slots: e.g. 13:34 --> 13:30-13:35, 13:36 --> 13:35-13:40
        Calendar proposedCalendar = Calendar.getInstance();
        Date roundedSlot = DateUtils.truncate(proposedSlot, Calendar.HOUR);
        //TODO source https://stackoverflow.com/a/40421043
        roundedSlot = DateUtils.addMinutes(roundedSlot, (proposedCalendar.get(Calendar.MINUTE) / 5) * 5);

        Date correctSlot = findSlot(roundedSlot, f);

        f.setDepartureTime(correctSlot);
        f.setArrivalTime(DateUtils.addMinutes(correctSlot, (int) f.getConnection().getFlightTimeHours() * 60));

        return f;


    }

    private Date findSlot(Date slot, Flight f) {

        if (airport.containsKey(f.getConnection())) {
            while (airport.get(f.getConnection()).containsKey(slot)) {
                slot = DateUtils.addMinutes(slot, 5);
            }
        }
        HashMap<Date, Flight> temp = new HashMap<>();
        temp.put(slot, f);
        airport.put(f.getConnection(), temp);
        return slot;

    }
}
