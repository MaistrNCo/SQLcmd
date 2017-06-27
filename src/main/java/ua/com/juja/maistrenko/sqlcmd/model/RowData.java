package ua.com.juja.maistrenko.sqlcmd.model;

import java.util.*;

public class RowData {
    private final HashMap<String, Object> columns;

    @Override
    public String toString() {
        if (columns.size() == 0) return "";
        StringBuilder result = new StringBuilder("[");
        for (Object value : columns.values()) {
            result.append(value).append(", ");
        }
        return result.substring(0, result.length() - 2) + "]";
    }

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
