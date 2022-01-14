package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.Main;
import com.codebase.socialnetwork.domain.Event;
import com.codebase.socialnetwork.domain.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

public class EventsController extends MainWindowController {

    List<Long> eventIds;
    List<Long> attendedEventsIds;
    List<Long> searchedEvents;
    int currentIndex;

    @FXML
    Button nextEventButton;
    @FXML
    Button prevEventButton;
    @FXML
    Button yourEventsButton;
    @FXML
    Button exploreButton;
    @FXML
    Button createButton;
    @FXML
    Button participationButton;
    @FXML
    ImageView eventImage;
    @FXML
    AnchorPane eventPane;
    @FXML
    AnchorPane createEventPane;
    @FXML
    AnchorPane buttonsPane;
    @FXML
    AnchorPane nothingPane;

    @FXML
    Label nameText;
    @FXML
    Label dateText;
    @FXML
    Label locationText;
    @FXML
    Label hostText;
    @FXML
    Label tagsText;
    @FXML
    Label descriptionText;

    @FXML
    TextField nameTextField;
    @FXML
    TextField locationTextField;
    @FXML
    TextField hostTextField;
    @FXML
    TextField tagsTextField;
    @FXML
    TextField descriptionTextField;

    @FXML
    Button createSuccessButton;
    @FXML
    Button cancelButton;
    @FXML
    Button uploadImageButton;
    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;
    @FXML
    ImageView uploadedImageView;
    InputStream uploadedFileInputStream;
    @FXML
    Label errorsLabel;

    @FXML
    Spinner<Integer> startHourSpinner;
    @FXML
    Spinner<Integer> startMinuteSpinner;
    @FXML
    Spinner<Integer> endHourSpinner;
    @FXML
    Spinner<Integer> endMinuteSpinner;

    @FXML
    TextField eventsSearchBar;
    @FXML
    Button eventsSearchButton;

    @FXML
    public void initialize() {
        eventIds = backEndController.getAllEventsIds();
        attendedEventsIds = backEndController.getAttendedEvents(backEndController.getCurrentUserId());
        currentIndex = 0;
        checkIndex();
        exploreButton.setVisible(false);
        createEventPane.setVisible(false);
        nothingPane.setVisible(false);
        uploadedImageView.setFitHeight(300);
        uploadedImageView.setFitWidth(140);


    }

    private void setItems(Event event){
        nameText.setText(event.getNameEvent());
        locationText.setText(event.getLocation());
        tagsText.setText(event.getTags());
        descriptionText.setText(event.getDescription());
        hostText.setText(event.getHost());
        String dateString = event.getStartingDate().toLocalDate().toString()+ " ";
        String time = event.getStartingDate().toLocalTime().toString() + " ";
        dateString += time;
        dateString += " - ";
        dateString += event.getEndingDate().toLocalDate().toString();
        dateString += " ";
        dateString += event.getEndingDate().toLocalTime().toString();
        dateText.setText(dateString);


        if(attendedEventsIds.contains(eventIds.get(currentIndex))){
            participationButton.setText("Attended!");
        }
        else{
            participationButton.setText("Attend");
        }

        Image image = new Image(event.getImageStream(),430,200,false,false);
        eventImage.setImage(image);
    }

    private void checkIndex(){
        if(eventIds.size() <= 1) showSwitchButtons(false,false);
        else if(eventIds.size() - 1 == currentIndex) {
            showSwitchButtons(true,false);
        }
        else if(currentIndex == 0){
            showSwitchButtons(false, true);
        }
        else showSwitchButtons(true, true);
        if(eventIds.size() == 0){
            nothingPane.setVisible(true);
            eventPane.setVisible(false);
            eventImage.setVisible(false);
        }
        else{
            eventPane.setVisible(true);
            eventImage.setVisible(true);
            nothingPane.setVisible(false);

            setItems(backEndController.findEvent(eventIds.get(currentIndex)));
        }
    }

    private void showSwitchButtons(boolean prev, boolean next){
        prevEventButton.setVisible(prev);
        nextEventButton.setVisible(next);
    }

    @FXML
    public void onNextEventButtonClick(){
        currentIndex++;
        checkIndex();
    }

    @FXML
    public void onPrevEventButtonClick(){
        currentIndex--;
        checkIndex();
    }

    @FXML
    public void onExploreButtonClick(){
        yourEventsButton.setVisible(true);
        exploreButton.setVisible(false);
        eventIds = backEndController.getAllEventsIds();
        currentIndex = 0;
        checkIndex();

    }

    @FXML
    public void onYourEventsButtonClick(){
        exploreButton.setVisible(true);
        yourEventsButton.setVisible(false);
        eventIds = backEndController.getAttendedEvents(backEndController.getCurrentUserId());
        currentIndex = 0;
        checkIndex();

    }

    @FXML
    public void onNewEventClick(){
        createEventPane.setVisible(true);
        eventPane.setVisible(false);
        eventImage.setVisible(false);
        buttonsPane.setVisible(false);
        eventsSearchBar.setVisible(false);
        eventsSearchButton.setVisible(false);
        showSwitchButtons(false,false);
    }

    @FXML
    public void onCreateEventButtonClick(){
        if(startDatePicker.getValue() == null ||  endDatePicker.getValue() == null){
            errorsLabel.setText("Invalid data");
            return;
        }

        String name = nameTextField.getText();
        String location = locationTextField.getText();
        String host = hostTextField.getText();
        String tags = tagsTextField.getText();
        String description = descriptionTextField.getText();

        int startHour = startHourSpinner.getValue();
        int startMinute = startMinuteSpinner.getValue();
        int endHour = endHourSpinner.getValue();
        int endMinute = endHourSpinner.getValue();

        LocalTime startTime = LocalTime.of(startHour,startMinute,0,0);
        LocalTime endTime = LocalTime.of(endHour,endMinute,0,0);
        LocalDateTime startDate = LocalDateTime.of(startDatePicker.getValue(), startTime);
        LocalDateTime endDate = LocalDateTime.of(endDatePicker.getValue(), endTime);

        try{
            backEndController.addEvent(name,startDate,endDate,location,description,tags,host,uploadedFileInputStream, backEndController.getCurrentUserId());
            onCancelButtonClick();
        }catch (ValidationException e){
            errorsLabel.setText(e.getMessage());
        }


    }

    @FXML
    public void onCancelButtonClick(){
        createEventPane.setVisible(false);
        eventPane.setVisible(true);
        eventImage.setVisible(true);
        buttonsPane.setVisible(true);
        eventsSearchBar.setVisible(true);
        eventsSearchButton.setVisible(true);

        nameTextField.clear();
        locationTextField.clear();
        hostTextField.clear();
        descriptionTextField.clear();
        tagsTextField.clear();
        uploadedImageView.setImage(null);
        uploadedFileInputStream = null;
        errorsLabel.setText("");

        checkIndex();
    }

    @FXML
    public void onUploadImageButtonClick() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File file = fileChooser.showOpenDialog(Main.mainStage);

        if( file != null ){
            uploadedFileInputStream = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            uploadedFileInputStream.transferTo(baos);
            uploadedImageView.setImage(new Image(new ByteArrayInputStream(baos.toByteArray())));
            uploadedFileInputStream = new ByteArrayInputStream(baos.toByteArray());
        }
    }

    @FXML
    public void onAttendButtonClick(){
        if(participationButton.getText().equals("Attend")){
            backEndController.addParticipant(backEndController.getCurrentUserId(), eventIds.get(currentIndex));
            attendedEventsIds.add(eventIds.get(currentIndex));
            participationButton.setText("Attended!");
        }
        else{
            backEndController.deleteParticipant(backEndController.getCurrentUserId(), eventIds.get(currentIndex));
            participationButton.setText("Attend");
            attendedEventsIds.removeIf(aLong -> aLong.equals(eventIds.get(currentIndex)));
        }
    }

    @FXML void onSearchEventsButtonClick(){
        String text = eventsSearchBar.getText();

        yourEventsButton.setVisible(true);
        exploreButton.setVisible(false);
        eventIds = backEndController.getEventsByName(text);
        currentIndex = 0;
        checkIndex();

        eventsSearchBar.clear();
    }

}
