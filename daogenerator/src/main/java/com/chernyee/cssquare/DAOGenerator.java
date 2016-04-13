package com.chernyee.cssquare;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class DAOGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.chernyee.cssquare.model");

        addNote(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty().primaryKeyAsc().autoincrement();
        note.addStringProperty("text").notNull();
        note.addStringProperty("desc");
        note.addStringProperty("color");
        note.addBooleanProperty("check");
    }



}
