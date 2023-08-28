package com.github.mnemalotebya;

import com.github.mnemalotebya.pojo.Ticket;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Ticket> tickets = JsonParser.getTickets();
        getMinFlightTimeForEachCarrier(tickets);
        System.out.println(getDifferenceAVGAndMedina(tickets));
    }

    private static void getMinFlightTimeForEachCarrier(List<Ticket> tickets) {
        Map<String, Long> workMap = tickets
                .stream()
                .filter(ticket -> (ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV")))
                .collect(Collectors.toMap(
                        Ticket::getCarrier,
                        t -> (t.getDepartureDate().toInstant().until(t.getArrivalDate().toInstant(), ChronoUnit.MINUTES)),
                        Long::min,
                        HashMap::new
                ));
        for (Map.Entry<String, Long> entry : workMap.entrySet()) {
            long hours = entry.getValue() / 60;
            long minutes = entry.getValue() % 60;
            System.out.println(entry.getKey() + " - " + hours + " часов " + minutes + " минут");
        }
    }

    private static double getDifferenceAVGAndMedina(List<Ticket> tickets) {
        double avg = tickets
                .stream()
                .filter(ticket -> (ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV")))
                .mapToLong(Ticket::getPrice).average().orElse(0);

        List<Ticket> sortedTickets = tickets
                .stream()
                .filter(ticket -> (ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV")))
                .sorted(Comparator.comparing(Ticket::getPrice))
                .toList();

        int medina;
        int medinaList = sortedTickets.size() / 2;
        if (sortedTickets.size() % 2 == 0) {
            medina = (sortedTickets.get(medinaList - 1).getPrice().intValue() + sortedTickets.get(medinaList).getPrice().intValue()) / 2;
        } else {
            medina = sortedTickets.get(medinaList).getPrice().intValue();
        }
        return avg - medina;
    }
}