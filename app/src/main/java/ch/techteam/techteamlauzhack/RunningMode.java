package ch.techteam.techteamlauzhack;

public enum RunningMode {
    RUN_DISTANCE("Run Distance"), RUN_TIME("Run Time"), INTERVAL("Intervals"), WALK("Walk");

    private String title;

    RunningMode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
