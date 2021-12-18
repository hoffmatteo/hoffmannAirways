package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.Airplane;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface AirplaneRepository extends CrudRepository<Airplane, Integer> {

    Airplane getAirplaneByPlaneID(int planeID);
    //get all available planes
    List<Airplane> getAirplanesByUnavailableUntilBeforeOrUnavailableUntilIsNull(Date date);

    List<Airplane> findAll();
    //get all assigned planes
    List<Airplane> findAirplanesByUnavailableUntilAfterAndAssignmentIsNotNull(Date date);

    //get all planes that are being repaired
    List<Airplane> findAirplanesByUnavailableUntilAfterAndAssignmentIsNull(Date date);




}
