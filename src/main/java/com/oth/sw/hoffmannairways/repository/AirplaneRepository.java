package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.Airplane;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface AirplaneRepository extends CrudRepository<Airplane, Integer> {

    Airplane getAirplaneByPlaneID(int planeID);

    List<Airplane> getAirplanesByUnavailableUntilBeforeOrUnavailableUntilIsNull(Date date);



}
