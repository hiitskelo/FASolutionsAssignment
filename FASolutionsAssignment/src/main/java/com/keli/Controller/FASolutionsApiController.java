package com.keli.Controller;

import com.keli.Utilities.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/keli/api")
public class FASolutionsApiController {

    @GetMapping()
    public ResponseEntity<String> getResponse() {
        return ResponseEntity.ok("OK");
    }
}
