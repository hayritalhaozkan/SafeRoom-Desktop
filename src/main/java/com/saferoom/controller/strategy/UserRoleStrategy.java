package com.saferoom.controller.strategy;

import com.saferoom.model.Participant;
import javafx.scene.Node;

public class UserRoleStrategy implements MeetingRoleStrategy {

    @Override
    public Node createParticipantListItemControls(Participant currentUser, Participant targetParticipant) {
        return null; // Standart kullanıcılar kontrol butonlarını görmez.
    }
}