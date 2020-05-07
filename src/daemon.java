package src;

/** 守护线程**/

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class daemon implements Runnable {

    public List<Result> Resultlist;

    @Override  
    public void run() {  
        try {  
            while (true) {  
                Thread.sleep(1000);  
                System.out.println("#" + Thread.currentThread().getName());  
            }  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        } finally {// 后台线程不执行finally子句  
            System.out.println("finally ");  
        }  
    }  
  
    public static void main(String[] args) {  
           List<Result> Resultlist = new ArrayList<>(4);
        for ( int i = 0; i < 4; i++) {        

            // Thread daemon = new Thread(new daemon());  
            // // 必须在start之前设置为后台线程  
            // daemon.setDaemon(true);  
            // daemon.start();
            Resultlist.add(parse());
            System.out.println("finally ");

        }

        System.out.println("All daemons started");
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Result parse() {
        Result order = new Result();
        order.t = 1;
        order.f = 20;
        order.at = 1;
        order.af = 20;
        order.bt = 1;
        order.bf = 20;
        order.ct = 1;
        order.cf = 20;
        
        return order;
    }
}  
