package de.mycrocast.dtalisty.data;

public class Entry extends AbstractIdentifiable {

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    private String name;
    private long creationTime;
    private boolean active;
    private Priority priority;

    public Entry(String name, Priority priority) {
        this.name = name;
        this.priority = priority;
        this.active = true;
        this.creationTime = System.currentTimeMillis();
    }

    public Entry(String name) {
        this(name, Priority.MEDIUM);
    }

    public Entry() {

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
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

}
