package com.almatarm.qt.db;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jvnet.inflector.Noun;
import org.apache.commons.beanutils.PropertyUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by almatarm on 29/11/2019.
 */
public class EntityGenerator {
    Config config;
    ArrayList<Entity> entities = new ArrayList<>();

    public EntityGenerator(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void process() throws IOException {
        for (Entity entity: entities) {
            generateClass(entity);
            generateClassImpl(entity);
        }
    }

    private void generateClass(Entity entity) throws IOException {
        try {
            Reader reader = new InputStreamReader(EntityGenerator.class.getClassLoader()
                    .getResourceAsStream("class.h"));
            VelocityContext context = new VelocityContext();
            context.put("Noun", Noun.class);
            context.put("Util", Util.class);

            context.put("entity", entity);
            context.put("className", entity.getClassName());
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
                Files.write(new File(Config.projectDir, entity.getClassName() + ".h").toPath(), output.getBytes());
        } catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException ex) {
            Logger.getLogger(EntityGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void generateClassImpl(Entity entity) throws IOException {
        try {
            Reader reader = new InputStreamReader(EntityGenerator.class.getClassLoader()
                    .getResourceAsStream("class.cpp"));
            VelocityContext context = new VelocityContext();
            context.put("Noun", Noun.class);
            context.put("Util", Util.class);
            context.put("new_line", "\n");

            context.put("namespace", config.namespace);
            context.put("entity", entity);
            context.put("className", entity.getClassName());
            context.put("fields", entity.getFields());

            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", reader);

            String output = writer.toString().replaceAll("\n\\s*\n+", "\n\n");
            System.out.println(output);
            if(Config.write)
                Files.write(new File(Config.projectDir, entity.getClassName() + ".cpp").toPath(), output.getBytes());
        } catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException ex) {
            Logger.getLogger(EntityGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
