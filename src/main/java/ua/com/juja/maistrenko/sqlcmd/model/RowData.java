package ua.com.juja.maistrenko.sqlcmd.model;

import java.util.*;

public class RowData {
    private HashMap<String, Object> columns;

    public RowData() {
        this.columns = new LinkedHashMap<>();
    }

    public void put(String name, String value) {
        columns.put(name, value);
    }

    public Set<String> getNames() {
        return columns.keySet();
    }

    public List<Object> getValues() {
        return new ArrayList<>(columns.values());
    }

    public Object get(String column) {
        return columns.get(column);
    }

    public boolean isEmpty() {
        return columns.isEmpty();
    }

}
