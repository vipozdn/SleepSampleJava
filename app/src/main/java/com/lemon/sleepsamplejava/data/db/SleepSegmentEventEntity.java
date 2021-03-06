package com.lemon.sleepsamplejava.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.location.SleepSegmentEvent;

import java.util.ArrayList;
import java.util.List;

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


    public static SleepSegmentEventEntity from(SleepSegmentEvent sleepSegmentEvent) {
        return new SleepSegmentEventEntity(sleepSegmentEvent.getStartTimeMillis(),
                sleepSegmentEvent.getEndTimeMillis(), sleepSegmentEvent.getStatus());
    }

    public static ArrayList<SleepSegmentEventEntity> from(List<SleepSegmentEvent> sleepSegmentEvents) {
        ArrayList<SleepSegmentEventEntity> eventEntities = new ArrayList<>();

        for (int i = 0; i < sleepSegmentEvents.size(); i++) {
            SleepSegmentEvent iEvent = sleepSegmentEvents.get(i);
            SleepSegmentEventEntity sleepSegmentEventEntity =
                    new SleepSegmentEventEntity(iEvent.getStartTimeMillis(),
                    iEvent.getEndTimeMillis(), iEvent.getStatus());
            eventEntities.add(sleepSegmentEventEntity);
        }

        return eventEntities;
    }
}
