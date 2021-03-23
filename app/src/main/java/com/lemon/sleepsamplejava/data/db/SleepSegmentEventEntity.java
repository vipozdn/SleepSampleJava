package com.lemon.sleepsamplejava.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.location.SleepSegmentEvent;

@Entity(tableName = "sleep_segment_events_table")
public class SleepSegmentEventEntity {

    @PrimaryKey
    @ColumnInfo(name = "start_time_millis")
    public long startTimeMillis;

    @ColumnInfo(name = "end_time_millis")
    public long endTimeMillis;

    @ColumnInfo(name = "status")
    public int status;

    public SleepSegmentEventEntity(long startTimeMillis, long endTimeMillis, int status) {
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
        this.status = status;
    }

    public SleepSegmentEventEntity from(SleepSegmentEvent sleepSegmentEvent) {
        this.startTimeMillis = sleepSegmentEvent.getStartTimeMillis();
        this.endTimeMillis = sleepSegmentEvent.getEndTimeMillis();
        this.status = sleepSegmentEvent.getStatus();
    }
}
