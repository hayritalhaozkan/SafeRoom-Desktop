package com.saferoom.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import com.saferoom.MainApp;
import com.saferoom.model.Meeting;
import com.saferoom.model.UserRole; // Düzeltme için import eklendi
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class JoinMeetController {

    @FXML private VBox rootPane;
    @FXML private JFXButton backButton;
    @FXML private TextField roomIdField;
    @FXML private Button joinButton;
    @FXML private JFXComboBox<String> audioInputBox;
    @FXML private JFXComboBox<String> audioOutputBox;
    @FXML private JFXComboBox<String> cameraSourceBox;
    @FXML private JFXToggleButton cameraToggle;
    @FXML private JFXToggleButton micToggle;
    @FXML private ProgressBar micTestBar;
    @FXML private JFXSlider inputVolumeSlider;
    @FXML private JFXSlider outputVolumeSlider;

    private Timeline micAnimation;
    private Scene returnScene;

    public void setReturnScene(Scene scene) {
        this.returnScene = scene;
    }

    @FXML
    public void initialize() {
        audioInputBox.getItems().addAll("Default - MacBook Pro Microphone", "External USB Mic");
        audioOutputBox.getItems().addAll("Default - MacBook Pro Speakers", "Bluetooth Headphones");
        cameraSourceBox.getItems().addAll("FaceTime HD Camera", "External Webcam");

        audioInputBox.getSelectionModel().selectFirst();
        audioOutputBox.getSelectionModel().selectFirst();
        cameraSourceBox.getSelectionModel().selectFirst();

        backButton.setOnAction(event -> handleBack());
        joinButton.setOnAction(event -> handleJoin());

        startMicTestAnimation();
    }

    private void handleJoin() {
        if (micAnimation != null) micAnimation.stop();
        if (returnScene != null) {
            try {
                FXMLLoader meetingLoader = new FXMLLoader(MainApp.class.getResource("view/MeetingPanelView.fxml"));
                Parent meetingRoot = meetingLoader.load();
                MeetingPanelController meetingController = meetingLoader.getController();
                String roomName = roomIdField.getText().isEmpty() ? "Joined Room" : roomIdField.getText();
                Meeting meetingToJoin = new Meeting(roomIdField.getText(), roomName);

                // DÜZELTME: initData metoduna UserRole.USER parametresi eklendi.
                meetingController.initData(meetingToJoin, UserRole.USER);

                returnScene.setRoot(meetingRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startMicTestAnimation() {
        micAnimation = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            Random random = new Random();
            micTestBar.setProgress(random.nextDouble() * 0.7);
        }));
        micAnimation.setCycleCount(Animation.INDEFINITE);
        micAnimation.play();
    }

    private void handleBack() {
        if (micAnimation != null) micAnimation.stop();
        if (returnScene != null) {
            try {
                Parent mainRoot = FXMLLoader.load(Objects.requireNonNull(MainApp.class.getResource("view/MainView.fxml")));
                returnScene.setRoot(mainRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}