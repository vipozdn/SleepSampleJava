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
public interface SleepSegmentEventDao {

    @Query("SELECT * FROM sleep_segment_events_table ORDER BY start_time_millis DESC")
    LiveData<List<SleepSegmentEventEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SleepSegmentEventEntity sleepSegmentEventEntity);

    // Returns the number of sleepSegmentEventEntities inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SleepSegmentEventEntity> sleepSegmentEventEntities);

    @Delete
    void delete(SleepSegmentEventEntity sleepSegmentEventEntity);

    // Returns the number of sleepSegmentEventEntities deleted
    @Query("DELETE FROM sleep_segment_events_table")
    void deleteAll();

}
