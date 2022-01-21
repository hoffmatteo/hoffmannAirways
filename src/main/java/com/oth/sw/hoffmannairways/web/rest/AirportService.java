package com.oth.sw.hoffmannairways.web.rest;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.util.logger.LoggerIF;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class AirportService {
    @Autowired
    private RestTemplate restServiceClient;

    @Autowired
    @Qualifier("SuccessLogger")
    private LoggerIF successLogger;

    private final String airportString = "http://im-codd.oth-regensburg.de:8935/api/rest/";

    private final String createPath = airportString + "/flight";

    private final String cancelPath = createPath + "/cancel";

    public Flight createFlight(Flight f) throws FlightException {
        FlighttransactionDTO dto = createDTO(f);
        try {
            FlighttransactionDTO returnedDTO = restServiceClient.postForObject(createPath, dto,
                    FlighttransactionDTO.class);

            if (returnedDTO.getDepartureTime() != null && returnedDTO.getArrivalTime() != null) {
                f.setDepartureTime(convertToDateViaSqlTimestamp(returnedDTO.getDepartureTime()));
                f.setArrivalTime(convertToDateViaSqlTimestamp(returnedDTO.getArrivalTime()));
                successLogger.log("AirportService", "Successfully created flight at ErbenAirports " + f);
                return f;

            } else {
                throw new FlightException("Could not create flight: Received null values from airport ", f);

            }


        } catch (RestClientException e) {
            throw new FlightException("Could not create flight: Communication to airport failed!", f);
        }
    }

    private FlighttransactionDTO createDTO(Flight f) {
        FlightConnection connection = f.getConnection();
        Airplane airplane = f.getAirplane();

        return new FlighttransactionDTO("HoffmannAirways", "matteo", connection.getFlightNumber(), connection.getFlightTimeHours(),
                airplane.getMaxCargo(), airplane.getTotalSeats(), connection.getDepartureAirport(), connection.getDestinationAirport(),
                convertToLocalDateTimeViaInstant(f.getDepartureTime()), convertToLocalDateTimeViaInstant(f.getArrivalTime()));

    }


    public boolean cancelFlight(Flight f) throws FlightException {
        FlighttransactionDTO dto = createDTO(f);
        try {
            boolean result = restServiceClient.postForObject(cancelPath, dto, boolean.class);
            successLogger.log("AirportService", "Successfully cancelled flight at ErbenAirports " + f);
            return result;

        } catch (RestClientException | NullPointerException e) {
            throw new FlightException("Could not cancel flight: Communication to airport failed!", f);
        }


    }

    public Flight editFlight(Flight oldFlight, Flight newFlight) throws FlightException {
        boolean cancelled = cancelFlight(oldFlight);
        if (cancelled) {
            return createFlight(newFlight);
        }
        return oldFlight;
    }


    //Source https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    //Source https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }


}
