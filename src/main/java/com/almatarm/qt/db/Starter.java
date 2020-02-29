package com.almatarm.qt.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by almatarm on 29/11/2019.
 */
public class Starter {
    public static void main(String[] args) throws IOException {
//        gallery();
//        pdam();
        todo();
    }

    public static void gallery() throws IOException {
        Config.projectDir       = new File(".");
        Config.write            = true;
        Config.namespace        = "gallery_core";
        Config.export_library   = "GALLERYCORE_EXPORT";
        Config.globalHeaderFile = "gallery-core_global.h";

        ArrayList<Entity> entites = new ArrayList<>();

        //Album Entity
        Entity album = new Entity("Album");
        album.addField(new Field("id", "int", true, true, "-1"));
        album.addField(new Field("name", "QString", true, true, "\"\""));
        album.addConstructor(Util.list(album.getFieldByName("name")), false);
        album.getFieldByName("id").primaryKey = true;
        album.getFieldByName("id").autoincrement = true;
        entites.add(album);

        //Album Entity
        Entity picture = new Entity("Picture");
        picture.addField(new Field("id", "int", true, true, "-1"));
        picture.addField(new Field("fileUrl", "QUrl", true, true));
        Constructor constructor = new Constructor(picture,
                Util.list(new Field("filePath", "QString", "\"\"")), false);
        constructor.callOtherConstuctor = "Picture(QUrl::fromLocalFile(filePath))";
        picture.getConstructors().add(constructor);
        picture.addConstructor(Util.list(picture.getFieldByName("fileUrl")), false);
        picture.getFieldByName("id").primaryKey = true;
        picture.getFieldByName("id").autoincrement = true;
        entites.add(picture);

        picture.addForignKey(new ForignKey(album, album.getFieldByName("id")));

        EntityGenerator entitiyGenerator = new EntityGenerator(entites);
        EntityDaoSqliteGenerator entitiyDaoGenerator = new EntityDaoSqliteGenerator(entites);
        EntityModelGenerator entityModelGenerator = new EntityModelGenerator(entites);
        SqlDBGenerator sqlDbGenerator = new SqlDBGenerator(entites);

        entitiyGenerator.process();
        entitiyDaoGenerator.process();
        entityModelGenerator.process();
        sqlDbGenerator.process();
    }


    public static void todo() throws IOException {
        Config.projectDir       = new File("/Users/almatarm/projects/code/qt/Todo/todo-core");
        Config.write            = true;
        Config.namespace        = "todo_core";
        Config.export_library   = "TODOCORE_EXPORT";
        Config.globalHeaderFile = "todo-core_global.h";

        ArrayList<Entity> entites = new ArrayList<>();

//        //User Entity
//        Entity user = new Entity("User");
//        user.addField(new Field("id", "int", true, true, "-1"));
//        user.addField(new Field("username", "QString", true, true, "\"\""));
//        user.addField(new Field("first_name", "QString", true, true, "\"\""));
//        user.addConstructor(Util.list(user.getFieldByName("username")), false);
//        user.getFieldByName("id").primaryKey = true;
//        user.getFieldByName("id").autoincrement = true;
//        entites.add(user);

        //Label Entity
//            "id": 790748,
//            "name": "Label1",
//            "color": 30,
//            "item_order": 0,
//            "is_deleted": 0,
//            "is_favorite": 0
        Entity label = new Entity("Label");
        label.addField(new Field("id", "int", true, true, "-1"));
        label.addField(new Field("name", "QString", true, true, "\"\""));
        label.addField(new Field("color", "int", true, true, "1"));
        label.addField(new Field("position", "int", true, true, "-1"));
        label.addConstructor(Util.list(label.getFieldByName("name")), false);
        label.getFieldByName("id").primaryKey = true;
        label.getFieldByName("id").autoincrement = true;
        label.getFieldByName("name").addRole(Field.Role.display_role);
        entites.add(label);

        // Project Entity
//            "id": 396936926,
//            "name": "Project1",
//            "color": 30,
//            "parent_id": null,
//            "child_order": 1,
//            "collapsed": 0,
//            "shared": false,
//            "parent_id": null,
//            "legacy_parent_id": null,
//            "is_deleted": 0,
//            "is_archived": 0,
//            "is_favorite": 0
        Entity project = new Entity("Project");
        project.addField(new Field("id", "int", true, true, "-1"));
        project.addField(new Field("name", "QString", true, true, "\"\""));
        project.addField(new Field("emoji", "QString", true, true, "\"\""));
        project.addField(new Field("color", "int", true, true, "1"));
        project.addField(new Field("collapsed", "bool", true, true, "false"));
//        project.addField(new Field("archived", "bool", true, true, "false"));
        project.addConstructor(Util.list(project.getFieldByName("name")), false);
        project.getFieldByName("id").primaryKey = true;
        project.getFieldByName("id").autoincrement = true;
        project.getFieldByName("name").addRole(Field.Role.display_role);
        entites.add(project);

        project.addForignKey(new ForignKey(project, project.getFieldByName("id"), "parent_id"));


//        //Group Entity
//        Entity group = new Entity("Group");
//        group.addField(new Field("id", "int", true, true, "-1"));
//        group.addField(new Field("name", "QString", true, true, "\"\""));
//        group.addField(new Field("color", "int", true, true, "1"));
//        group.addField(new Field("type", "int", true, true, "-1"));
//        group.addConstructor(Util.list(group.getFieldByName("name")), false);
//        group.getFieldByName("id").primaryKey = true;
//        group.getFieldByName("id").autoincrement = true;
//        entites.add(group);
//
//        //GroupItems Entity
//        Entity groupItems = new Entity("GroupItems");
//        groupItems.addField(new Field("group_id", "int", true, true, "-1"));
//        groupItems.addField(new Field("item_id", "int", true, true, "-1"));
//        groupItems.addField(new Field("order", "int", true, true, "-1"));
//        entites.add(groupItems);


//        //Label Entity
//        Entity note = new Entity("Label");
//        note.addField(new Field("id", "int", true, true, "-1"));
//        note.addField(new Field("name", "QString", true, true, "\"\""));
//        note.addField(new Field("color", "int", true, true, "1"));
//        note.addField(new Field("order", "int", true, true, "-1"));
//        note.addConstructor(Util.list(note.getFieldByName("name")), false);
//        note.getFieldByName("id").primaryKey = true;
//        note.getFieldByName("id").autoincrement = true;
//        entites.add(note);

        //Task Entity
//        "id": 301946961,
//        "user_id": 1855589,
//        "project_id": 396936926,
//        "content": "Task1",
//        "priority": 1,
//        "due": null,
//        "parent_id": null,
//        "child_order": 1,
//        "section_id": null,
//        "day_order": -1,
//        "collapsed": 0,
//        "children": null,
//        "labels": [12839231, 18391839],
//        "added_by_uid": 1855589,
//        "assigned_by_uid": 1855589,
//        "responsible_uid": null,
//        "checked": 0,
//        "in_history": 0,
//        "is_deleted": 0,
//        "sync_id": null,
//        "date_added": "2014-09-26T08:25:05Z"
        Entity task = new Entity("Task");
        task.addField(new Field("id", "int", true, true, "-1"));
        task.addField(new Field("emoji", "QString", true, true, "\"\""));
        task.addField(new Field("content", "QString", true, true, "\"\""));
        task.addField(new Field("color", "int", true, true, "1"));
        task.addField(new Field("position", "int", true, true, "-1"));
        task.addConstructor(Util.list(task.getFieldByName("content")), false);
        task.getFieldByName("id").primaryKey = true;
        task.getFieldByName("id").autoincrement = true;
        task.getFieldByName("content").addRole(Field.Role.display_role);
        entites.add(task);

        task.addForignKey(new ForignKey(project, project.getFieldByName("id"), true));
        task.addForignKey(new ForignKey(task, task.getFieldByName("id"), "parent_id"));

        EntityGenerator entitiyGenerator = new EntityGenerator(entites);
        EntityDaoSqliteGenerator entitiyDaoGenerator = new EntityDaoSqliteGenerator(entites);
        EntityModelGenerator entityModelGenerator = new EntityModelGenerator(entites);
        SqlDBGenerator sqlDbGenerator = new SqlDBGenerator(entites);

        entitiyGenerator.process();
        entitiyDaoGenerator.process();
        entityModelGenerator.process();
        sqlDbGenerator.process();
    }

    public static void pdam() throws IOException {
        Config.projectDir       = new File("/Users/almatarm/Sync/projects/qt/PDAM/pdam-core");
        Config.write            = true;
        Config.namespace        = "pdam_core";
        Config.export_library   = "PDAMCORE_EXPORT";
        Config.globalHeaderFile = "pdam-core_global.h";

        ArrayList<Entity> entites = new ArrayList<>();

        //MediaFile Entity
        Entity mediaFile = new Entity("MediaFile");
        mediaFile.addField(new Field("id", "int", true, true, "-1"));
        mediaFile.addField(new Field("fileUrl", "QUrl", true, true));
        Constructor constructor = new Constructor(mediaFile,
                Util.list(new Field("filePath", "QString", "\"\"")), false);
        constructor.callOtherConstuctor = "MediaFile(QUrl::fromLocalFile(filePath))";
        mediaFile.getConstructors().add(constructor);
        mediaFile.addConstructor(Util.list(mediaFile.getFieldByName("fileUrl")), false);
        mediaFile.getFieldByName("id").primaryKey = true;
        mediaFile.getFieldByName("id").autoincrement = true;
        entites.add(mediaFile);


        //Artist Entity
        Entity artist = new Entity("Artist");
        artist.addField(new Field("id", "int", true, true, "-1"));
        artist.addField(new Field("name", "QString", "\"\""));
        artist.addConstructor(Util.list(artist.getFieldByName("name")), false);
        artist.getFieldByName("id").primaryKey = true;
        artist.getFieldByName("id").autoincrement = true;
        artist.getFieldByName("name").addRole(Field.Role.display_role);
        entites.add(artist);

        //Audiobook Entity
        Entity audiobook = new Entity("Audiobook");
        audiobook.addField(new Field("id", "int", true, true));
        audiobook.addField(new Field("title", "QString", true, true, "\"\""));
        audiobook.addField(new Field("authors", "QString", true, true, "\"\""));
        audiobook.addField(new Field("narrators", "QString", true, true, "\"\""));
        audiobook.addField(new Field("series", "QString", true, true, "\"\""));
        audiobook.addField(new Field("series_order", "float", true, true, "0.0"));
        audiobook.addField(new Field("release_date", "QDateTime", true, true, "QDateTime::currentDateTime()"));
        audiobook.addField(new Field("summary", "QString", true, true, "\"\""));
        audiobook.getFieldByName("id").primaryKey = true;
        audiobook.getFieldByName("id").autoincrement = true;
        audiobook.getFieldByName("id").writable = false;
        audiobook.getFieldByName("title").addRole(Field.Role.display_role);
//        audiobook.addConstructor(new ArrayList<Field>(), false);
        audiobook.addConstructor(Util.list(audiobook.getFieldByName("title")), false);
        entites.add(audiobook);

        EntityGenerator entitiyGenerator = new EntityGenerator(entites);
        EntityDaoSqliteGenerator entitiyDaoGenerator = new EntityDaoSqliteGenerator(entites);
        EntityModelGenerator entityModelGenerator = new EntityModelGenerator(entites);
        SqlDBGenerator sqlDbGenerator = new SqlDBGenerator(entites);
        sqlDbGenerator.databaseFileName = "pdam.db";

        entitiyGenerator.process();
        entitiyDaoGenerator.process();
        entityModelGenerator.process();
        sqlDbGenerator.process();
    }
}

