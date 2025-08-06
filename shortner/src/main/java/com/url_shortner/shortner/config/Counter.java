package com.url_shortner.shortner.config;

import org.springframework.stereotype.Service;

@Service
public class Counter {
    private final Range range;

    public Counter(Range range) {
        this.range = range;
    }



    public synchronized long getCounter() {
        if(range.start>=range.end) {
            range.requestNewRange();
        }
       return this.range.start++;
    }
    public synchronized void incrementCounter() {
        if(range.start>=range.end) {
            range.requestNewRange();
        }
        this.range.start++;
    }

}
