package com.oth.sw.hoffmannairways.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oth.sw.hoffmannairways.entity.util.SingleIdEntity;
import de.othr.sw.HaberlRepairs.entity.dto.SingleRepairOrderDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Airplane extends SingleIdEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int planeID;
    private String planeName;
    private int totalSeats;
    private double maxCargo;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonIgnore
    private Date unavailableUntil;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private List<@NotNull SingleRepairOrderDTO> issues = new ArrayList<>();
    @OneToMany(mappedBy = "airplane", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    @Nullable
    private List<@NotNull Flight> assignments = new ArrayList<>();

    public Airplane(String planeName, int totalSeats, double maxCargo) {
        this.planeName = planeName;
        this.totalSeats = totalSeats;
        this.maxCargo = maxCargo;
    }


    public Airplane() {

    }

    @Override
    public Integer getID() {
        return this.planeID;
    }

    public int getPlaneID() {
        return planeID;
    }

    public void setPlaneID(int planeID) {
        this.planeID = planeID;
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

    public List<SingleRepairOrderDTO> getIssues() {
        return issues;
    }

    public void setIssues(List<SingleRepairOrderDTO> issues) {
        this.issues = issues;
    }

    public List<Flight> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Flight> assignment) {
        this.assignments = assignment;
    }

    @Override
    public String toString() {
        return "Airplane{" +
                "planeID=" + planeID +
                ", planeName='" + planeName + '\'' +
                ", totalSeats=" + totalSeats +
                ", maxCargo=" + maxCargo +
                ", unavailableUntil=" + unavailableUntil +
                '}';
    }


}
