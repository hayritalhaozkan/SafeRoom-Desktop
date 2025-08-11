package com.saferoom.controller;

import com.saferoom.model.Message;
import com.saferoom.model.User;
import com.saferoom.view.cell.MessageCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

public class ChatViewController {

    @FXML private Label chatPartnerAvatar;
    @FXML private Label chatPartnerName;
    @FXML private Label chatPartnerStatus;
    @FXML private ListView<Message> messageListView;
    @FXML private TextField messageInputField;
    @FXML private Button sendButton;
    @FXML private Button phoneButton;
    @FXML private Button videoButton;
    @FXML private HBox chatHeader;

    private User currentUser;
    private String currentChannelId;
    private ObservableList<Message> messages;

    private static final Map<String, ObservableList<Message>> channelMessages = new HashMap<>();

    public void initialize() {
        this.currentUser = new User("currentUser123", "You");
        if (channelMessages.isEmpty()) {
            setupDummyMessages();
        }
    }

    public void initChannel(String channelId) {
        this.currentChannelId = channelId;
        this.messages = channelMessages.computeIfAbsent(channelId, k -> FXCollections.observableArrayList());
        messageListView.setItems(messages);
        messageListView.setCellFactory(param -> new MessageCell(currentUser.getId()));
    }

    public void setHeader(String name, String status, String avatarChar, boolean isGroupChat) {
        chatPartnerName.setText(name);
        chatPartnerStatus.setText(status);
        chatPartnerAvatar.setText(avatarChar);

        chatPartnerStatus.getStyleClass().removeAll("status-online", "status-offline");
        if (status.equalsIgnoreCase("Online")) {
            chatPartnerStatus.getStyleClass().add("status-online");
        } else {
            chatPartnerStatus.getStyleClass().add("status-offline");
        }

        phoneButton.setVisible(!isGroupChat);
        videoButton.setVisible(!isGroupChat);
    }

    @FXML
    private void handleSendMessage() {
        String text = messageInputField.getText();
        if (text == null || text.trim().isEmpty()) return;

        Message newMessage = new Message(text, currentUser.getId(), currentUser.getName().substring(0, 1));
        messages.add(newMessage);

        messageInputField.clear();
        messageListView.scrollTo(messages.size() - 1);
    }

    private void setupDummyMessages() {
        channelMessages.put("zeynep_kaya", FXCollections.observableArrayList(
                new Message("Selam, projenin son durumu hakkında bilgi alabilir miyim?", "zeynep1", "Z"),
                new Message("Tabii, raporu hazırlıyorum. Yarın sabah sende olur.", "currentUser123", "Y"),
                new Message("Harika, teşekkürler! Kolay gelsin.", "zeynep1", "Z")
        ));
        channelMessages.put("ahmet_celik", FXCollections.observableArrayList(
                new Message("Raporu yarın sabah gönderirim.", "ahmet1", "A")
        ));
        channelMessages.put("meeting_phoenix", FXCollections.observableArrayList(
                new Message("Toplantı 15:00'te başlıyor arkadaşlar.", "zeynep1", "Z"),
                new Message("Ben hazır ve beklemedeyim.", "ahmet1", "A")
        ));
    }
}