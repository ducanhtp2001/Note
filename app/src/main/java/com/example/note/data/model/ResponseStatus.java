package com.example.note.data.model;

public class ResponseStatus {
    private boolean status;

    public ResponseStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
