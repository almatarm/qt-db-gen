package com.almatarm.qt.db;

/**
 * Created by almatarm on 2019-12-02.
 */
public class SQLiteUtil {
    String tableName;
    Entity entity;

    public SQLiteUtil(String tableName, Entity entity) {
        this.tableName = tableName;
        this.entity = entity;
    }

    public String createTable() {
        StringBuilder buff = new StringBuilder();
        buff.append("\"CREATE TABLE ")
                .append(tableName)
                .append(" (\"\n");
        for (int i = 0; i < entity.getFields().size(); i++) {
            Field field = entity.fields.get(i);
            String col = field.column != null ? field.column : field.name;
            String type = field.cType != null ? field.cType : guessColumnValue(field.type);
            buff.append(String.format("\t\t\t\"    %-15s %-12s\"%n", col, type
                    + (field.primaryKey ? " PRIMARY KEY" : "")
                    + (field.autoincrement ? " AUTOINCREMENT" : "")
                    + (i + 1 < entity.getFields().size() || !entity.forignKeys.isEmpty() ? "," : "")));
        }


        for (int i = 0; i < entity.forignKeys.size(); i++) {
            ForignKey fk = entity.forignKeys.get(i);
            buff.append(String.format(
                    "\t\t\t\"    FOREIGN KEY (%s)\"%n" +
                    "\t\t\t\"        REFERENCES %s (%s)%s\"%n",
                    fk.getFieldName(), fk.getEntity().table, fk.getKey().getName(),
                    (i + 1 < entity.forignKeys.size() ? "," : "")));
        }
        buff.append("\t\t\t\")\"");
        return buff.toString();
    }

    public String insertStatement() {
        StringBuilder buff = new StringBuilder();
        buff.append("\"INSERT INTO ")
                .append(tableName)
                .append("\"\n");
        buff.append("\t\t\t\"(");
        for(int i = 0; i < entity.getFields().size(); i++) {
            Field field = entity.fields.get(i);
            if(field.autoincrement) continue;
            String col  = field.column != null ? field.column : field.name;
            buff.append(col + (i+1 < entity.getFields().size()? ", " : ")\"\n"));
        }
        buff.append("\t\t\" VALUES\"\n");
        buff.append("\t\t\t\"(");
        for(int i = 0; i < entity.getFields().size(); i++) {
            Field field = entity.fields.get(i);
            if(field.autoincrement) continue;
            String col  = field.column != null ? field.column : field.name;
            buff.append(":" + col + (i+1 < entity.getFields().size()? ", " : ")\""));
        }
        return buff.toString();
    }

    public String updateStatement() {
        StringBuilder buff = new StringBuilder();
        buff.append("\"UDPATE ")
                .append(tableName)
                .append(" SET\"\n");
        for(int i = 0; i < entity.getFields().size(); i++) {
            Field field = entity.fields.get(i);
            String col  = field.column != null ? field.column : field.name;
            System.out.println("field = " + field);
            System.out.println("col = " + col);
            if(col.toLowerCase().equals("id")) continue;
            buff.append(String.format("\t\t\t\"    %-15s = (:%s)%s\"%n", col, col
                    , (i+1 < entity.getFields().size()? "," : "")));
        }
        buff.append("\t\t\t\" WHERE id = (:id)\"");
        return buff.toString();
    }

    public String guessColumnValue(String type) {
        switch (type) {
            case "byte":
            case "short":
            case "int":
            case "long":
                return "INTEGER";

            case "float":
            case "double":
                return "REAL";

            case "bool":
                return "INTEGER";

            case "QString":
                return "TEXT";

            case "QByteArray":
                return "BLOB";

            default:
                return "TEXT";
        }

        /*
            NULL	NULL value.	NULL
            INTEGER	Signed integer, stored in 8, 16, 24, 32, 48, or 64-bits depending on the magnitude of the value.	typedef qint8/16/32/64
            REAL	64-bit floating point value.	By default mapping to QString
            TEXT	Character string (UTF-8, UTF-16BE or UTF-16-LE).	Mapped to QString
            CLOB	Character large string object	Mapped to QString
            BLOB	The value is a BLOB of data, stored exactly as it was input.	Mapped to QByteArray
         */
    }
}
