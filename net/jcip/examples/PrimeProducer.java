package net.jcip.examples;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.*;

/**
 * 中断取消操作
 * PrimeProducer
 * <p/>
 * Using interruption for cancellation
 *
 * @author Brian Goetz and Tim Peierls
 */
public class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;

    PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            //检查程序是否被中断
            while (!Thread.currentThread().isInterrupted()) {
                queue.put(p = p.nextProbablePrime());//可中段的阻塞方法
                System.out.println("execute once.");
            }
        } catch (InterruptedException consumed) {
            /* Allow thread to exit */
            System.out.println("中断");
        }
    }

    /**
     * 取消操作
     */
    public void cancel() {
        System.out.println("调用中断方法");
        interrupt();
    }

    public static void main(String args[]) throws InterruptedException {
        BlockingQueue<BigInteger> queue = new LinkedBlockingQueue<BigInteger>();

        PrimeProducer primeProducer = new PrimeProducer(queue);
        primeProducer.start();

        for (int i = 0; i <= 2; i ++) {
            System.out.println(i);
            Thread.sleep(10);
        }
        primeProducer.cancel();//在主线程执行完后中断取消子线程
    }
}
