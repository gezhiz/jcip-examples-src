package net.jcip.gezz;

/**
 * Created by gezz on 2018/3/22.
 */
public class CasCounter {

    public CasCounter(SimulatedCAS value) {
        this.value = value;
    }

    private SimulatedCAS value;

    //反例
    private int wrongCount = 0;

    public int getValue() {
        return value.getValue();
    }

    public int increment() {
        int oldValue = value.getValue();
        //compareAndSwap是一个原子操作，返回最新的值
        while (value.compareAndSwap(oldValue, oldValue + 1) != oldValue) {
            //防止其他线程修改value内部的值，如果不重新设置oldValue，其他线程修改了value，会发生死锁
            oldValue = value.getValue();//这个位置有可能多个线程进入，获取到的值是最新的，如果其他线程更新了该值，也能把oldValue更新到,进入下个循环，然后把值再次更新
        }
        return oldValue + 1;
    }

    public int increment(int aValue) {
        int oldValue = value.getValue();
        while (value.compareAndSwap(oldValue,aValue + oldValue) != oldValue) {
            oldValue = value.getValue();
        }
        return oldValue + aValue;
    }

    //测试结果：最后的值均为10;而wrongCount的值最后有可能不是10
    public static final void main(String[] args) {
        final CasCounter casCounter = new CasCounter(new SimulatedCAS());
        for (int i =0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    casCounter.increment(1);
                    System.out.println(casCounter.getValue());
                }
            }).start();
        }

        for (int i =0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    casCounter.wrongCount++;
                    System.out.println("wrongCount:"+casCounter.wrongCount);
                }
            }).start();
        }
    }
}
