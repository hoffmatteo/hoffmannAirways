package com.oth.sw.hoffmannairways.service;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;

import java.util.Collection;
import java.util.Date;

public interface AirplaneServiceIF {

    public Airplane assignPlane(Flight flight) throws AirplaneException;

    public Airplane repairPlane(Airplane plane) throws AirplaneException;

    public Airplane createPlane(Airplane plane) throws AirplaneException;

    public Collection<Airplane> getAvailablePlanes();

    public Collection<Airplane> getAllPlanes();

    public Collection<Airplane> getAllAssignedPlanes();

    public Collection<Airplane> getAllBrokenPlanes();

    public Airplane getPlane(int id) throws AirplaneException;
    
    void updateUnavailable(Date date, int planeID) throws AirplaneException;
}
