package com.saferoom.controller.strategy;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.saferoom.model.Participant;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class AdminRoleStrategy implements MeetingRoleStrategy {

    @Override
    public Node createParticipantListItemControls(Participant currentUser, Participant targetParticipant) {
        if (!currentUser.equals(targetParticipant)) {
            JFXButton optionsButton = new JFXButton();
            FontIcon icon = new FontIcon("fas-ellipsis-v");
            icon.getStyleClass().add("participant-action-icon");
            optionsButton.setGraphic(icon);
            optionsButton.getStyleClass().add("participant-action-button");

            JFXPopup popup = createAdminPopup(targetParticipant);
            optionsButton.setOnAction(event -> popup.show(optionsButton, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -10, 0));

            return optionsButton;
        }
        return null;
    }

    private JFXPopup createAdminPopup(Participant targetParticipant) {
        JFXButton muteMicButton = new JFXButton("Mikrofonu Kapat");
        JFXButton stopCameraButton = new JFXButton("Kamerayı Kapat");
        JFXButton removeButton = new JFXButton("Toplantıdan At");

        muteMicButton.setOnAction(e -> System.out.println("ADMIN ACTION: " + targetParticipant.getName() + " susturuldu."));
        stopCameraButton.setOnAction(e -> System.out.println("ADMIN ACTION: " + targetParticipant.getName() + " kamerası kapatıldı."));
        removeButton.setOnAction(e -> System.out.println("ADMIN ACTION: " + targetParticipant.getName() + " atıldı."));

        VBox content = new VBox(muteMicButton, stopCameraButton, removeButton);
        content.getStyleClass().add("popup-menu-container");

        for (Node node : content.getChildren()) {
            if (node instanceof JFXButton) {
                ((JFXButton) node).getStyleClass().add("popup-menu-item");
            }
        }
        removeButton.getStyleClass().add("popup-menu-item-danger");

        JFXPopup popup = new JFXPopup(content);
        // DÜZELTME: Kütüphane versiyonuyla uyumsuz olan animasyon satırı kaldırıldı.
        // popup.setAnimationType(JFXPopup.AnimationType.FADE);
        return popup;
    }
}
