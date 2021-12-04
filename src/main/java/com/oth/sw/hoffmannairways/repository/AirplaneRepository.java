package com.oth.sw.hoffmannairways.repository;

import com.oth.sw.hoffmannairways.entity.Airplane;
import org.springframework.data.repository.CrudRepository;

public interface AirplaneRepository extends CrudRepository<Airplane, Integer> {

    Airplane getAirplaneByPlaneID(int planeID);

}
