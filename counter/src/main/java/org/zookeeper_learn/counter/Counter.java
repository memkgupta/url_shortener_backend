package org.zookeeper_learn.counter;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
@Component
public class Counter {
    private BigInteger counter;
    public Counter() {
        counter = BigInteger.ZERO;
    }
    public synchronized BigInteger getCounterAndIncrement(){
        BigInteger value = counter;
        counter = counter.add(new BigInteger("100000"));
        return counter;
    }
}
