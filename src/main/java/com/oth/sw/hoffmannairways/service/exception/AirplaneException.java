package com.oth.sw.hoffmannairways.service.exception;

import com.oth.sw.hoffmannairways.entity.Airplane;

public class AirplaneException extends Exception {
    private Airplane plane;


    public AirplaneException(String message, Airplane plane) {
        super(message);
        this.plane = plane;
    }

    public Airplane getPlane() {
        return plane;
    }

    public void setPlane(Airplane plane) {
        this.plane = plane;
    }
}