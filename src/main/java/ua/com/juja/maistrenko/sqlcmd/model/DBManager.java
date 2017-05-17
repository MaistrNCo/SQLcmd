package ua.com.juja.maistrenko.sqlcmd.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by maistrenko on 12.03.17.
 */
public interface DBManager {

    List<RowData> selectAllFromTable(String tableName);

    int getRowCount(String tableName) throws SQLException;

    Set<String> getTablesList();

    Set<String> getColumnsNames(String tableName);

    void connect(ConnectionSettings conSettings);

    void disconnect() ;

    boolean isConnected();

    void clear(String tableName);

    void drop(String tableName);

    void create(String tableName, String[] columnNames);

    void delete(String tableName, String conditionName, String conditionValue);

    void insert(String tableName, RowData rd);

    void update(String tableName, String conditionName, String conditionValue, RowData newValue);

    void createDB(String name);

    void dropDB(String name);

}
