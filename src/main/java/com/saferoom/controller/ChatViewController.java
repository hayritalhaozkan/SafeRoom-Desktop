package com.saferoom.controller;

import com.saferoom.model.Message;
import com.saferoom.model.User;
import com.saferoom.service.ChatService;
import com.saferoom.view.cell.MessageCell;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane; // Yeni import
import javafx.scene.layout.HBox;

public class ChatViewController {

    // --- YENİ EKLENEN FXML ALANI ---
    @FXML private BorderPane chatPane; // Arayüzün kök paneline erişim

    // Mevcut FXML alanları
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
    private ChatService chatService;

    @FXML
    public void initialize() {
        this.currentUser = new User("currentUser123", "You");
        this.chatService = ChatService.getInstance();

        chatService.newMessageProperty().addListener((obs, oldMsg, newMsg) -> {
            if (newMsg != null && newMsg.getSenderId().equals(currentUser.getId())) {
                messageListView.scrollTo(messageListView.getItems().size() - 1);
            }
        });
    }

    // --- YENİ EKLENEN METOT ---
    /**
     * Bu panelin maksimum genişliğini dışarıdan ayarlar.
     * @param width Ayarlanacak maksimum genişlik.
     */
    public void setWidthConstraint(double width) {
        if (chatPane != null) {
            chatPane.setMaxWidth(width);
            chatPane.setPrefWidth(width);
        }
    }

    public void initChannel(String channelId) {
        this.currentChannelId = channelId;
        ObservableList<Message> messages = chatService.getMessagesForChannel(channelId);
        messageListView.setItems(messages);
        messageListView.setCellFactory(param -> new MessageCell(currentUser.getId()));
        if (!messages.isEmpty()) {
            messageListView.scrollTo(messages.size() - 1);
        }
    }

    public void setHeader(String name, String status, String avatarChar, boolean isGroupChat) {
        // ... (Bu metodun içeriği aynı kalıyor) ...
    }

    @FXML
    private void handleSendMessage() {
        // ... (Bu metodun içeriği aynı kalıyor) ...
    }
}