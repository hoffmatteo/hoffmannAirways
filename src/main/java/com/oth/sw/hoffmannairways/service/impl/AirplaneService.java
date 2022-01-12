package com.oth.sw.hoffmannairways.service.impl;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.repository.AirplaneRepository;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

@Service
public class AirplaneService implements AirplaneServiceIF {
    @Autowired
    AirplaneRepository airplaneRepository;
    //TODO Schnittstelle fehlt --> flightService
    //TODO oder ich rufe das von FlightService aus auf?

    @Transactional
    public Airplane assignPlane(Flight flight) throws AirplaneException {
        Airplane plane = airplaneRepository.findAirplaneByPlaneID(flight.getAirplane().getPlaneID()).orElseThrow(() -> new AirplaneException("Plane could not be found", flight.getAirplane()));
        if (plane.getUnavailableUntil() == null || plane.getUnavailableUntil().before(new Date())) {
            plane.setUnavailableUntil(flight.getArrivalTime());
            return plane;
        } else {
            throw new AirplaneException("Plane is unavailable!", plane);
        }
    }


    @Transactional
    public Airplane repairPlane(Airplane plane) throws AirplaneException {

        if (!plane.getIssues().isEmpty() && plane.getUnavailableUntil() != null) {
            //TODO call haberl repairs
            //Date newDeadline = new Date();
            //plane.setUnavailableUntil(newDeadline);
            return airplaneRepository.save(plane);
        } else {
            throw new AirplaneException("Repair request information was not set properly!", plane);

        }
    }

    @Transactional
    public Airplane createPlane(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public Collection<Airplane> getAvailablePlanes() {
        return airplaneRepository.findAirplanesByUnavailableUntilBeforeOrUnavailableUntilIsNull(new Date());
    }


    public Collection<Airplane> getAllPlanes() {
        return airplaneRepository.findAll();
    }

    public Collection<Airplane> getAllAssignedPlanes() {
        return airplaneRepository.findAirplanesByUnavailableUntilAfterAndAssignmentIsNotNull(new Date());
    }

    public Collection<Airplane> getAllBrokenPlanes() {
        return airplaneRepository.findAirplanesByUnavailableUntilAfterAndAssignmentIsNull(new Date());
    }


}
