package com.saferoom.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Participant {
    private final StringProperty name;
    private final BooleanProperty cameraOn;
    private final BooleanProperty muted;

    public Participant(String name, boolean cameraOn, boolean muted) {
        this.name = new SimpleStringProperty(name);
        this.cameraOn = new SimpleBooleanProperty(cameraOn);
        this.muted = new SimpleBooleanProperty(muted);
    }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    public boolean isCameraOn() { return cameraOn.get(); }
    public BooleanProperty cameraOnProperty() { return cameraOn; }
    public void setCameraOn(boolean cameraOn) { this.cameraOn.set(cameraOn); }

    public boolean isMuted() { return muted.get(); }
    public BooleanProperty mutedProperty() { return muted; }
    public void setMuted(boolean muted) { this.muted.set(muted); }
}