package com.github.mnemalotebya;

import com.github.mnemalotebya.pojo.Ticket;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonParser {

    private static final String DATA_JSON_PATH = "src/main/resources/tickets.json";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:mm");

    public static List<Ticket> getTickets() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(
                    new InputStreamReader(new FileInputStream(DATA_JSON_PATH), StandardCharsets.UTF_8));
            JSONArray tickets = (JSONArray) jsonData.get("tickets");
            return fillTicketList(tickets);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Ticket> fillTicketList(JSONArray tickets) {
        List<Ticket> ticketList = new ArrayList<>();
        for (Object ticketJs : tickets) {
            JSONObject object = (JSONObject) ticketJs;
            Ticket ticket = new Ticket();
            ticket.setOrigin(object.get("origin").toString());
            ticket.setOriginName(object.get("origin_name").toString());
            ticket.setDestination(object.get("destination").toString());
            ticket.setDestinationName(object.get("destination_name").toString());
            ticket.setDepartureDate(getDateFromString
                    (object.get("departure_date").toString(), object.get("departure_time").toString()));
            ticket.setArrivalDate(getDateFromString
                    (object.get("arrival_date").toString(), object.get("arrival_time").toString()));
            ticket.setCarrier(object.get("carrier").toString());
            ticket.setStops((Long) object.get("stops"));
            ticket.setPrice((Long) object.get("price"));
            ticketList.add(ticket);
        }
        return ticketList;
    }

    private static Date getDateFromString(String date, String time) {
        String newDate = date + " " + time;
        try {
            return DATE_FORMAT.parse(newDate);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
