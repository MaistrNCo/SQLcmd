package ua.com.juja.maistrenko.sqlcmd.model;

import java.util.List;
import java.util.Set;

public interface DBManager {

    List<RowData> selectAllFromTable(String tableName);

    Set<String> getTablesList();

    Set<String> getColumnsNames(String tableName);

    void connect(ConnectionSettings conSettings);

    void disconnect() ;

    boolean isConnected();

    void clear(String tableName);

    void drop(String tableName);

    void create(String tableName, List<String> columnNames);

    void delete(String tableName, String conditionName, String conditionValue);

    void insert(String tableName, RowData rd);

    void update(String tableName, String conditionName, String conditionValue, RowData newValue);

    void createDB(String name);

    void dropDB(String name);

}
