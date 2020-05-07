package src;

import java.sql.*;


/**
 * 这是个非常简单的SQLite的Java程序,
 * 程序中创建数据库、创建表、然后插入数据，
 * 最后读出数据显示出来
 */
public class mesquite_user
{
    public static void main(String[] args) {
        newtable();
        insert("16014241","123456",0,0,0);
        search_all();
        switch (search_name("16014241","123456")){
            case 2:System.out.println("用户名和密码正确\n");break;
            case 1:System.out.println("用户名正确，密码错误\n");break;
            case 0:System.out.println("用户不存在\n");break;
        }
    }


    public static void newtable() { // 创建用户数据表格

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + Constants.DATABASE_NAME);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sql = "create table if not exists " + Constants.USER_TABLE_NAME
                    + "(name varchar primary key,key varchar,id integer, start integer, end integer)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public static void insert(String name, String key, Integer id, Integer start , Integer end) { // 插入用户数据
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + Constants.DATABASE_NAME);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "REPLACE INTO " + Constants.USER_TABLE_NAME + " (name,key,id,start,end) " + "VALUES (" + name + "," + key
                    + "," + id +"," + start +","+ end+");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Inserted successfully");
    }

    public static void update(String name, String key, Integer id) // 更新用户数据(////////// null //////)
    {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + Constants.DATABASE_NAME);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "UPDATE " + Constants.USER_TABLE_NAME + " set id = " + id + " where name=" + name;
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Update done successfully");
    }

    public static void search_all() { // 查询用户数据
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.DATABASE_NAME);
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from " + Constants.USER_TABLE_NAME + ";"); // 查询数据
            while (rs.next()) { // 将查询到的数据打印出来

                System.out.print("name = " + rs.getString("name") + " "); // 列属性一
                System.out.print("key = " + rs.getString("key") + " "); // 列属性二
                System.out.print("id = " + rs.getString("id") + " \n"); // 列属性三
                System.out.print("start = " + rs.getString("start") + " \n"); // 列属性三
                System.out.print("end = " + rs.getString("end") + " \n"); // 列属性三
            }
            rs.close();
            conn.close(); // 结束数据库的连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Query done successfully");
    }

    public static int search_name(String name,String key) { // 查询用户数据
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:"+Constants.DATABASE_NAME);
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from "+Constants.USER_TABLE_NAME+" where name="+name+";"); //查询数据

            if(!rs.next()){
                System.out.println("ERROR\n");
                rs.close();
                conn.close(); //结束数据库的连接
                return 0;
            }
            else{
                if(rs.getString("key").equals(key)) {
                    rs.close();
                    conn.close(); //结束数据库的连接
                    return 2; 
                }
                return 1;
            }
        }catch( Exception e )
        {
            e.printStackTrace ( );
        }
        System.out.println("Query done successfully");
        return 0;
    }


}
