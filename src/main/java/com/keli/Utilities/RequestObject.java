package com.keli.Utilities;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class RequestObject {

    private Integer id;
    private String from;
    private String to;

    public RequestObject(Integer id, @NotNull String from, @NotNull String to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }
}
