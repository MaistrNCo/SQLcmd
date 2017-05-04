package ua.com.juja.sqlcmd.model;

/**
 * Created by maistrenko on 08.03.17.
 */
public class RowData {
    private Column[] columns;
    private int index;

    public RowData(int columnsCount) {
        this.columns = new Column[columnsCount];
    }

    public void addColumnValue(String name, String value) {
        columns[index++] = new Column(name, value);
    }

    public String[] getNames() {
        String[] result = new String[columns.length];
        int i = 0;
        for (Column col : columns) {
            result[i++] = col.getName();
        }
        return result;
    }

    public Object[] getValues() {
        Object[] result = new Object[columns.length];
        int i = 0;
        for (Column col : columns) {
            result[i++] = col.getValue();
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "RowData ";
        for (Column col : columns) {
            result = result.concat("\\t " + col.getName() + "\\t  " + col.getValue());
        }

        return result;
    }

    private class Column {
        private String name;
        private String value;

        public Column(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
