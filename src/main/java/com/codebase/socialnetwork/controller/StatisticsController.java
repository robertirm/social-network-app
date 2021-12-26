package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.Main;
import com.codebase.socialnetwork.domain.FriendDTO;
import com.codebase.socialnetwork.domain.Message;
import com.codebase.socialnetwork.domain.User;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

public class StatisticsController extends MainWindowController {


    ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    Pane activityPane;
    @FXML
    Pane historyPane;

    @FXML
    PieChart pieChart;
    @FXML
    Button viewPieChartButton;
    @FXML
    DatePicker activityStartDatePicker;
    @FXML
    DatePicker activityEndDatePicker;


    @FXML
    BarChart<String,Integer> barChart;
    @FXML
    NumberAxis yAxis;
    @FXML
    CategoryAxis xAxis;
    @FXML
    Button viewBarChartButton;
    @FXML
    DatePicker historyStartDatePicker;
    @FXML
    DatePicker historyEndDatePicker;
    @FXML
    ComboBox<User> historyComboBox;

    @FXML
    Button saveActivityPdfButton;
    @FXML
    Button saveHistoryPdfButton;

    private void showPieChart(boolean value){
        pieChart.setVisible(value);
        saveActivityPdfButton.setVisible(value);
        if(value){
            activityPane.setTranslateY(-10);
        }
        else{
            activityPane.setTranslateY(100);

        }
    }

    private void showBarChart(boolean value){
        barChart.setVisible(value);
        saveHistoryPdfButton.setVisible(value);
        if(value){
            historyPane.setTranslateY(-10);
        }else {
            historyPane.setTranslateY(100);
        }
    }


    @FXML
    public void initialize() {
        xAxis.setAnimated(false); // there's a known bug with the animation
        yAxis.setAnimated(false);

        showPieChart(false);
        showBarChart(false);

        List<User> userList = backEndController.getAllFriendsForUser(backEndController.getCurrentUsername()).stream()
                        .map(FriendDTO::getUser)
                        .collect(Collectors.toList());
        users.setAll(userList);
        historyComboBox.setItems(users);
    }


    @FXML
    public void onViewActivityButton(){
        if(activityStartDatePicker.getValue() == null ||  activityEndDatePicker.getValue() == null)
            return;

        LocalTime time = LocalTime.of(0,0,0,0);

        LocalDateTime startDate = LocalDateTime.of(activityStartDatePicker.getValue(), time);
        LocalDateTime endDate = LocalDateTime.of(activityEndDatePicker.getValue(), time);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Sent\n Messages", backEndController.getNumberOfSentMessages(startDate,endDate)),
                        new PieChart.Data("Received\n Messages", backEndController.getNumberOfReceivedMessages(startDate,endDate)),
                        new PieChart.Data("New Friends", backEndController.getAllFriendsForUser(backEndController.getCurrentUsername()).size())
                );

        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " : ", data.pieValueProperty().getValue().intValue()
                        )
                )
        );

        pieChart.getData().setAll(pieChartData);
        pieChart.setTitle("Your activity between " + activityStartDatePicker.getValue() +" - "+ activityEndDatePicker.getValue());

        showPieChart(true);
    }

    @FXML
    public void onViewHistoryButton(){
        if(historyStartDatePicker.getValue() == null ||  historyEndDatePicker.getValue() == null || historyComboBox.getValue() == null)
            return;

        LocalTime time = LocalTime.of(0,0,0,0);

        LocalDateTime startDate = LocalDateTime.of(historyStartDatePicker.getValue(), time);
        LocalDateTime endDate = LocalDateTime.of(historyEndDatePicker.getValue(), time);

        User selectedUser = historyComboBox.getValue();

        List<Message> messages = backEndController.getMessagesFromFriend(selectedUser,startDate,endDate);
        System.out.println(messages.size());

        barChart.getData().clear();
        Map<LocalDate, Integer> dates = new TreeMap<>();
        messages.forEach(msg-> dates.merge(msg.getMessageDate().toLocalDate(), 1, Integer::sum));

        barChart.setTitle("History of your messages with " + selectedUser.getUsername() + "\n   between " + historyStartDatePicker.getValue() +" - "+ historyEndDatePicker.getValue() );
        xAxis.setLabel("Date");
        yAxis.setLabel("Nr. of Messages");

        XYChart.Series<String,Integer> series = new XYChart.Series();
        series.setName("Number of Messages");

        dates.forEach((k,v)-> series.getData().add(new XYChart.Data(k.toString(), v)));

        barChart.getData().add(series);

        showBarChart(true);
    }

    @FXML
    public void onSaveActivityPdfButton() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(Main.mainStage);

        if(selectedDirectory == null)
            return;

        int textX = 100;
        int textY = 700;
        if(activityStartDatePicker.getValue() == null ||  activityEndDatePicker.getValue() == null)
            return;

        LocalTime time = LocalTime.of(0,0,0,0);

        LocalDateTime startDate = LocalDateTime.of(activityStartDatePicker.getValue(), time);
        LocalDateTime endDate = LocalDateTime.of(activityEndDatePicker.getValue(), time);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage( page );

        PDFont font = PDType1Font.HELVETICA_BOLD;
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setFont( font, 10 );
        contentStream.newLineAtOffset(textX,textY);
        contentStream.showText("number of sent messages : " +backEndController.getNumberOfSentMessages(startDate,endDate));
        contentStream.endText();

        textY += 20;

        contentStream.beginText();
        contentStream.setFont( font, 10 );
        contentStream.newLineAtOffset(textX,textY);
        contentStream.showText("number of received messages : " +backEndController.getNumberOfReceivedMessages(startDate,endDate));
        contentStream.endText();

        textY += 20;

        contentStream.beginText();
        contentStream.setFont( font, 10 );
        contentStream.newLineAtOffset(textX,textY);
        contentStream.showText("number of sent messages : " +backEndController.getAllFriendsForUser(backEndController.getCurrentUsername()).size());
        contentStream.endText();

        contentStream.close();

        document.save( selectedDirectory+"/"+backEndController.getCurrentUsername()+ "_activityReport.pdf");
        document.close();


    }

    @FXML
    public void onSaveHistoryPdfButton() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(Main.mainStage);

        if(selectedDirectory == null)
            return;

        int textX = 100;
        AtomicInteger textY = new AtomicInteger(700);

        LocalTime time = LocalTime.of(0,0,0,0);
        LocalDateTime startDate = LocalDateTime.of(historyStartDatePicker.getValue(), time);
        LocalDateTime endDate = LocalDateTime.of(historyEndDatePicker.getValue(), time);
        User selectedUser = historyComboBox.getValue();
        List<Message> messages = backEndController.getMessagesFromFriend(selectedUser,startDate,endDate);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage( page );

        PDFont font1 = PDType1Font.HELVETICA_BOLD;
        PDFont font2 = PDType1Font.HELVETICA;
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        //WritableImage image = barChart.snapshot(new SnapshotParameters(), null);

        contentStream.beginText();
        contentStream.setFont(font1, 20 );
        contentStream.newLineAtOffset(textX,textY.get());
        contentStream.showText("History of messages from " + selectedUser.getUsername());
        contentStream.endText();
        textY.addAndGet(-20);

        messages.forEach(x -> {
            try {
                contentStream.beginText();
                contentStream.setFont( font2, 10 );
                contentStream.newLineAtOffset(textX-20,textY.get());
                contentStream.showText(x.getMessageDate().format(DATE_TIME_FORMATTER) + " : " +  x.getMessageContent());
                contentStream.endText();
                textY.addAndGet(-10);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        contentStream.close();

        document.save( selectedDirectory+"/"+backEndController.getCurrentUsername()+ "_historyReport.pdf");
        document.close();


    }
}
