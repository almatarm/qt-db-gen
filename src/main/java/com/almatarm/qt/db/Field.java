package com.almatarm.qt.db;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by almatarm on 29/11/2019.
 */
public class Field implements Serializable{
    enum Role {
        display_role("Qt::DisplayRole");

        String role;

        Role(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }

        public static Role toRole(String r) {
            switch (r) {
                case "display_role":
                    return display_role;
            }
            return null;
        }
    }
    String name;
    String type = "QString";
    boolean getter = true;
    boolean setter = true;
    String defaultValue = null;
    String className = null;
    String column;
    String cType;
    boolean primaryKey = false;
    boolean autoincrement = false;
    boolean writable = true;
    ForignKey forignKey;
    ArrayList<String> roles = new ArrayList<>();

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Field(String name, String type, String defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public Field(String name, String type, boolean getter, boolean setter) {
        this(name, type, getter, setter, null);
    }

    public Field(String name, String type, boolean getter, boolean setter, String defaultValue) {
        this.name = name;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        this.defaultValue = defaultValue;
    }

    public String guessDefaultValue() {
        switch (type) {
            case "byte":
            case "short":
            case "int":
            case "long":
                return "0";

            case "float":
            case "double":
                return "0.0";

            case "bool":
                return "false";

            default:
                return "nullptr";
        }
    }

    public String qVariantConvertor(String entity) {
        String col  = column != null ? column : name;
        String tp = qVariantTo();
        return String.format("%s->set%s(query.value(\"%s\").to%s());",
                entity, Util.capitalizeFirstLetter(name), col, tp);
    }

    public String qVariantTo() {
        String tp   = type.startsWith("Q") ? type.substring(1) : type;
        switch (type) {
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "bool":
                tp = Util.capitalizeFirstLetter(type);
                break;
        }
        return tp;
    }

    public boolean isPrimitive() {
        return type.equals("byte") ||
                type.equals("short") ||
                type.equals("int") ||
                type.equals("long") ||
                type.equals("float") ||
                type.equals("double") ||
                type.equals("bool");
    }

    public String getGetterMethod() {
        return String.format("%s %s() const;", type, name);
    }

    public String getSetterMethod() {
        return String.format("void set%s(%s %s);",
                Util.capitalizeFirstLetter(name),
                isPrimitive()? type : "const " + type + "&",
                name);
    }

    public String getGetterMethodImpl() {
        return String.format("%s %s::%s::%s() const { return %s_; }",
                type, Config.namespace, className, name, name );
    }

    public String getSetterMethodImpl() {
        return String.format("void %s::%s::set%s(%s %s) { %s_ = %s; }",
                Config.namespace, className,
                Util.capitalizeFirstLetter(name),
                isPrimitive()? type : "const " + type + "&",
                name, name, name);
    }

    public void addRole(Role role) {
        roles.add(role.getRole());
    }

    public boolean hasRole(Role role) {
        return roles.contains(role.getRole());
    }

    public boolean hasRole(String role) {
        return roles.contains(Role.toRole(role).getRole());
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (name != null ? !name.equals(field.name) : field.name != null) return false;
        return type != null ? type.equals(field.type) : field.type == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isGetter() {
        return getter;
    }

    public void setGetter(boolean getter) {
        this.getter = getter;
    }

    public boolean isSetter() {
        return setter;
    }

    public void setSetter(boolean setter) {
        this.setter = setter;
    }

    public Field(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public String nameToRole() {
        String role = Util.capitalizeFirstLetter(name);
        if(role.contains("_")) {
            String[] splits = role.split("_");
            role = "";
            for(String split: splits) {
                role += Util.capitalizeFirstLetter(split);
            }
        }
        return role + "Role";
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", getter=" + getter +
                ", setter=" + setter +
                ", defaultValue='" + defaultValue + '\'' +
                ", className='" + className + '\'' +
                ", column='" + column + '\'' +
                ", cType='" + cType + '\'' +
                ", primaryKey=" + primaryKey +
                ", autoincrement=" + autoincrement +
                ", writable=" + writable +
                ", forignKey=" + forignKey +
                ", roles=" + roles +
                '}';
    }
}
