package com.almatarm.qt.db;

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
public class SqlDBGenerator {
    String databaseFileName = "database.db";
    Config config;
    ArrayList<Entity> entities = new ArrayList<>();

    public SqlDBGenerator(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void process() throws IOException {
//        for (Entity entity: entities) {
//            generateClass(entity);
//            generateClassImpl(entity);
//        }
        generateDatabaseManager(entities);
        generateDatabaseManagerImpl(entities);
    }

    private void generateDatabaseManager(ArrayList<Entity> entities) throws IOException {
        try {
            Reader reader = new InputStreamReader(SqlDBGenerator.class.getClassLoader()
                    .getResourceAsStream("DatabaseManager.h"));
            VelocityContext context = new VelocityContext();
            context.put("Noun", Noun.class);
            context.put("Util", Util.class);
            context.put("new_line", "\n");

            context.put("namespace", config.namespace);
            context.put("entities", entities);
            context.put("database_filename", databaseFileName);

            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", reader);

            String output = writer.toString().replaceAll("\n\\s*\n+", "\n\n");
//            System.out.println(output);
            if(Config.write)
                Files.write(new File(Config.projectDir, "DatabaseManager.h").toPath(), output.getBytes());
        } catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException ex) {
            Logger.getLogger(SqlDBGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generateDatabaseManagerImpl(ArrayList<Entity> entities) throws IOException {
        try {
            Reader reader = new InputStreamReader(SqlDBGenerator.class.getClassLoader()
                    .getResourceAsStream("DatabaseManager.cpp"));
            VelocityContext context = new VelocityContext();
            context.put("Noun", Noun.class);
            context.put("Util", Util.class);
            context.put("new_line", "\n");

            context.put("namespace", config.namespace);
            context.put("entities", entities);
            context.put("database_filename", databaseFileName);

            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", reader);

            String output = writer.toString().replaceAll("\n\\s*\n+", "\n\n");
//            System.out.println(output);
            if(Config.write)
                Files.write(new File(Config.projectDir, "DatabaseManager.cpp").toPath(), output.getBytes());
        } catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException ex) {
            Logger.getLogger(SqlDBGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
