package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ToDoModel;

import java.sql.*;

public class ToDoController {

    @FXML
    private TableColumn<?, ?> colCompletedTasks;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private ListView<CheckBox> listTask;

    @FXML
    private TableView<ToDoModel> tblTasks;

    @FXML
    private TextField txtEnterTask;

    @FXML
    private DatePicker txtDate;

    ObservableList<CheckBox> taskList = FXCollections.observableArrayList();
    @FXML
    void btnAddTaskOnClick(ActionEvent event) {
        ToDoModel task = new ToDoModel(
                txtEnterTask.getText(),
                txtDate.getValue()
        );
        CheckBox checkBox = new CheckBox(task.getTaskDescription()+" "+task.getDate());
        taskList.add(checkBox);
        listTask.setItems(taskList);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ to_do_list", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO task VALUES(?,?)");
            pstm.setString(1, txtEnterTask.getText());
            pstm.setDate(2, Date.valueOf(txtDate.getValue()));
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        txtEnterTask.clear();
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        colCompletedTasks.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        ObservableList<ToDoModel> tasksObservableList = FXCollections.observableArrayList();
        taskList.forEach(task ->{
           if(task.isSelected()){
               String fullText = task.getText();
               String[] parts = fullText.split(" ",2);
               String taskDescription = parts[0];
               String datePart = parts[1];
               tasksObservableList.add(
                       new ToDoModel(taskDescription, java.time.LocalDate.parse(datePart))
               );

           }
        });
        tblTasks.setItems(tasksObservableList);
    }

}
