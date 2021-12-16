package com.oth.sw.hoffmannairways.service;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;

import java.util.Collection;

public interface AirplaneServiceIF {

    public Airplane assignPlane(Flight flight);

    public Airplane repairPlane(Airplane plane);

    public Airplane createPlane(Airplane plane);

    public Collection<Airplane> getAvailablePlanes();

    public Collection<Airplane> getAllPlanes();

}
