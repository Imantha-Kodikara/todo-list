package model;

import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ToDoModel {
    private String taskDescription;
    private LocalDate date;
}
