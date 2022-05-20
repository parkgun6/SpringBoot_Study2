package org.geon.springbootstudy.sample.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class SampleController {

    @GetMapping("/")
    public ResponseEntity<String> sampleGet() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }
}
