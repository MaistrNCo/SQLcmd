package ua.com.juja.maistrenko.sqlcmd.model;

import java.util.List;
import java.util.Set;

public interface DBManager {

    List<RowData> selectAllFromTable(String tableName);

    Set<String> getTablesList();

    Set<String> getColumnsNames(String tableName);

    String getPropertiesPath();

    void connect(ConnectionSettings conSettings);

    void disconnect();

    boolean isConnected();

    int clear(String tableName);

    void drop(String tableName);

    void create(String tableName, List<String> columnNames);

    int delete(String tableName, RowData rowData);

    int insert(String tableName, RowData rowData);

    int update(String tableName, RowData condition, RowData newValue);

    void createDB(String name);

    void dropDB(String name);

}
