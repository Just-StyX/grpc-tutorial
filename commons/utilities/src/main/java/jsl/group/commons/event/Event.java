package jsl.group.commons.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;

public class Event<K, D> {
    private final EventType eventType;
    private final K key;
    private final D data;
    private final ZonedDateTime zonedDateTime;

    public Event() {
        this.eventType = null;
        this.key = null;
        this.data = null;
        this.zonedDateTime = ZonedDateTime.now();
    }

    public Event(EventType eventType, K key, D data) {
        this.eventType = eventType;
        this.key = key;
        this.data = data;
        this.zonedDateTime = ZonedDateTime.now();
    }

    public EventType getEventType() {
        return eventType;
    }

    public K getKey() {
        return key;
    }

    public D getData() {
        return data;
    }

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }
}
