package com.github.mnemalotebya;

import com.github.mnemalotebya.pojo.Ticket;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Calendar calendar = Calendar.getInstance();

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
                        t -> (t.getArrivalDate().getTime() - t.getDepartureDate().getTime()),
                        Long::min,
                        HashMap::new
                ));
        for (Map.Entry<String, Long> entry : workMap.entrySet()) {
            calendar.setTimeInMillis(entry.getValue());
            System.out.println(entry.getKey() + " - " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));
        }
    }

    private static double getDifferenceAVGAndMedina(List<Ticket> tickets) {
        double avg = tickets
                .stream()
                .filter(ticket -> (ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV")))
                .mapToLong(Ticket::getPrice).average().orElse(0);

        var ref = new Object() {
            int medina;
        };

        List<Ticket> sortedTickets = tickets
                .stream()
                .filter(ticket -> (ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV")))
                .sorted(Comparator.comparing(Ticket::getPrice))
                .toList();
        sortedTickets
                .forEach(ticket -> {
                    int medinaList = sortedTickets.size() / 2;
                    if (sortedTickets.size() % 2 == 0) {
                        ref.medina = Math.toIntExact((sortedTickets.get(medinaList).getPrice()) + (sortedTickets.get(medinaList + 1).getPrice()) /2);
                    } else {
                        ref.medina = Math.toIntExact((sortedTickets.get(medinaList + 1).getPrice()));
                    }
                });
        return avg - ref.medina;
    }
}