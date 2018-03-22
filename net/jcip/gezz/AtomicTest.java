package net.jcip.gezz;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gezz on 2018/3/22.
 */
public class AtomicTest {

    @Test
    public void testAtomicInteger() {
        final AtomicInteger atomicInteger = new AtomicInteger();//共享变量
        System.out.println("atomicInteger默认值：" + atomicInteger.get());
        for (int i = 0; i < 100; i++) {
            final int expect = i;
            final int update = i + 1;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(atomicInteger.addAndGet(1));
                }
            }).start();
        }
    }

}
