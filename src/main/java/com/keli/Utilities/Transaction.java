package com.keli.Utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Transaction {
    @JsonProperty("security")
    private Security security;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("unitPrice")
    private Double unitPrice;
    @JsonProperty("tradeAmount")
    private Double tradeAmount;
    @JsonProperty("typeName")
    private String typeName;
    @JsonProperty("transactionDate")
    private LocalDate transactionDate;
    @JsonProperty("settlementDate")
    private LocalDate settlementDate;
}
