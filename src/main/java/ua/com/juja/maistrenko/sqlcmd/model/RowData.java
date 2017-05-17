package ua.com.juja.maistrenko.sqlcmd.model;

import java.util.*;

/**
 * Created by maistrenko on 08.03.17.
 */
public class RowData {
    private HashMap<String, Object> columns;

    public RowData() {
        this.columns = new LinkedHashMap<>();
    }

    public void put(String name, String value) {
        columns.put(name, value);
    }

    public Set<String> getNames() {
        Set<String> result = new TreeSet<>();

        for(String column:columns.keySet()) {
            result.add(column);
        }
        return result;
    }

    public List<Object> getValues() {
        return new ArrayList<Object>(columns.values()) ;
    }

    public Object get(String column){
        return columns.get(column);
    }


}
