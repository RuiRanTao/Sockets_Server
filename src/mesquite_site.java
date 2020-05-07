package src;

import java.sql.*;


/**
 * 这是个非常简单的SQLite的Java程序,
 * 程序中创建数据库、创建表、然后插入数据，
 * 最后读出数据显示出来
 */
public class mesquite_site
{
    public static void main(String[] args) {
        newtable();
        // insert(10,2,"jk",3,4);
        search();
    }


    public static void newtable() {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sql = "create table if not exists " + Constants.SITE_TABLE_NAME + "(ID integer primary key,floor integer,place varchar,num integer,val integer, order_val integer, start integer, end integer)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public static  void insert(Integer id,Integer floor,String place,Integer num,Integer val){
            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");
                stmt = c.createStatement();
                String sql = "INSERT INTO "+ Constants.SITE_TABLE_NAME+" (ID,floor,place,num,val,order_val,start,end) " +
                "VALUES ("+id +","+ floor +","+ "\""+ place +"\"" +","+ num +","+ val  + ","+ 0 + ","+ 0 +","+ 0 +");";
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close();
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println("Inserted successfully");
    }


    public static void update_val(Integer id,Integer floor,String place,Integer num,Integer val)  //更新座位状态
    {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "UPDATE "+Constants.SITE_TABLE_NAME+" set val = "+val+" where ID="+id;
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Update done successfully");
    }

    public static void update_order_val(Integer id,Integer order_val)  //更新座位状态
    {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "UPDATE "+Constants.SITE_TABLE_NAME+" set order_val = "+order_val+" where ID="+id;
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Update done successfully");
    }

    public static void update_order_time(Integer id,long time1, long time2)  //更新座位状态
    {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "UPDATE "+Constants.SITE_TABLE_NAME+" set start = "+time1+","+"end = " +time2+ " where ID="+id;
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Update done successfully");
    }

    public static void search(){
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from "+Constants.SITE_TABLE_NAME+";"); //查询数据
            while (rs.next()) { //将查询到的数据打印出来

                System.out.print("id = " + rs.getString("ID") + " "); //列属性一
                System.out.print("区域 = " + rs.getString("floor") + " "); //列属性二
                System.out.print("座位号 = " + rs.getString("place") + " "); //列属性三
                System.out.print("座位号 = " + rs.getString("num") + " "); //列属性三
                System.out.println("座位号 = " + rs.getString("val") + " "); //列属性三
                System.out.print("start = " + rs.getString("start") + " \n"); // 列属性三
                System.out.print("end = " + rs.getString("end") + " \n"); // 列属性三
            }
            rs.close();
            conn.close(); //结束数据库的连接
        }catch( Exception e )
        {
            e.printStackTrace ( );
        }
        System.out.println("Query done successfully");
    }
}
