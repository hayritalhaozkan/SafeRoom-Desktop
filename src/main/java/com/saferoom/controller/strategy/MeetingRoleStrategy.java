package com.saferoom.controller.strategy;

import com.saferoom.model.Participant;
import javafx.scene.Node;

public interface MeetingRoleStrategy {
    Node createParticipantListItemControls(Participant currentUser, Participant targetParticipant);
}
