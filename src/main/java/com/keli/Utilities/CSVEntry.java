package com.keli.Utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CSVEntry {
    @JsonProperty("shortName")
    private String shortName;
    @JsonProperty("transactions")
    private List<Transaction> transactions;

}
