package com.almatarm.qt.db;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jvnet.inflector.Noun;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by almatarm on 29/11/2019.
 */
public class EntityDaoSqliteGenerator {
    Config config;
    ArrayList<Entity> entities = new ArrayList<>();

    public EntityDaoSqliteGenerator(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void process() throws IOException {
        for (Entity entity: entities) {
            generateClassDao(entity);
            generateClassDaoImpl(entity);
        }
    }

    private void generateClassDao(Entity entity) throws IOException {
        try {
            Reader reader = new InputStreamReader(EntityDaoSqliteGenerator.class.getClassLoader()
                    .getResourceAsStream("class_dao.h"));
            VelocityContext context = new VelocityContext();
            context.put("Noun", Noun.class);
            context.put("Util", Util.class);

            context.put("entity", entity);
            context.put("className", entity.getClassName());
            context.put("classNameVar", Util.decapitalizeFirstLetter(entity.getClassName()));
            context.put("fields", entity.getFields());

            context.put("isDependent", entity.isDependent());
            if(entity.isDependent()) {
                ForignKey fk = entity.getDependentKey();
                context.put("fkClassName", fk.getEntity().getClassName());
                context.put("fkName", fk.getFieldName());
                context.put("fkType", fk.key.type);
            }

            context.put("export_library", config.export_library);
            context.put("globalHeaderFile", config.globalHeaderFile);
            context.put("namespace", config.namespace);

            context.put("new_line", "\n");
            context.put("beans", new PropertyUtils());

            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", reader);

            String output = writer.toString().replaceAll("\n\\s*\n+", "\n\n");
//            System.out.println(output);
            if(Config.write)
                Files.write(new File(Config.projectDir, entity.getClassName()  + "Dao.h").toPath(), output.getBytes());
        } catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException ex) {
            Logger.getLogger(EntityDaoSqliteGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void generateClassDaoImpl(Entity entity) throws IOException {
        try {
            SQLiteUtil sqlite = new SQLiteUtil(entity.table, entity);

            Reader reader = new InputStreamReader(EntityDaoSqliteGenerator.class.getClassLoader()
                    .getResourceAsStream("class_dao.cpp"));
            VelocityContext context = new VelocityContext();
            context.put("Noun", Noun.class);
            context.put("Util", Util.class);
            context.put("new_line", "\n");

            context.put("namespace", config.namespace);
            context.put("entity", entity);
            context.put("className", entity.getClassName());
            context.put("classNameVar", Util.decapitalizeFirstLetter(entity.getClassName()));
            context.put("table", entity.table);
            context.put("fields", entity.getFields());
            context.put("create_table", sqlite.createTable());
            context.put("update_statement", sqlite.updateStatement());
            context.put("insert_statement", sqlite.insertStatement());

            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", reader);

            String output = writer.toString().replaceAll("\n\\s*\n+", "\n\n");
            System.out.println(output);
            if(Config.write)
                Files.write(new File(Config.projectDir, entity.getClassName() + "Dao.cpp").toPath(), output.getBytes());
        } catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException ex) {
            Logger.getLogger(EntityDaoSqliteGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
