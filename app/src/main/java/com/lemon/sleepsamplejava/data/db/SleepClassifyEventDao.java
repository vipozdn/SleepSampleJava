package com.lemon.sleepsamplejava.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface SleepClassifyEventDao {

    @Query("SELECT * FROM sleep_classify_events_table ORDER BY time_stamp_millis DESC")
    LiveData<List<SleepClassifyEventEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SleepClassifyEventEntity sleepClassifyEventEntity);

    // Returns the number of sleepClassifyEventEntities inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SleepClassifyEventEntity> sleepClassifyEventEntities);

    @Delete
    void delete(SleepClassifyEventEntity sleepClassifyEventEntity);

    // Returns the number of sleepClassifyEventEntities deleted
    @Query("DELETE FROM sleep_classify_events_table")
    void deleteAll();
}
