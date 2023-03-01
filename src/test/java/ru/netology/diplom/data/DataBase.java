package ru.netology.diplom.data;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    private static String url = System.getProperty("db.url");
    private static String user = System.getProperty("db.user");
    private static String password = System.getProperty("db.password");

    @SneakyThrows
    public static void clearTables() {
        QueryRunner runner = new QueryRunner();
        String deleteCredit = "DELETE FROM credit_request_entity";
        String deletePayment = "DELETE FROM payment_entity";
        String deleteOrder = "DELETE FROM order_entity";

        Connection connection = DriverManager.getConnection(url, user, password);
        runner.update(connection, deleteCredit);
        runner.update(connection, deletePayment);
        runner.update(connection, deleteOrder);
    }

    @SneakyThrows
    public static String getStatus(String statusSQL) {
        QueryRunner runner = new QueryRunner();
        Connection connection = DriverManager.getConnection(
                url, user, password);
        String result = runner.query(connection, statusSQL, new ScalarHandler<>());
        return result;
    }

    @SneakyThrows
    public static String getStatusPayment() {
        String statusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        return getStatus(statusSQL);
    }

    @SneakyThrows
    public static String getStatusCredit() {
        String statusSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return getStatus(statusSQL);
    }

    public static String countRecords() throws SQLException {
        val countSQL = "SELECT COUNT(*) FROM order_entity";
        val runner = new QueryRunner();
        Long count = null;
        val conn = DriverManager.getConnection(
                url, user, password);
        count = runner.query(conn, countSQL, new ScalarHandler<>());
        return Long.toString(count);
    }
}