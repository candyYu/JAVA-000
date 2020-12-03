package io.github.candy;

import io.github.candy.proxyDataSource.ReadOnly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by candy on 2020/12/3.
 */
@Service
@Slf4j
@ReadOnly
public class OrderService {



    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;


    public void  readFromMaster() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        String sql = "select * from `order`  order by id limit 10";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            long id = rs.getLong("id");
            long user_id = rs.getLong("user_id");
            log.info("id: {} user_id: {}", id , user_id);
        }
        statement.close();
        connection.close();
    }


    @ReadOnly
    public void readFromSlave() throws SQLException {
//        DynamicDataSourceContext.setRoutingDataSourceKey("slave");
        Connection connection = this.dataSource.getConnection();
        String sql = "select * from `order`  order by id limit 10";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            long id = rs.getLong("id");
            long user_id = rs.getLong("user_id");
            log.info("id: {} user_id: {}", id , user_id);
        }
        statement.close();
        connection.close();
    }

}
