package com.saferoom.controller.strategy;

import com.saferoom.model.Participant;
import javafx.scene.control.MenuItem;
import java.util.List;

public interface MeetingRoleStrategy {
    List<MenuItem> createParticipantMenuItems(Participant currentUser, Participant targetParticipant, Runnable onStateChanged);
}