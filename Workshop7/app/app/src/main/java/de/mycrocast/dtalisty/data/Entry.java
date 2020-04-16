package de.mycrocast.dtalisty.data;

public class Entry extends AbstractIdentifiable {

    public enum Priority {
        HIGH,
        MEDIUM,
        LOW
    }

    private String name;
    private long creationTime;
    private boolean isActive;
    private Priority priority;


    public Entry(String name, Priority priority) {
        this.name = name;
        this.priority = priority;
        this.isActive = true;
        this.creationTime = System.currentTimeMillis();
    }

    public Entry(String name) {
        this(name, Priority.MEDIUM);
    }

    public Entry() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
