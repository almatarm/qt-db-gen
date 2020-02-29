package com.almatarm.qt.db;

import java.util.ArrayList;

/**
 * Created by almatarm on 05/12/2019.
 */
public class Constructor {
    Entity entity;
    ArrayList<Field> fields;
    boolean explicit = false;
    String callOtherConstuctor = null;

    public Constructor(ArrayList<Field> fields, boolean explicit) {
        this.fields = fields;
        this.explicit = explicit;
    }

    public Constructor(Entity entity, ArrayList<Field> fields, boolean explicit) {
        this.entity = entity;
        this.fields = fields;
        this.explicit = explicit;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public String getConstructorMethod() {
        StringBuilder params = new StringBuilder();
        fields.forEach(f -> {
            params.append(String.format("%s %s%s, ",
                    f.isPrimitive()? f.type : "const " + f.type + "&",
                    f.name,
                    f.defaultValue != null? " = " + f.defaultValue : ""));
        });
        return String.format("%s%s(%s);",
            explicit? "explicit " : "", entity.getClassName(), params.length() > 0 ? params.substring(0, params.length() -2) : "");
    }

    public String getConstructorMethodImpl() {
        String className = entity.className;
        StringBuilder buff = new StringBuilder();
        StringBuilder params = new StringBuilder();
        fields.forEach(f -> {
            params.append(String.format("%s %s, ",
                    f.isPrimitive()? f.type : "const " + f.type + "&",
                    f.name));
        });
        buff.append(String.format("%s::%s::%s(%s) :%n",
                Config.namespace, className,
                className, params.length() > 0 ? params.substring(0, params.length() -2) : ""));

        if(callOtherConstuctor != null) {
            buff.append("\t" + callOtherConstuctor);
        } else {
            for(Field field : entity.fields) {
                if (fields.contains(field)) {
                    buff.append(String.format("\t%s_(%s),\n", field.name, field.name));
                } else if (field.defaultValue != null) {
                    buff.append(String.format("\t%s_(%s),\n", field.name, field.defaultValue));
                } else {
                    buff.append(String.format("\t%s_(%s),\n", field.name, field.guessDefaultValue()));
                }
            }
            buff.delete(buff.length() -2, buff.length());
        }
        buff.append(" {\n\n}\n");
        return buff.toString();
    }


}
