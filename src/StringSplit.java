package src;

import java.util.concurrent.TimeUnit;

public class StringSplit {
    public static void main(String[] args) {
    //一般用法     
       for (int i = 0; i < 3; i++) {  
            Thread daemon = new Thread(new daemon());  
            // 必须在start之前设置为后台线程  
            daemon.setDaemon(true);  
            daemon.start();
        }  
        System.out.println("All daemons started");  

        String sourceStr = "1,2,3,4,5，yjf,hj,回家";
        String[] sourceStrArray = sourceStr.split(",");
        for (int i = 0; i < sourceStrArray.length; i++) {
            System.out.println(sourceStrArray[i]);
        }

        // 限定最多分割出3个字符串
        int maxSplit = 5;
        sourceStrArray = sourceStr.split(",", maxSplit);
        for (int i = 0; i < sourceStrArray.length; i++) {
            System.out.println(sourceStrArray[i]);
        }
   
        try {  
            TimeUnit.MILLISECONDS.sleep(6000);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }
    }
}