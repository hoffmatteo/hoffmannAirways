package com.oth.sw.hoffmannairways.service;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.repository.AirplaneRepository;
import com.oth.sw.hoffmannairways.service.inf.AirplaneServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class AirplaneService implements AirplaneServiceIF {
    @Autowired
    AirplaneRepository airplaneRepository;
    //TODO Schnittstelle fehlt --> flightService
    //TODO oder ich rufe das von FlightService aus auf?

    @Transactional
    public Airplane assignPlane(Flight flight) {
        Airplane plane = airplaneRepository.getAirplaneByPlaneID(flight.getAirplane().getPlaneID());
        //TODO was bedeutet assignPlane überhaupt?
        //Availability setzen?
        if (plane.getUnavailableUntil() == null || plane.getUnavailableUntil().before(new Date())) {
            plane.setUnavailableUntil(flight.getArrivalTime());
            return plane;
        } else {
            //TODO Exception?
            return null;
        }
    }


    @Transactional
    public Airplane repairPlane(Airplane plane) {
        if (!plane.getIssues().isEmpty() && plane.getUnavailableUntil() != null) {
            //TODO call haberl repairs
            Date newDeadline = new Date();
            plane.setUnavailableUntil(newDeadline);
            return airplaneRepository.save(plane);
        }
        return null;
    }

    @Transactional
    public Airplane createPlane(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }


}
