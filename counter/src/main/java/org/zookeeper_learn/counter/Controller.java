package org.zookeeper_learn.counter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Controller {
    private final Counter counter;
    public Controller(Counter counter) {
        this.counter = counter;
    }
    @GetMapping("/get")
    public String get() {
        return counter.getCounterAndIncrement().toString();
    }
}
