package com.example.springDemo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path =  "/test")
public class EncriptionTest {
	
	 @PostMapping("/encrypt")
	 public ResponseEntity<Object> encrypt(@RequestBody Object body) {
	        return new ResponseEntity<>(body, CREATED);
	  }
	 
	 @PostMapping("/decrypt")
	 public ResponseEntity<Object> decrypt(@RequestBody Object body) {
	        return new ResponseEntity<>(body, CREATED);
	  }
}
