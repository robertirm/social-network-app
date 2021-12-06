package com.codebase.socialnetwork.controller;

import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class PrintAllController{
    ObservableList<User> userObservableList = FXCollections.observableArrayList();
    public UserService<Long, User> userService;

    public void setService(UserService<Long, User> userService) {
        this.userService = userService;
        userObservableList.setAll(userService.getAllUsers());
    }

    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableColumn<User, Double> tableColumnUsername;
    @FXML
    TableView<User> tableViewUsers;


    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<User, Double>("username"));

        tableViewUsers.setItems(userObservableList);

//        tableColumnFirstName.textProperty().addListener(o -> handleFilter());
//        tableColumnLastName.textProperty().addListener(o -> handleFilter());
//        tableColumnUsername.textProperty().addListener(o -> handleFilter());
    }



}
