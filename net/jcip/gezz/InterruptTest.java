package net.jcip.gezz;

/**
 * 可取消的任务
 * 取消操作情形：1、用户手动取消；2、超时取消；3、其他线程已经解决了当前线程正在解决的问题；4、程序出错；5、进程关闭
 * Created by gezz on 2018/3/21.
 */
public class InterruptTest {


    class FileReader implements Runnable {

        private volatile boolean cancelled;

        @Override
        public void run() {
            try {
                readFile();
            } catch (InterruptedException e) {
                //中断处理
                existed();
            } finally {

            }
        }

        public void cancel() {
            cancelled = true;
        }

        public void checkCancelled() throws InterruptedException {
            if (cancelled || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        }

        //在每一个耗时操作前检查任务是否已取消
        private void readFile() throws InterruptedException {
            System.out.println("Start read file.");
            checkCancelled();
            Thread.sleep(1000);//耗时操作1
            checkCancelled();
            Thread.sleep(1000);//耗时操作2
            checkCancelled();
            Thread.sleep(1000);//耗时操作3
            checkCancelled();
            Thread.sleep(1000);//耗时操作4
            System.out.println("Read file success.");
        }

        private void existed() {
            System.out.print("Read Completed");
        }
    }

}
