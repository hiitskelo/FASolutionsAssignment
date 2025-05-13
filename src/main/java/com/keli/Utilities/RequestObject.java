package com.keli.Utilities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestObject {

    private Integer id;
    private LocalDate from;
    private LocalDate to;

    public RequestObject(Integer id, LocalDate from, LocalDate to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }
}
