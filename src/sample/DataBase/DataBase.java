package sample.DataBase;

import sample.Constants;

import java.sql.*;

public class DataBase implements AccountService {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public DataBase() {
        connection = null;
        statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(Constants.dataBaseURLAddress, Constants.adminName, Constants.adminPassword);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Data base opened successfully!");
    }

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE AccountService\n" +
                "(\n" +
                "ID INT,\n" +
                "BALANCE BIGINT\n" +
                ");";
        statement.execute(sql);
        connection.commit();
    }

    private void addUser(Integer id, Long balance) throws SQLException {
        String sql = "INSERT INTO AccountService (ID, BALANCE)\n" +
                "VALUES ('" + id + "','" + balance + "'" +
                ");";
        statement.execute(sql);
        connection.commit();
    }

    private void deleteUser(Integer id) throws SQLException {
        String sql = "DELETE FROM AccountService\n" +
                "WHERE ID=" + id + ";";
        statement.execute(sql);
        // there is no need to commit now, because addUser will be called next (some kind of optimization)
    }

    public void deteteTable(String tableName) throws SQLException {
        String sql = "DROP TABLE " + tableName;
        statement.executeUpdate(sql);
        connection.commit();
    }

    public void close() throws SQLException {
        statement.close();
        connection.commit();
        connection.close();
    }

    @Override
    public Long getAmount(Integer id) throws SQLException {
        String sql = "SELECT BALANCE FROM AccountService WHERE ID=" + id + ";";
        resultSet = statement.executeQuery(sql);
        if (resultSet == null) {
            try {
                addUser(id, 0L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0L;
        } else {
            boolean called = false;
            while (resultSet.next()) {
                called = true;
                Long result = resultSet.getLong("BALANCE");
                if (result != null) {
                    return result;
                }
            }
            if (!called) {
                try {
                    addUser(id, 0L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0L;
            } else {
                throw new SQLDataException("Oops, something went wrong while reading BALANCE for the given ID");
            }
        }
    }

    @Override
    public void addAmount(Integer id, Long amount) throws SQLException {
        String sql = "SELECT BALANCE FROM AccountService WHERE ID=" + id + ";";
        resultSet = statement.executeQuery(sql);
        if (resultSet == null) {
            try {
                addUser(id, amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Long currentBalance = 0L;
            while (resultSet.next()) {
                currentBalance = resultSet.getLong("BALANCE");
            }
            if (currentBalance == null) {
                throw new SQLDataException("Oops, something went wrong while reading BALANCE for the given ID");
            }
            currentBalance += amount; // updating balance with addAmount value
            deleteUser(id);
            addUser(id, currentBalance);
        }
    }
}
