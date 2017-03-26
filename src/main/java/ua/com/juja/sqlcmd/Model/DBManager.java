package ua.com.juja.sqlcmd.Model;

import java.sql.SQLException;

/**
 * Created by maistrenko on 12.03.17.
 */
public interface DBManager {
    RowData[] selectAllFromTable(String tableName);

    int getRowCount(String tableName) throws SQLException;

    String[] getTablesList();

    String[] getColumnsNames(String tableName);

    void connectDefault(String settingsFileName);

    void connect(ConnectionSettings conSettings);

    boolean isConnected();

    void clear(String tableName);

    void drop(String tableName);

    void create(String tableName, String[] columnNames );

    void delete(String tableName, String conditionName, String conditionValue);

    void insert(String tableName, RowData rd);

    void update(String tableName, String conditionName, String conditionValue, RowData newValue);

    void updatePrepared(String tableName, String conditionName, String conditionValue, RowData newValue);
}
