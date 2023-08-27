package com.github.mnemalotebya.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String origin;
    private String originName;
    private String destination;
    private String destinationName;
    private Date departureDate;
    private Date arrivalDate;
    private String carrier;
    private Long stops;
    private Long price;
}
