package com.ktar.dragonbot;

import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Database {
    protected Connection connection;

    protected Database() {
        this.connection = null;
    }

    /**
     * Checks if a connection is open with the database
     *
     * @return true if the connection is open
     * @throws SQLException if the connection cannot be checked
     */
    public boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    /**
     * Gets the connection with the database
     *
     * @return Connection with the database, null if none
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Opens a connection with the database
     *
     * @return Opened connection
     * @throws SQLException if the connection can not be opened
     */
    public Connection openConnection() throws SQLException {
        if (checkConnection()) {
            return connection;
        }
        File file = new File("database.db");
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Logger.error("Unable to create database!");
            }
        }
//        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * Closes the connection with the database
     *
     * @return true if successful
     * @throws SQLException if the connection cannot be closed
     */
    public boolean closeConnection() throws SQLException {
        if (connection == null) {
            return false;
        }
        connection.close();
        return true;
    }

    /**
     * Executes a SQL Query<br>
     * <p>
     * If the connection is closed, it will be opened
     *
     * @param query Query to be run
     * @return the results of the query
     * @throws SQLException If the query cannot be executed
     */
    public ResultSet querySQL(String query) throws SQLException {
        if (!checkConnection()) {
            openConnection();
        }

        Statement statement = connection.createStatement();

        ResultSet ResultSet = statement.executeQuery(query);

        connection.commit();

        return ResultSet;
    }

    /**
     * Executes an Update SQL Query<br>
     * See {@link Statement#executeUpdate(String)}<br>
     * If the connection is closed, it will be opened
     *
     * @param query Query to be run
     * @return Result Code, see {@link Statement#executeUpdate(String)}
     * @throws SQLException If the query cannot be executed
     */
    public int updateSQL(String query) throws SQLException {
        if (!checkConnection()) {
            openConnection();
        }

        Statement statement = connection.createStatement();

        int ResultCode = statement.executeUpdate(query);

        connection.commit();

        return ResultCode;
    }

    /**
     * Executes a Batch SQL Query<br>
     * Batch SQL Queries are a more efficient way of<br>
     * sending multiple SQL statements at a time.<br>
     * See {@link Statement#executeBatch}<br>
     * If the connection is closed, it will be opened
     *
     * @param stmts The statements to be executed, stored in an array
     * @return Result Code, see {@link Statement#executeBatch()}
     * @throws SQLException If the query cannot be executed
     */
    public int[] sendBatchStatement(String[] stmts) throws SQLException {
        if (!checkConnection()) {
            openConnection();
        }

        Statement statement = connection.createStatement();

        for (String state : stmts) {
            statement.addBatch(state);
        }

        int[] ResultCodes = statement.executeBatch();

        connection.commit();

        return ResultCodes;
    }


}
