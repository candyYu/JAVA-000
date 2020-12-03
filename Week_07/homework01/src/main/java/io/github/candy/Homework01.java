package io.github.candy;

import com.mysql.cj.jdbc.ClientPreparedStatement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.*;
import java.util.Random;

/**
 * Created by candy on 2020/12/2.
 */
public class Homework01 {


    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
//    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:33087/mydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&allowLoadLocalInfile=true&rewriteBatchedStatements=true";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "root";

    public static void main(String[] args) {
//        addOneByOne();
        addBatch();
//        addByLoadData();
    }

    public static void addOneByOne() {

        long start  = System.currentTimeMillis();

        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            StringBuilder sql = new StringBuilder();
            Random random = new Random();

            for (int i = 1; i <= 1000000; i++) {
                sql.append("insert into `order` (id,user_id,pay_price,create_time,update_time,pay_type,location_details) values(");
                sql.append(i);
                sql.append(",");
                sql.append(random.nextInt(2));
                sql.append(",");
                sql.append(50000);
                sql.append(",");
                sql.append(System.currentTimeMillis());
                sql.append(",");
                sql.append(System.currentTimeMillis());
                sql.append(",");
                sql.append(1);
                sql.append(",");
                sql.append("'发送发送的福建师范'");
                sql.append(");");
                System.out.println(sql.toString());
                stmt.executeUpdate(sql.toString());
                sql.setLength(0);
            }

            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        long end  = System.currentTimeMillis();

        System.out.println("循环插入耗时"+(end-start)/1000 + "秒");

    }


    public static void addBatch() {

        long start  = System.currentTimeMillis();

        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");

            String sql = "insert into `order` (id,user_id,pay_price,create_time,update_time,pay_type,location_details)values(?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            Random random = new Random(2);
            conn.setAutoCommit(false);

//            for(int i = 0; i <100; i++) {
                for (int j = 1; j <= 10000; j++) {
                     int index = 1;
                    statement.setLong(index++,10000 + j);
                    statement.setLong(index++, random.nextInt(2));
                    statement.setInt(index++, 50000);
                    statement.setLong(index++, System.currentTimeMillis());
                    statement.setLong(index++, System.currentTimeMillis());
                    statement.setInt(index++,2);
                    statement.setString(index++, "发送发送的福建师范");
                    statement.addBatch();

                    if((j!=0 && j%1000==0) || j==10000){//可以设置不同的大小；如50，100，200，500，1000等等
                        statement.executeBatch();
                        //优化插入第三步提交，批量插入数据库中。
                        conn.commit();
                        statement.clearBatch();//提交后，Batch清空。
                    }
                }
//            }
//            statement.executeBatch();
//                conn.commit();
            statement.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        long end  = System.currentTimeMillis();
        System.out.println("statementBatch插入耗时"+(end-start)/1000 + "秒");
    }

    public static void addByLoadData() {

        long start = System.currentTimeMillis();

        String table = "`order`";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            File file = new File("test.txt");
            FileWriter fw = new FileWriter(file);

            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i <= 10000; i++) {
                sb.append(i);
                sb.append(",");
                sb.append(random.nextInt(2));
                sb.append(",");
                sb.append(50000);
                sb.append(",");
                sb.append(System.currentTimeMillis());
                sb.append(",");
                sb.append(System.currentTimeMillis());
                sb.append(",");
                sb.append(1);
                sb.append(",");
                sb.append("'发送发送的福建师范'");
                sb.append("\n");
                fw.write(sb.toString());
                sb.setLength(0);
            }
            fw.flush();
            fw.close();
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            String sql = "load data local infile 'test.txt' IGNORE into table " + table + " fields terminated by ',' ENCLOSED BY '\"'  (`id`,user_id,pay_price,create_time,update_time,pay_type,location_details)";
            stmt = conn.prepareStatement(sql);

            ClientPreparedStatement clientStmt = stmt.unwrap(ClientPreparedStatement.class);

            clientStmt.setLocalInfileInputStream(new FileInputStream(file));

            int rets = clientStmt.executeUpdate();

            System.out.println("rets:"+rets);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        long end  = System.currentTimeMillis();
        System.out.println("loadfile插入耗时"+(end-start)/1000 + "秒");

    }
}
