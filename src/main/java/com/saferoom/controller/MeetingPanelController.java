package com.saferoom.controller;

import com.jfoenix.controls.JFXButton;
import com.saferoom.MainApp;
import com.saferoom.model.Meeting;
import com.saferoom.model.Participant;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Objects;

public class MeetingPanelController {

    // FXML Değişkenleri
    @FXML private BorderPane gridWrapperPane;
    @FXML private GridPane videoGrid;
    @FXML private BorderPane speakerViewPane;
    @FXML private StackPane contentStackPane;
    @FXML private VBox rightPanelVBox;
    @FXML private Label meetingNameLabel;
    @FXML private JFXButton leaveButton;
    @FXML private JFXButton gridViewButton;
    @FXML private JFXButton speakerViewButton;
    @FXML private ToggleButton participantsToggle;
    @FXML private ToggleButton chatToggle;
    @FXML private ScrollPane participantsScrollPane;
    @FXML private VBox participantsListVBox;
    @FXML private VBox chatVBox;
    @FXML private Label timerLabel;
    @FXML private JFXButton micButton;
    @FXML private JFXButton cameraButton;
    @FXML private JFXButton chatButtonBottom;
    @FXML private JFXButton screenShareButton;
    @FXML private JFXButton recordButton;
    @FXML private JFXButton participantsButtonTop;


    private Meeting currentMeeting;
    private boolean isPanelOpen = false;
    private final Duration animationDuration = Duration.millis(350);

    @FXML
    public void initialize() {
        rightPanelVBox.setTranslateX(320);
        rightPanelVBox.setVisible(false);
        rightPanelVBox.setMaxWidth(rightPanelVBox.getPrefWidth());

        gridViewButton.setOnAction(e -> showGridView());
        speakerViewButton.setOnAction(e -> showSpeakerView());
        participantsToggle.setOnAction(e -> showParticipantsView());
        chatToggle.setOnAction(e -> showChatView());
    }

    public void initData(Meeting meeting) {
        this.currentMeeting = meeting;
        meetingNameLabel.setText(meeting.getMeetingName());
        currentMeeting.addDummyParticipants();

        updateParticipantsList();
        showGridView();
    }

    private void showGridView() {
        gridWrapperPane.setVisible(true);
        speakerViewPane.setVisible(false);
        updateVideoGrid();
        setActiveLayoutButton(gridViewButton);
    }

    private void showSpeakerView() {
        gridWrapperPane.setVisible(false);
        speakerViewPane.setVisible(true);
        updateSpeakerView();
        setActiveLayoutButton(speakerViewButton);
    }

    private void setActiveLayoutButton(JFXButton activeButton) {
        gridViewButton.getStyleClass().remove("active");
        speakerViewButton.getStyleClass().remove("active");
        activeButton.getStyleClass().add("active");
    }

    private void updateSpeakerView() {
        speakerViewPane.getChildren().clear();
        if (currentMeeting.getParticipants().isEmpty()) return;

        Participant speaker = currentMeeting.getParticipants().get(0);
        StackPane speakerTile = createVideoTile(speaker);
        speakerViewPane.setCenter(speakerTile);

        VBox otherParticipantsVBox = new VBox(10);
        otherParticipantsVBox.setPadding(new Insets(0, 0, 0, 16));
        otherParticipantsVBox.setAlignment(Pos.TOP_CENTER);

        for (int i = 1; i < currentMeeting.getParticipants().size(); i++) {
            Participant p = currentMeeting.getParticipants().get(i);
            StackPane smallTile = createVideoTile(p);
            smallTile.setPrefSize(160, 90);
            smallTile.setMaxSize(160, 90);
            otherParticipantsVBox.getChildren().add(smallTile);
        }

        ScrollPane scrollPane = new ScrollPane(otherParticipantsVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("sidebar-scroll-pane");

        speakerViewPane.setRight(scrollPane);
    }

    private void updateVideoGrid() {
        videoGrid.getChildren().clear();
        videoGrid.getColumnConstraints().clear();
        videoGrid.getRowConstraints().clear();

        int numParticipants = currentMeeting.getParticipants().size();
        if (numParticipants == 0) return;

        int numColumns = (int) Math.ceil(Math.sqrt(numParticipants));
        int numRows = (int) Math.ceil((double) numParticipants / numColumns);

        for (int i = 0; i < numColumns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setHgrow(Priority.ALWAYS);
            videoGrid.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setVgrow(Priority.ALWAYS);
            videoGrid.getRowConstraints().add(rowConst);
        }

        int col = 0;
        int row = 0;
        for (Participant p : currentMeeting.getParticipants()) {
            videoGrid.add(createVideoTile(p), col, row);
            col++;
            if (col >= numColumns) {
                col = 0;
                row++;
            }
        }
    }

    private void updateParticipantsList() {
        participantsListVBox.getChildren().clear();
        for (Participant p : currentMeeting.getParticipants()) {
            participantsListVBox.getChildren().add(createParticipantListItem(p));
        }
    }

    @FXML
    private void toggleRightPanel() {
        rightPanelVBox.setVisible(true);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        final double panelWidth = rightPanelVBox.getPrefWidth();
        KeyValue panelTranslateValue;
        KeyValue gridPaddingValue;

        if (isPanelOpen) {
            panelTranslateValue = new KeyValue(rightPanelVBox.translateXProperty(), panelWidth);
            gridPaddingValue = new KeyValue(contentStackPane.paddingProperty(), new Insets(0, 0, 0, 0));
            timeline.setOnFinished(e -> rightPanelVBox.setVisible(false));
        } else {
            panelTranslateValue = new KeyValue(rightPanelVBox.translateXProperty(), 0);
            gridPaddingValue = new KeyValue(contentStackPane.paddingProperty(), new Insets(0, panelWidth, 0, 0));
            timeline.setOnFinished(null);
        }

        KeyFrame keyFrame = new KeyFrame(animationDuration, panelTranslateValue, gridPaddingValue);
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
        isPanelOpen = !isPanelOpen;
    }

    @FXML
    private void toggleRightPanelAndGoToChat() {
        chatToggle.setSelected(true);
        showChatView();
        if (!isPanelOpen) {
            toggleRightPanel();
        }
    }

    private void showParticipantsView() {
        participantsScrollPane.setVisible(true);
        chatVBox.setVisible(false);
    }

    private void showChatView() {
        participantsScrollPane.setVisible(false);
        chatVBox.setVisible(true);
    }

    private Node createParticipantListItem(Participant participant) {
        HBox itemBox = new HBox(12);
        itemBox.setAlignment(Pos.CENTER_LEFT);
        itemBox.setPadding(new Insets(8, 16, 8, 16));

        Label avatar = new Label(participant.getName().substring(0, 1));
        avatar.getStyleClass().add("participant-avatar");

        VBox nameBox = new VBox(-2);
        Label nameLabel = new Label(participant.getName());
        nameLabel.getStyleClass().add("participant-name");
        Label roleLabel = new Label("Member");
        roleLabel.getStyleClass().add("participant-role");
        nameBox.getChildren().addAll(nameLabel, roleLabel);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        FontIcon micIcon = new FontIcon(participant.isMuted() ? "fas-microphone-slash" : "fas-microphone");
        micIcon.getStyleClass().add("participant-list-icon");
        if (participant.isMuted()) micIcon.getStyleClass().add("icon-danger");

        FontIcon camIcon = new FontIcon(participant.isCameraOn() ? "fas-video" : "fas-video-slash");
        camIcon.getStyleClass().add("participant-list-icon");
        if (!participant.isCameraOn()) camIcon.getStyleClass().add("icon-danger");

        itemBox.getChildren().addAll(avatar, nameBox, spacer, micIcon, camIcon);
        return itemBox;
    }

    private StackPane createVideoTile(Participant participant) {
        StackPane tile = new StackPane();
        tile.getStyleClass().add("video-tile");

        if (!participant.isCameraOn()) {
            Label avatarLabel = new Label(participant.getName().substring(0, 1));
            avatarLabel.getStyleClass().add("video-tile-avatar-label");
            tile.getChildren().add(avatarLabel);
        }

        HBox nameTag = new HBox(6);
        nameTag.setAlignment(Pos.CENTER_LEFT);
        nameTag.getStyleClass().add("video-tile-nametag");
        FontIcon micIcon = new FontIcon(participant.isMuted() ? "fas-microphone-slash" : "fas-microphone");
        micIcon.getStyleClass().add("video-tile-mic-icon");
        if (participant.isMuted()) micIcon.getStyleClass().add("icon-danger");
        Label nameLabel = new Label(participant.getName());
        nameLabel.getStyleClass().add("video-tile-name-label");
        nameTag.getChildren().addAll(micIcon, nameLabel);

        StackPane.setAlignment(nameTag, Pos.BOTTOM_LEFT);
        StackPane.setMargin(nameTag, new Insets(10));
        tile.getChildren().add(nameTag);

        return tile;
    }

    @FXML
    private void leaveMeeting() {
        System.out.println("Leaving meeting: " + currentMeeting.getMeetingName());
        try {
            Scene currentScene = leaveButton.getScene();
            if (currentScene != null) {
                Parent mainRoot = FXMLLoader.load(Objects.requireNonNull(MainApp.class.getResource("view/MainView.fxml")));
                currentScene.setRoot(mainRoot);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
