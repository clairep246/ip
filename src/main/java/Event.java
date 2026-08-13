public class Event {
    private String name;
    private String from;
    private String to;

    private static int eventCount = 0;
    private int eventId;
    private boolean isDone;

    public Event(String name, String from, String to) {
        this.name = name;
        this.from = from;
        this.to = to;

        eventCount++;
        this.eventId = eventCount;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    public int getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + name
                + " (from: " + from + " to: " + to + ")";
    }
}