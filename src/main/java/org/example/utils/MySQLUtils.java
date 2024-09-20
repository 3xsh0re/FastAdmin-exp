package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLUtils {
    public static Connection TestConnect(String ip, String port, String database, String username, String password) {
        if (port == null || port.equals(""))
            port = "3306";
        // JDBC URL格式: jdbc:mysql://<主机名>:<端口号>/<数据库名>?useSSL=false
        String url = "jdbc:mysql://"+ ip + ":" + port + "/" + database;

        // 测试连接是否成功
        try {
            // 创建连接
            Connection connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("\033[32;1m[+]" + "成功连接到目标数据库!" + "\033[0m");
                return connection;
            } else {
                System.out.println("\033[31;1m[-]" + "连接失败!" + "\033[0m");
            }
        } catch (SQLException e) {
            System.out.println("\033[31;1m[-]" + "连接失败!" + "\033[0m");
        }
        return null;
    }
}

