package com.almatarm.qt.db;

import org.jvnet.inflector.Noun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by almatarm on 29/11/2019.
 */
public class Entity implements Serializable {
    String className;
    String table;
    ArrayList<Field> fields = new ArrayList<>();
    ArrayList<Constructor> constructors = new ArrayList<>();
    ArrayList<ForignKey> forignKeys = new ArrayList<>();

    public Entity() {}

    public Entity(String className) {
        this.className = className;
        this.table = Noun.pluralOf(className).toLowerCase();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void addField(Field field) {
        field.className = className;
        fields.add(field);
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public ArrayList<Constructor> getConstructors() {
        return constructors;
    }

    public void setConstructors(ArrayList<Constructor> constructors) {
        this.constructors = constructors;
    }

    public void addConstructor(ArrayList<Field> fields, boolean explicit) {
        constructors.add(new Constructor(this, fields, explicit));
    }

    public Field getFieldByName(String name) {
        for(Field field : fields) {
            if(field.getName().equals(name)) return field;
        }
        return null;
    }

    public void addForignKey(ForignKey forignKey) {
        forignKeys.add(forignKey);
        Field field = new Field(forignKey.getFieldName(), forignKey.key.type, true, true, forignKey.key.defaultValue);
        field.forignKey = forignKey;
        field.className = className;
        fields.add(field);
    }

    public boolean isDependent() {
        for(ForignKey fk : forignKeys)
            if(fk.dependant)
                return true;
        return false;
    }

    public ForignKey getDependentKey() {
        for(ForignKey fk : forignKeys)
            if(fk.dependant)
                return fk;
        return null;
    }

    public HashSet<String> getIncludes() {
        HashSet<String> includes = new HashSet<>();
        for (Field field : fields) {
            if(!field.isPrimitive()) {
                if (field.type.startsWith("Q")) {
                    includes.add(String.format("<%s>", field.type));
                } else {
                    includes.add(String.format("\"%s\"", field.type));
                }
            }
        }
        return includes;
    }

    public ArrayList<String> getConstructorMethods() {
        ArrayList<String> cstructors = new ArrayList<>();
        constructors.forEach(c -> {
            cstructors.add(c.getConstructorMethod());
        });
        return cstructors;
    }

    public ArrayList<String> getConstructorMethodsImpl() {
        ArrayList<String> cstructors = new ArrayList<>();
        constructors.forEach(c -> {
            cstructors.add(c.getConstructorMethodImpl());
        });
        return cstructors;
    }

    public String getWritableFields() {
        ArrayList<Field> wriableFields = new ArrayList<Field>();
        fields.forEach( f -> {
            if(f.writable) wriableFields.add(f);
        });

        if(wriableFields.isEmpty()) return "false;";

        StringBuilder buff = new StringBuilder();
        buff.append("\n");
        for(int i = 0; i < wriableFields.size(); i++) {
            Field field = wriableFields.get(i);
            buff.append("\t\t\trole == Roles::" + field.nameToRole());
            buff.append(i + 1 < wriableFields.size() ? " ||\n" : ";");
        }
        return buff.toString();
    }
}
