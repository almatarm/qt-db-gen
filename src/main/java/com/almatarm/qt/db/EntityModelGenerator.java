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
public class EntityModelGenerator {
    Config config;
    ArrayList<Entity> entities = new ArrayList<>();

    public EntityModelGenerator(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void process() throws IOException {
        for (Entity entity: entities) {
            generateClassModel(entity);
            generateClassModelImpl(entity);
        }
    }

    private void generateClassModel(Entity entity) throws IOException {
        try {
            Reader reader = new InputStreamReader(EntityModelGenerator.class.getClassLoader()
                    .getResourceAsStream("class_model.h"));
            VelocityContext context = new VelocityContext();
            context.put("Noun", Noun.class);
            context.put("Util", Util.class);

            context.put("entity", entity);
            context.put("className", entity.getClassName());
            context.put("classNameVar", Util.decapitalizeFirstLetter(entity.getClassName()));
            context.put("classDaoVar", Util.decapitalizeFirstLetter(entity.getClassName()) + "Dao");
            context.put("classNameVars", Noun.pluralOf(Util.decapitalizeFirstLetter(entity.getClassName())));
            context.put("fields", entity.getFields());

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
                Files.write(new File(Config.projectDir, entity.getClassName()  + "Model.h").toPath(), output.getBytes());
        } catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException ex) {
            Logger.getLogger(EntityModelGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void generateClassModelImpl(Entity entity) throws IOException {
        try {
            SQLiteUtil sqlite = new SQLiteUtil(entity.getClassName(), entity);

            Reader reader = new InputStreamReader(EntityModelGenerator.class.getClassLoader()
                    .getResourceAsStream("class_model.cpp"));
            VelocityContext context = new VelocityContext();
            context.put("Noun", Noun.class);
            context.put("Util", Util.class);
            context.put("new_line", "\n");

            context.put("namespace", config.namespace);
            context.put("entity", entity);
            context.put("className", entity.getClassName());
            context.put("classNameVar", Util.decapitalizeFirstLetter(entity.getClassName()));
            context.put("classDaoVar", Util.decapitalizeFirstLetter(entity.getClassName()) + "Dao");
            context.put("classNameVars", Noun.pluralOf(Util.decapitalizeFirstLetter(entity.getClassName())));
            context.put("fields", entity.getFields());

            context.put("table", entity.table);
            context.put("create_table", sqlite.createTable());
            context.put("update_statement", sqlite.updateStatement());
            context.put("insert_statement", sqlite.insertStatement());

            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", reader);

            String output = writer.toString().replaceAll("\n\\s*\n+", "\n\n");
            System.out.println(output);
            if(Config.write)
                Files.write(new File(Config.projectDir, entity.getClassName() + "Model.cpp").toPath(), output.getBytes());
        } catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException ex) {
            Logger.getLogger(EntityModelGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
