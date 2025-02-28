package org.ticketbooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

public class Train {
    private String trainNo;
    private String trainId;

//    private Date arrival;
//    private Date departure;
    private List<List<Integer>> seats;
    private List<String> stations;
    private Map<String,String> stationsTimes;
    public Train(){}

    public Train(String trainNo, String trainId, List<List<Integer>> seats, List<String> stations, Map<String, String> stationsTimes) {
        this.trainNo = trainNo;
        this.trainId = trainId;
        this.seats = seats;
        this.stations = stations;
        this.stationsTimes = stationsTimes;
    }

    public String getTrainInfo(){
        return String.format("Train ID: %s Train No: %s", trainId, trainNo);
    }

    public String getTrainNo() {
        return this.trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainId() {
        return this.trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public List<List<Integer>> getSeats() {
        return this.seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public List<String> getStations() {
        return this.stations;
    }

    public void setStations(List<String> trainRoute) {
        this.stations = trainRoute;
    }

    public Map<String, String> getStationsTimes() {
        return this.stationsTimes;
    }

    public void setStationsTimes(Map<String, String> stationsTimes) {
        this.stationsTimes = stationsTimes;
    }

}
