package net.jcip.examples;

import java.util.concurrent.*;

/**
 * CellularAutomata
 *
 * Coordinating computation in a cellular automaton with CyclicBarrier
 *
 * @author Brian Goetz and Tim Peierls
 */
public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;//关卡
    private final Worker[] workers;

    public CellularAutomata(Board board) {
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();//可使用线程的数量
        this.barrier = new CyclicBarrier(count,
                //关卡行为，当所有线程通过关卡时，会在一个子任务中执行关卡行为
                new Runnable() {
                    public void run() {
                        mainBoard.commitNewValues();//,在所有工作线程都执行完成之后，提交计算结果给数据模型
                    }});
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++)
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
    }

    private class Worker implements Runnable {
        private final Board board;

        public Worker(Board board) { this.board = board; }
        public void run() {
            while (!board.hasConverged()) {
                //耗时操作
                for (int x = 0; x < board.getMaxX(); x++)
                    for (int y = 0; y < board.getMaxY(); y++)
                        board.setNewValue(x, y, computeValue(x, y));
                try {
                    //该方法会一直阻塞知道所有的线程到达关卡点
                    barrier.await();//该方法会为每一个线程返回到达索引号
                } catch (InterruptedException ex) {
                    return;
                } catch (BrokenBarrierException ex) {
                    return;
                }
            }
        }

        private int computeValue(int x, int y) {
            // Compute the new value that goes in (x,y)
            return 0;
        }
    }

    public void start() {
        for (int i = 0; i < workers.length; i++)
            new Thread(workers[i]).start();
        mainBoard.waitForConvergence();
    }

    interface Board {
        int getMaxX();
        int getMaxY();
        int getValue(int x, int y);
        int setNewValue(int x, int y, int value);
        void commitNewValues();
        boolean hasConverged();
        void waitForConvergence();
        Board getSubBoard(int numPartitions, int index);
    }
}
