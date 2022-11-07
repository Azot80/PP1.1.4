package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final int COLUMN_ID_NUM = 1;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LASTNAME = "lastName";
    public static final String COLUMN_AGE = "age";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        Util util = new Util();
        try (Connection connection = util.openConnection();  Statement statement = connection.createStatement()) {
            String createUsersTableQuery = "CREATE TABLE " + TABLE_NAME +
                    "(" + COLUMN_ID + " BIGINT(19) NOT NULL AUTO_INCREMENT, " +
                    COLUMN_NAME + " VARCHAR(45) NOT NULL, " +
                    COLUMN_LASTNAME + " VARCHAR(45) NOT NULL, " +
                    COLUMN_AGE + " TINYINT(0) NOT NULL, " +
                    " PRIMARY KEY ( id ))";
            statement.executeUpdate(createUsersTableQuery);
        } catch (SQLException e) {
            System.out.println("Запрос создания таблицы не проршел: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        Util util = new Util();
        try (Connection connection = util.openConnection(); Statement statement = connection.createStatement()) {
            Savepoint savePoint = connection.setSavepoint("savePoint dropUsersTable");
            String dropUsersTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
            try {
                statement.executeUpdate(dropUsersTableQuery);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                System.out.println("Запрос удаления таблицы не проршел: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Запрос удаления таблицы не проршел: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String insertUserQuery = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + ", " +
                COLUMN_LASTNAME + ", " +
                COLUMN_AGE +
                ")" +
                "VALUES (?, ?, ?)";
        Util util = new Util();
        try (Connection connection = util.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {
            Savepoint savePoint = connection.setSavepoint("savePoint saveUser");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            try {
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                System.out.println("Запрос на создание нового user не проршел: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Запрос удаления таблицы не проршел: " + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        String deleteByUserIDQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        Util util = new Util();
        try (Connection connection = util.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(deleteByUserIDQuery)) {
            Savepoint savePoint = connection.setSavepoint("savePoint removeUserById");
            preparedStatement.setLong(COLUMN_ID_NUM, id);
            try {
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                System.out.println("Запрос удаление user не проршел: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Запрос удаления таблицы не проршел: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        String getUserQuery = "SELECT * FROM " + TABLE_NAME;
        Util util = new Util();
        try (Connection connection = util.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(getUserQuery); ResultSet result = preparedStatement.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (result.next()) {
                User user = new User();
                user.setId(result.getLong(COLUMN_ID));
                user.setName(result.getString(COLUMN_NAME));
                user.setLastName(result.getString(COLUMN_LASTNAME));
                user.setAge(result.getByte(COLUMN_AGE));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            System.out.println("Запрос не проршел: " + e.getMessage());
            return null;
        }
    }

    public void cleanUsersTable() {
        String cleaUsersTableQuery = "TRUNCATE TABLE " + TABLE_NAME;
        Util util = new Util();
        try (Connection connection = util.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(cleaUsersTableQuery)) {
            Savepoint savePoint = connection.setSavepoint("savePoint cleanUsersTable");
            try {
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                System.out.println("Запрос на очистку таблицы users не проршел: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Запрос удаления таблицы не проршел: " + e.getMessage());
        }
    }
}