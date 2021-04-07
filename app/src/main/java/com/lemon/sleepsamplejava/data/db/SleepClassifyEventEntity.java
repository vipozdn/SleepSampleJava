package com.lemon.sleepsamplejava.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.location.SleepClassifyEvent;
import com.google.android.gms.location.SleepSegmentEvent;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "sleep_classify_events_table")
public class SleepClassifyEventEntity {

    @PrimaryKey
    @ColumnInfo(name = "time_stamp_seconds")
    public int timestampSeconds;

    @ColumnInfo(name = "confidence")
    public int confidence;

    @ColumnInfo(name = "motion")
    public int motion;

    @ColumnInfo(name = "light")
    public int light;

    public SleepClassifyEventEntity(int timestampSeconds, int confidence, int motion, int light) {
        this.timestampSeconds = timestampSeconds;
        this.confidence = confidence;
        this.motion = motion;
        this.light = light;
    }

    public static SleepClassifyEventEntity from(SleepClassifyEvent sleepClassifyEvent) {
        return new SleepClassifyEventEntity(
                ((int)(sleepClassifyEvent.getTimestampMillis() / 1000)),
                sleepClassifyEvent.getConfidence(),
                sleepClassifyEvent.getMotion(),
                sleepClassifyEvent.getLight()
        );
    }

    public static ArrayList<SleepClassifyEventEntity> from(List<SleepClassifyEvent> sleepClassifyEvents) {
        ArrayList<SleepClassifyEventEntity> eventEntities = new ArrayList<>();

        for (int i = 0; i < sleepClassifyEvents.size(); i++) {
            SleepClassifyEvent iEvent = sleepClassifyEvents.get(i);
            SleepClassifyEventEntity sleepClassifyEventEntity =
                    new SleepClassifyEventEntity(
                            ((int)(iEvent.getTimestampMillis() / 1000)),
                            iEvent.getConfidence(),
                            iEvent.getMotion(),
                            iEvent.getLight()
                    );
            eventEntities.add(sleepClassifyEventEntity);
        }

        return eventEntities;

    }
}
