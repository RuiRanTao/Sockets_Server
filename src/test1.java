package src;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class test1 {
public static void main(String[] args) {
    Calendar now = Calendar.getInstance();
    System.out.println("年: " + now.get(Calendar.YEAR));
    System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
    System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
    System.out.println("时: " + now.get(Calendar.HOUR_OF_DAY));
    System.out.println("分: " + now.get(Calendar.MINUTE));
    System.out.println("秒: " + now.get(Calendar.SECOND));
    long timsec = now.getTimeInMillis();
    System.out.println("当前时间毫秒数：" +timsec/1000);
    System.out.println(now.getTime());

	Date dd=new Date();
	//格式化
	SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String time=sim.format(dd);
	System.out.println(time);
}
}
