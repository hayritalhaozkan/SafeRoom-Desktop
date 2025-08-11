package com.saferoom.view.cell;

import com.saferoom.controller.MessagesController.Contact;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ContactCell extends ListCell<Contact> {
    private final HBox hbox = new HBox(15);
    private final Label avatar = new Label();
    private final Label nameLabel = new Label();
    private final Label lastMessageLabel = new Label();
    private final Label timeLabel = new Label();
    private final Label unreadBadge = new Label();
    private final VBox textVBox = new VBox(2);
    private final VBox timeVBox = new VBox(5);
    private final Pane spacer = new Pane();

    public ContactCell() {
        super();
        avatar.getStyleClass().add("contact-avatar");
        nameLabel.getStyleClass().add("contact-name");
        lastMessageLabel.getStyleClass().add("contact-last-message");
        timeLabel.getStyleClass().add("contact-time");
        unreadBadge.getStyleClass().add("unread-badge");
        timeVBox.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        textVBox.getChildren().addAll(nameLabel, lastMessageLabel);
        timeVBox.getChildren().addAll(timeLabel, unreadBadge);
        hbox.getChildren().addAll(avatar, textVBox, spacer, timeVBox);
        hbox.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Contact contact, boolean empty) {
        super.updateItem(contact, empty);
        if (empty || contact == null) {
            setGraphic(null);
        } else {
            avatar.setText(contact.getAvatarChar());
            nameLabel.setText(contact.getName());
            lastMessageLabel.setText(contact.getLastMessage());
            timeLabel.setText(contact.getTime());
            unreadBadge.setVisible(contact.getUnreadCount() > 0);
            if (contact.getUnreadCount() > 0) {
                unreadBadge.setText(String.valueOf(contact.getUnreadCount()));
            }
            setGraphic(hbox);
        }
    }
}