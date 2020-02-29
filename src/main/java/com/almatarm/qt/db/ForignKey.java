package com.almatarm.qt.db;

/**
 * Created by almatarm on 08/12/2019.
 */
public class ForignKey {
    Entity entity;
    Field key;
    String fieldName;
    boolean dependant = false;

    public ForignKey(Entity entity, Field key) {
        this.entity = entity;
        this.key = key;
        this.fieldName = (entity.className + "_" + key.name).toLowerCase();
    }

    public ForignKey(Entity entity, Field key, boolean dependant) {
        this.entity = entity;
        this.key = key;
        this.dependant = dependant;
        this.fieldName = (entity.className + "_" + key.name).toLowerCase();
    }

    public ForignKey(Entity entity, Field key, String fieldName) {
        this.entity = entity;
        this.key = key;
        this.fieldName = fieldName;
    }

    public ForignKey(Entity entity, Field key, String fieldName, boolean dependant) {
        this.entity = entity;
        this.key = key;
        this.fieldName = fieldName;
        this.dependant = dependant;
    }

    public Field getKey() {
        return key;
    }

    public void setKey(Field key) {
        this.key = key;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getFieldName() {
        return fieldName;
    }
}
