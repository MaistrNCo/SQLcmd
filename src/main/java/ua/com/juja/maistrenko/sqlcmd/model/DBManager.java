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

    void clear(String tableName);

    void drop(String tableName);

    void create(String tableName, List<String> columnNames);

    void delete(String tableName, RowData rowData);

    void insert(String tableName, RowData rowData);

    void update(String tableName, RowData condition, RowData newValue);

    void createDB(String name);

    void dropDB(String name);

}
