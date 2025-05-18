package com.keli.Controller;

import com.keli.Utilities.AuthProperties;
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

    private final AuthProperties authProperties;

    public FASolutionsApiController(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @GetMapping("/report/{id}/{from}/{to}")
    public ResponseEntity<String> getResponse(@PathVariable @NotNull Integer id,
                                                     @PathVariable @NotNull String from,
                                                     @PathVariable @NotNull String to) {
        return ResponseEntity.ok(new FASolutionsApiConsumer(new RequestObject(id, from, to), authProperties).getReport());
    }
}
