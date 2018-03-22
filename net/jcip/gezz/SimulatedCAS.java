package net.jcip.gezz;

/**
 * Created by gezz on 2018/3/22.
 */
public class SimulatedCAS {
    private volatile int value;

    public synchronized int getValue() { return value; }

    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (value == expectedValue) {
            value = newValue;
            System.out.println("updated:" + newValue);
        }
        return oldValue;
    }

}
