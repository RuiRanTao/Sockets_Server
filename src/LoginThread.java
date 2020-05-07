package src;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.Calendar;

public class LoginThread extends Thread {

    private Socket socket=null;
    private String Skey ="query_ok,";
    private Integer key;

    public LoginThread(Socket socket) {
        super();
        this.socket = socket;
    }
    
    /*
     * 处理用户的请求
     *
     */
    public void run(){
        OutputStream outputStream=null;
        BufferedReader bufferedReader=null;
        InputStreamReader inputStreamReader=null;

        //InputStream inputStream= null;//得到一个输入流，接收客户端传递的信息
        try {
            inputStreamReader=new InputStreamReader(socket.getInputStream());//提高效率，将自己字节流转为字符流
            bufferedReader=new BufferedReader(inputStreamReader);//加入缓冲区
            String temp=null;
            // String info="";
            while((temp=bufferedReader.readLine())!=null){
                // info+="\r\n";
                // info+=temp;
                System.out.print("已接收到客户端连接  ");
                // System.out.println(temp);
                String[] sourceStrArray = temp.split(",");
                int len = sourceStrArray.length;
                if(len==5){                                             //查询请求和单片机请求
                    Site.ID = Integer.parseInt(sourceStrArray[0]) ;
                    Site.floor = Integer.parseInt(sourceStrArray[1]) ;
                    Site.place = Integer.parseInt(sourceStrArray[2]) ;
                    switch(Site.place){
                        case 1:Site.region ="A";
                        break;
                        case 2:Site.region ="B";
                        break;
                        case 3:Site.region ="C";
                        break;
                    }
                    Site.num = Integer.parseInt(sourceStrArray[3]) ;
                    Site.val = Integer.parseInt(sourceStrArray[4]) ;  

                    if(Site.ID==10000*Site.floor+1000*Site.place+Site.num){
                        //   mesquite_site.insert(Site.ID, Site.floor, Site.region, Site.num, Site.val);
                            mesquite_site.update_val(Site.ID, Site.floor, Site.region, Site.num, Site.val);
                            Connection conn = null;
                            Statement stat = null;
                            ResultSet rs = null;
                            String back=null;
                            try {
                                conn = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
                                stat = conn.createStatement();
                                rs = stat.executeQuery("select * from "+Constants.SITE_TABLE_NAME+" where ID="+Site.ID+";"); //查询数据
                                while (rs.next()) { //将查询到的数据打印出来
                                int time1 = 10*Integer.parseInt(rs.getString("ID"));
                                int time2 = Integer.parseInt(rs.getString("val"));
                                Calendar now = Calendar.getInstance();
                                long timsec = now.getTimeInMillis()/1000;
                                if(time1<timsec && timsec<time2){
                                    back = "ordered";
                                }else{
                                    back = "empty";
                                }
                                }
                                rs.close();
                                conn.close(); //结束数据库的连接
                            }catch( Exception e )
                            {
                                e.printStackTrace ( );
                            }  
                            PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                            printWriter.println(back); 
                            printWriter.flush();
                    }
                    else if((Site.ID==1)&&(Site.floor==1)&&(Site.place==1)&&(Site.num==1)&&(Site.val==1)){
                        Connection conn = null;
                        Statement stat = null;
                        ResultSet rs = null;
                        try {
                            conn = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
                            stat = conn.createStatement();
                            rs = stat.executeQuery("select * from "+Constants.SITE_TABLE_NAME+";"); //查询数据
                            while (rs.next()) { //将查询到的数据打印出来
                            key=10*Integer.parseInt(rs.getString("ID"))+Integer.parseInt(rs.getString("val"));
                            Skey+=Integer.toString(key);
                            Skey+=",";
                            }
                            rs.close();
                            conn.close(); //结束数据库的连接
                        }catch( Exception e )
                        {
                            e.printStackTrace ( );
                        }  
                        outputStream=socket.getOutputStream();//获取一个输出流，向服务端发送信息
                        PrintWriter printWriter=new PrintWriter(outputStream);//将输出流包装成打印流
                        printWriter.println(Skey); 
                        printWriter.flush();
                        System.out.print("普通查询成功  ");
                    }
                }  

                if( sourceStrArray[0] .equals("query_user") ) {
                    Connection conn = null;
                    Statement stat = null;
                    ResultSet rs = null;
                    Integer id=0,start=0,end=0;
                    String name = sourceStrArray[1] ;              
                    conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.DATABASE_NAME);
                    stat = conn.createStatement();
                    rs = stat.executeQuery(
                        "select * from " + Constants.USER_TABLE_NAME + " where name="+ name +";");
                    while (rs.next()) { // 将查询到的数据打印出来
                        id = Integer.parseInt(rs.getString("id"));
                        start = Integer.parseInt(rs.getString("start"));
                        end = Integer.parseInt(rs.getString("end"));
                    }
                    conn.close(); //结束数据库的连接
                    PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                    printWriter.println("query_user_ok" +"," +  id + "," + start +"," +  end); 
                    printWriter.flush();
                    System.out.print("用户信息获取成功  " + id +start + end);
                }

                if( sourceStrArray[0] .equals("query_order") ) {
                    Connection conn = null;
                    Statement stat = null;
                    ResultSet rs = null;
                    try {
                        conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.DATABASE_NAME);
                        stat = conn.createStatement();
                        rs = stat.executeQuery("select * from " + Constants.SITE_TABLE_NAME + " where (start>="
                                + sourceStrArray[1] + " and start<=" + sourceStrArray[2] + ") or (end>="
                                + sourceStrArray[1] + " and end<=" + sourceStrArray[2] + ");"); // 查询数据
                        while (rs.next()) { // 将查询到的数据打印出来
                            Integer id = Integer.parseInt(rs.getString("ID"));
                            stat.executeUpdate(
                                    "UPDATE " + Constants.SITE_TABLE_NAME + " set order_val = 1 where ID=" + id + ";");
                        }
                        rs = stat.executeQuery("select * from "+Constants.SITE_TABLE_NAME+";"); //查询数据
                        while (rs.next()) { //将查询到的数据打印出来
                        key=10*Integer.parseInt(rs.getString("ID"))+Integer.parseInt(rs.getString("order_val"));
                        Skey+=Integer.toString(key);
                        Skey+=",";
                        }
                        rs = stat.executeQuery("select * from " + Constants.SITE_TABLE_NAME + " where (start>="
                        + sourceStrArray[1] + " and start<=" + sourceStrArray[2] + ") or (end>="
                        + sourceStrArray[1] + " and end<=" + sourceStrArray[2] + ");"); // 查询数据
                        while (rs.next()) { // 将查询到的数据打印出来
                            Integer id = Integer.parseInt(rs.getString("ID"));
                            stat.executeUpdate(
                                    "UPDATE " + Constants.SITE_TABLE_NAME + " set order_val = 0 where ID=" + id + ";");
                        }
                        rs.close();
                        conn.close(); //结束数据库的连接
                    }catch( Exception e )
                    {
                        e.printStackTrace ( );
                    }  
                    PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                    printWriter.println(Skey); 
                    printWriter.flush();
                    System.out.print("预约查询成功  ");
                }

                if( sourceStrArray[0] .equals("order") ) {
                    Connection conn = null;
                    Statement stat = null;
                    ResultSet rs = null;
                    Integer id=0;
                    Site.floor = Integer.parseInt(sourceStrArray[1]) ;
                    Site.place = Integer.parseInt(sourceStrArray[2]) ;
                    Site.num = Integer.parseInt(sourceStrArray[3]) ;
                    Site.ID = 10000*Site.floor+1000*Site.place+Site.num ;
                    long time1 = Integer.parseInt(sourceStrArray[4]) ;
                    long time2 = Integer.parseInt(sourceStrArray[5]) ;  
                    String name = sourceStrArray[6] ;

                    conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.DATABASE_NAME);
                    stat = conn.createStatement();
                    rs = stat.executeQuery(
                        "select * from " + Constants.USER_TABLE_NAME + " where name="+ name +";");
                    while (rs.next()) { // 将查询到的数据打印出来
                        id = Integer.parseInt(rs.getString("id"));
                    }
                    rs.close();
                    mesquite_site.update_order_time(id, 0, 0);

                    stat.executeUpdate(
                        "UPDATE " + Constants.USER_TABLE_NAME + " set id = "+Site.ID+" , start ="+ time1 + ", end =" +time2+ " where name=" + name + ";");
                    conn.close(); //结束数据库的连接

                    mesquite_site.update_order_time(Site.ID, time1, time2);
                    
                    PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                    printWriter.println("order_ok"); 
                    printWriter.flush();
                    System.out.print("预约成功  ");
                }

                if( sourceStrArray[0] .equals("login") ) {
                    System.out.println("服务端接收到客户端信息："+temp+",当前客户端ip为："+socket.getInetAddress().getHostAddress());
                    switch (mesquite_user.search_name(sourceStrArray[1],sourceStrArray[2])){
                        case 2:{
                            System.out.println("用户名和密码正确\n");
                            Connection conn = null;
                            Statement stat = null;
                            ResultSet rs = null;
                            try {
                                conn = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
                                stat = conn.createStatement();
                                rs = stat.executeQuery("select * from "+Constants.USER_TABLE_NAME+" where name="+ sourceStrArray[1] +";"); //查询数据
                                if(rs.next()){
                                    System.out.println("查询成功\n");   
                                    PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                                    printWriter.println("login_ok,"+rs.getString("name")+ "," +rs.getString("key")+ "," +rs.getString("id")); 
                                    printWriter.flush();
                                }
                                rs.close();
                                conn.close(); //结束数据库的连接
                            }catch( Exception e )
                            {
                                e.printStackTrace ( );
                            }  
                        }break;
                        case 1:{
                            System.out.println("用户名正确，密码错误\n");
                            System.out.println("查询成功\n");   
                            PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                            printWriter.println("login_noke,name_ok,key_error"); 
                            printWriter.flush();
                        }break;
                        case 0:{
                            System.out.println("用户不存在\n");
                            System.out.println("查询成功\n");   
                            PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                            printWriter.println("login_no,name_err"); 
                            printWriter.flush();
                        }break;
                    }
                }
                if( sourceStrArray[0] .equals("register") ) {
                    switch(mesquite_user.search_name(sourceStrArray[1],sourceStrArray[2])){   //用户名不存在
                            case 2:{
                                System.out.println("用户名已存在\n");
                                System.out.println("查询成功\n");   
                                PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                                printWriter.println("register_err"); 
                                printWriter.flush();
                            }break;
                            case 1:{
                                System.out.println("用户名已存在\n");
                                System.out.println("查询成功\n");   
                                PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                                printWriter.println("register_err"); 
                                printWriter.flush();
                            }break;
                            case 0:{
                                mesquite_user.insert(sourceStrArray[1], sourceStrArray[2] , 0 ,0 ,0);
                                System.out.println("注册成功\n");
                                System.out.println("查询成功\n");   
                                PrintWriter printWriter=new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
                                printWriter.println("register_ok"); 
                                printWriter.flush();
                            }break;
                    }
                }
            }

//            outputStream=socket.getOutputStream();//获取一个输出流，向服务端发送信息
//            PrintWriter printWriter=new PrintWriter(outputStream);//将输出流包装成打印流
//            printWriter.print("服务端已接收到您的信息");
//            printWriter.flush();
            socket.shutdownOutput();//关闭输出流
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            //关闭相对应的资源
            //printWriter.close();
            try {
                if(outputStream!=null)
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(bufferedReader!=null)
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //inputStream.close();
            try {
                if(socket!=null)
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
