package com.saferoom.controller.strategy;

import com.saferoom.model.Participant;
import javafx.scene.control.MenuItem;
import java.util.Collections;
import java.util.List;

public class UserRoleStrategy implements MeetingRoleStrategy {

    @Override
    public List<MenuItem> createParticipantMenuItems(Participant currentUser, Participant targetParticipant, Runnable onStateChanged) {
        return Collections.emptyList();
    }
}