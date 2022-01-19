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

    //TODO save?

    @Transactional
    public Airplane assignPlane(Flight flight) throws AirplaneException {
        if (flight.getAirplane() == null) {
            throw new AirplaneException("Plane is not set!", null);
        }
        Airplane plane = airplaneRepository.findAirplaneByPlaneID(flight.getAirplane().getPlaneID()).orElseThrow(() -> new AirplaneException("Plane could not be found", flight.getAirplane()));
        if (plane.getUnavailableUntil() == null || plane.getUnavailableUntil().before(flight.getDepartureTime())) {
            plane.setUnavailableUntil(flight.getArrivalTime());
            return plane;
        } else {
            throw new AirplaneException("Plane is unavailable!", plane);
        }
    }


    @Transactional
    public Airplane repairPlane(Airplane plane) throws AirplaneException {

        if (!plane.getIssues().isEmpty() && plane.getUnavailableUntil() != null) {
            Airplane oldPlane = airplaneRepository.findAirplaneByPlaneID(plane.getPlaneID()).orElseThrow();
            oldPlane.setUnavailableUntil(plane.getUnavailableUntil());
            oldPlane.setIssues(plane.getIssues());
            if (oldPlane.getAssignments() != null) {
                if (!oldPlane.getAssignments().isEmpty()) {
                    oldPlane.getAssignments().clear();
                }
            }
            return airplaneRepository.save(oldPlane);
        } else {
            throw new AirplaneException("Repair request information was not set properly!", plane);

        }
    }

    @Transactional
    public Airplane createPlane(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public Collection<Airplane> getAvailablePlanes() {
        return airplaneRepository.findAirplanesByUnavailableUntilBeforeOrUnavailableUntilIsNullOrderByPlaneName(new Date());
    }


    public Collection<Airplane> getAllPlanes() {
        return airplaneRepository.findAllByOrderByPlaneName();
    }

    public Collection<Airplane> getAllAssignedPlanes() {
        return airplaneRepository.findDistinctByUnavailableUntilAfterAndAssignmentsIsNotNull(new Date());
    }

    public Collection<Airplane> getAllBrokenPlanes() {
        return airplaneRepository.findAirplanesByUnavailableUntilAfterAndAssignmentsIsNull(new Date());
    }

    @Override
    public Airplane getPlane(int id) throws AirplaneException {
        return airplaneRepository.findAirplaneByPlaneID(id).orElseThrow(() -> new AirplaneException("TODO", null));
    }
}
