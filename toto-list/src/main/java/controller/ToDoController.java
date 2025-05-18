package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ToDoController {

    @FXML
    private ListView<CheckBox> listTask;

    @FXML
    private TextField txtEnterTask;

    ObservableList<CheckBox>tasks = FXCollections.observableArrayList();
    @FXML
    void btnAddTaskOnClick(ActionEvent event) {
        String taskText = txtEnterTask.getText();
        CheckBox checkBox = new CheckBox(taskText);
        tasks.add((checkBox));
        listTask.setItems(tasks);
        txtEnterTask.clear();

    }

}
