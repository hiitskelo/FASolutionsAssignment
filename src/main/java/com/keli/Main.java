package com.keli;

import com.keli.Controller.FASolutionsApiController;
import com.keli.Utilities.RequestObject;

import java.time.LocalDate;

public class Main {
    public static void main (String[] args) {
        RequestObject ro = new RequestObject(3, LocalDate.of(2022, 05, 15), LocalDate.now());
        new FASolutionsApiController().getResponse(ro.getId(), ro.getFrom(), ro.getTo());
    }
}
