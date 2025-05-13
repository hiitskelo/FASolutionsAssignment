package com.keli.Controller;

import com.keli.Utilities.RequestObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@RestController
@RequestMapping("/keli/api")
public class FASolutionsApiController {

    @GetMapping
    public ResponseEntity<String> getResponse(@PathVariable @NotNull Integer id,
                                                     @PathVariable @NotNull LocalDate from,
                                                     @PathVariable @NotNull LocalDate to) {
        new FASolutionsApiConsumer(new RequestObject(id, from, to)).getReport();
        return ResponseEntity.ok("OK");
    }
}
