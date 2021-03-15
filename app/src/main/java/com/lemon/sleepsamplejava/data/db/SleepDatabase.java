package com.lemon.sleepsamplejava.data.db;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



@Database(entities = {SleepSegmentEventEntity.class, SleepClassifyEventEntity.class}, version = 1)
public abstract class SleepDatabase extends RoomDatabase {

    private final static String DATABASE_NAME = "sleep_segments_database";

    public abstract SleepSegmentEventDao sleepSegmentEventDao();

    public abstract SleepClassifyEventDao sleepClassifyEventDao();


    @Nullable
    private static volatile SleepDatabase instance = null;

    public static SleepDatabase getDatabase(Context context) {

        if (instance == null) {
            synchronized (SleepDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, SleepDatabase.class, DATABASE_NAME)
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this sample.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return instance;
    }

}
