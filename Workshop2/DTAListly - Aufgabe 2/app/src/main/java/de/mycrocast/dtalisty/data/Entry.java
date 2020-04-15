package de.mycrocast.dtalisty.data;

public class Entry {

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    private String name;
    private long creationTime;
    private boolean isActive;
    private Priority priority;

    public Entry(String name, Priority priority) {
        this.name = name;
        this.priority = priority;
        this.isActive = true;
    }

    public Entry(String name) {
        this(name, Priority.MEDIUM);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
