package es.upm.miw.devops.rest.dto;

import jakarta.validation.constraints.NotNull;

public class UserActiveStatusItem {

    @NotNull
    private String id;

    @NotNull
    private Boolean active;

    public UserActiveStatusItem() {}

    public UserActiveStatusItem(String id, Boolean active) {
        this.id = id;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}