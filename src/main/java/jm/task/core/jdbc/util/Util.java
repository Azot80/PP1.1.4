package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    public static final String DB_NAME = "bd_task1.1.4";
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/";
    public static final String UNICODETIMEZONE_STRING = "?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final static String URL = CONNECTION_STRING + DB_NAME + UNICODETIMEZONE_STRING;
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";

    public Connection openConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Нет связи с БД: " + DB_NAME + e.getMessage());
        }
        return null;
    }
}


