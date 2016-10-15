package sample.DataBase;

import sample.Constants;

import java.sql.*;

public class DataBase implements AccountService {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private LRUCache<Integer, Long> cacheMemory;

    public DataBase() {
        connection = null;
        statement = null;
        cacheMemory = new LRUCache<>(Constants.MAX_ELEMENTS_IN_CACHE);
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
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        resultSet = databaseMetaData.getTables(null, null, "%", null);
        boolean containsRequiredTable = false;
        while (resultSet.next()) {
            if (Constants.TABLE_NAME.toLowerCase().equals(resultSet.getString(3))) {
                containsRequiredTable = true;
                break;
            }
        }
        if (containsRequiredTable) {
            System.out.println("This table has been already created");
            return;
        }
        String sql = "CREATE TABLE " + Constants.TABLE_NAME + "\n" +
                "(\n" +
                "ID INT,\n" +
                "BALANCE BIGINT\n" +
                ");";
        statement.execute(sql);
        connection.commit();
        System.out.println("Table " + Constants.TABLE_NAME + " has been successfully created");
    }

    private void addUser(Integer id, Long balance) throws SQLException {
        if (cacheMemory.containsKey(id)) {
            cacheMemory.remove(id);
        }
        cacheMemory.put(id, balance);
        String sql = "INSERT INTO AccountService (ID, BALANCE)\n" +
                "VALUES ('" + id + "','" + balance + "'" +
                ");";
        statement.execute(sql);
        connection.commit();
    }

    private void deleteUser(Integer id) throws SQLException {
        if (cacheMemory.containsKey(id)) {
            cacheMemory.remove(id);
        }
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
        if (cacheMemory.containsKey(id)) {
            return cacheMemory.get(id); // cache optimization
        }
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
                    cacheMemory.put(id, result);
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
        if (amount == 0L) {
            return;
        }
        String sql = "SELECT BALANCE FROM AccountService WHERE ID=" + id + ";";
        resultSet = statement.executeQuery(sql);
        if (resultSet == null) {
            try {
                addUser(id, amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
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
