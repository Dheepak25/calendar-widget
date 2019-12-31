package org.andstatus.todoagenda.task;

import org.andstatus.todoagenda.prefs.InstanceSettings;
import org.andstatus.todoagenda.prefs.OrderedEventSource;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import static org.andstatus.todoagenda.util.StringUtil.notNull;

public class TaskEvent {

    private OrderedEventSource eventSource;
    private long id;
    private String title = "";
    private final InstanceSettings settings;
    private final DateTimeZone zone;
    private DateTime startDate;
    private DateTime dueDate;
    private int color;
    private TaskStatus status = TaskStatus.UNKNOWN;

    public TaskEvent(InstanceSettings settings, DateTimeZone zone) {
        this.settings = settings;
        this.zone = zone;
    }

    public OrderedEventSource getEventSource() {
        return eventSource;
    }

    public TaskEvent setEventSource(OrderedEventSource eventSource) {
        this.eventSource = eventSource;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = notNull(title);
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getDueDate() {
        return dueDate;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDates(Long startMillis, Long dueMillis) {
        startDate = toStartDate(startMillis, dueMillis);
        dueDate = toDueDate(startMillis, dueMillis);
    }

    private DateTime toStartDate(Long startMillis, Long dueMillis) {
        DateTime startDate;
        if (startMillis != null) {
            startDate = new DateTime(startMillis, zone);
        } else {
            if (dueMillis != null) {
                startDate = new DateTime(dueMillis, zone);
            } else {
                startDate = settings.clock().now().withTimeAtStartOfDay();
            }
        }
        return startDate;
    }

    private DateTime toDueDate(Long startMillis, Long dueMillis) {
        DateTime dueDate = dueMillis == null
                ? settings.clock().startOfTomorrow(zone)
                : new DateTime(dueMillis, zone);
        return startMillis == null
                ? dueDate.plusSeconds(1)
                : dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
