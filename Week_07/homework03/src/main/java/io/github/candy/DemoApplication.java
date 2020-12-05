package io.github.candy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by candy on 2020/12/5.
 */
public class DemoApplication {

    public static void main(String[] args) {
        try {
            DataSource dataSource = YamlDataSourceFactory.newInstance();
            readFromSlave(dataSource);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void readFromSlave(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        String sql = "select * from `order`  order by id limit 10";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            long id = rs.getLong("id");
            long user_id = rs.getLong("user_id");
            System.out.println("id: "+id+" user_id: "+ user_id);
        }
        statement.close();
        connection.close();
    }
}
