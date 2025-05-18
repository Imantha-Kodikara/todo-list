package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ToDoModel;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ToDoController implements Initializable {

    @FXML
    private TableColumn<?, ?> colCompletedTasks;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private ListView<CheckBox> listTask;

    @FXML
    private TableView<ToDoModel> tblTasks;

    @FXML
    private TextField txtEnterTask;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtTime;

    ObservableList<CheckBox> taskList = FXCollections.observableArrayList();
    @FXML
    void btnAddTaskOnClick(ActionEvent event) {
        ToDoModel task = new ToDoModel(
                txtEnterTask.getText(),
                txtDate.getValue(),
                txtTime.getText()
        );
        CheckBox checkBox = new CheckBox(task.getTaskDescription()+" | "+task.getDate()+" | "+task.getTime());
        taskList.add(checkBox);
        listTask.setItems(taskList);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO task VALUES(?,?,?,?)");
            pstm.setString(1, txtEnterTask.getText());
            pstm.setDate(2, Date.valueOf(txtDate.getValue()));
            pstm.setString(3, txtTime.getText());
            pstm.setBoolean(4, false);

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtEnterTask.clear();
    }
    ObservableList<ToDoModel> tasksObservableList = FXCollections.observableArrayList();
    @FXML
    void btnReloadOnAction(ActionEvent event) {
        colCompletedTasks.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));


        taskList.forEach(task ->{
           if(task.isSelected()){
               String fullText = task.getText();
               String[] parts = fullText.split(" \\| ",3);
               String taskDescription = parts[0];
               String datePart = parts[1];
               String timePart = parts[2];
               tasksObservableList.add(
                       new ToDoModel(taskDescription, java.time.LocalDate.parse(datePart), timePart)
               );

               try {
                   Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "1234");
                   String sql = "UPDATE task SET status = true WHERE taskDescription = ? AND date = ? AND time = ?";

                   PreparedStatement pstm = connection.prepareStatement(sql);
                   pstm.setString(1, taskDescription);
                   pstm.setDate(2, Date.valueOf(datePart));
                   pstm.setString(3, timePart);

                   pstm.executeUpdate();
               } catch (SQLException e) {
                   throw new RuntimeException(e);
               }


           }
        });
        tblTasks.setItems(tasksObservableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCompletedTasks.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        ObservableList<ToDoModel> completedTasks = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list", "root", "1234");
            String sql = "SELECT * FROM task WHERE status = true";
            PreparedStatement pstm = connection.prepareStatement(sql);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()){
                String description = resultSet.getString("taskDescription");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                String time = resultSet.getString("time");

                completedTasks.add(new ToDoModel(description, date, time));
            }
            tblTasks.setItems(completedTasks);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
