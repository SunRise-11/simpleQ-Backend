package com.example.restservice.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.example.restservice.model.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}
