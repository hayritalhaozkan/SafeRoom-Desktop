package com.saferoom.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Meeting {
    private String meetingId;
    private String meetingName;
    private ObservableList<Participant> participants;

    public Meeting(String meetingId, String meetingName) {
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.participants = FXCollections.observableArrayList();
    }

    public void addDummyParticipants() {
        participants.add(new Participant("User 1 (You)", true, true));
        participants.add(new Participant("Alice", true, false));
        participants.add(new Participant("Bob", false, true));
        participants.add(new Participant("Charlie", false, false));
        participants.add(new Participant("David", true, true));
        participants.add(new Participant("Eve", true, false));
        participants.add(new Participant("Frank", false, true));
        participants.add(new Participant("Grace", true, true));
    }

    public String getMeetingId() { return meetingId; }
    public String getMeetingName() { return meetingName; }
    public ObservableList<Participant> getParticipants() { return participants; }
}