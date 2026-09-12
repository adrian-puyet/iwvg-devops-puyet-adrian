package es.upm.miw.devops.rest.dto;

import jakarta.validation.constraints.NotNull;

public class UserActiveStatusItem {

    @NotNull
    private Long id;

    @NotNull
    private Boolean active;

    public UserActiveStatusItem() {}

    public UserActiveStatusItem(Long id, Boolean active) {
        this.id = id;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}