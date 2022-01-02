package com.oth.sw.hoffmannairways.util;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("LanguageDetectionInspection")
public class Helper {

    public static String getFormattedDate(Date oldDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.format(oldDate);
    }

    //TODO producer methode, entscheidung über welche bean injiziert wird
    //Plan: zwei unterschiedliche FlightService beans, gleiches Interface:
    //FlightServiceCustomer: wie bisher, alles nur lokal
    //FlightServicePartner: beinhaltet die MessageQueue um den Partner zu benachrichtigen
    //FlightServiceFactory --> unterscheidet zwischen den beiden beans per Qualifier
    //RestController benutzt FlightServicePartner
    //Alle anderen haben beide: falls der user ein partner ist (userservice!) dann FlightService
    //cases: flight changed, flight deleted --> sobald ein partner betroffen ist, FlightServicePartner?
    //Problem auch: viel Dopplung (list all flights, connection etc.)
    //Plan2:
    //Setup Zeug? Connections, Flugzeuge, Accounts, siehe Jakob
}
