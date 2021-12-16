package com.oth.sw.hoffmannairways.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int planeID;
    private String planeName;
    private int totalSeats;
    private double maxCargo;
    private Date unavailableUntil;
    @ElementCollection
    private List<String> issues;
    @OneToOne(mappedBy = "airplane")

    private Flight assignment;

    public int getPlaneID() {
        return planeID;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public double getMaxCargo() {
        return maxCargo;
    }

    public void setMaxCargo(double maxCargo) {
        this.maxCargo = maxCargo;
    }

    public Date getUnavailableUntil() {
        return unavailableUntil;
    }

    public void setUnavailableUntil(Date unavailableUntil) {
        this.unavailableUntil = unavailableUntil;
    }

    public List<String> getIssues() {
        return issues;
    }

    public void setIssues(List<String> issues) {
        this.issues = issues;
    }

    public Flight getAssignment() {
        return assignment;
    }

    public void setAssignment(Flight assignment) {
        this.assignment = assignment;
    }

    public Airplane(String planeName, int totalSeats, double maxCargo) {
        this.planeName = planeName;
        this.totalSeats = totalSeats;
        this.maxCargo = maxCargo;
    }

    public Airplane() {

    }
}
