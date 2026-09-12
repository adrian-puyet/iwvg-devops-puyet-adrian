package es.upm.miw.devops.rest.dto;

public class ActiveStatusRequest {

    private boolean active;

    public ActiveStatusRequest() {
    }

    public ActiveStatusRequest(boolean active) {
        this.active = active;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}