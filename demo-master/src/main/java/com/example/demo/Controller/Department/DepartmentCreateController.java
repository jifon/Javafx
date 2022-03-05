package com.example.demo.Controller.Department;

import com.example.demo.Main;
import com.example.demo.Model.Department;
import com.example.demo.Model.Employee;
import com.example.demo.Repository.DepartmentRepository;
import com.example.demo.Repository.EmployeeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;

public class DepartmentCreateController {


    final DepartmentRepository departmentRepository = new DepartmentRepository();
    final EmployeeRepository employeeRepository = new EmployeeRepository();

    @FXML
    public TextField Dname;
    @FXML
    public TextField Dnumber;
    @FXML
    public DatePicker Mgr_start_date;
    @FXML
    public ComboBox<Employee> Mgr_ssn;

    @FXML
    private void initialize(){
        initializeFields();
    }

    public void initializeFields(){
        ObservableList<Employee> eList = employeeRepository.getList();
        Mgr_ssn.setItems(eList);

        Mgr_ssn.setConverter(new StringConverter<Employee>(){

            @Override
            public String toString(Employee object) {
                return object == null ? null : object.getFname();
            }

            @Override
            public Employee fromString(String string) {
                return Mgr_ssn.getItems().stream().filter(i -> i.getFname().equals(string)).findAny().orElse(null);
            }

        });

        Mgr_ssn.setCellFactory(new Callback<ListView<Employee>, ListCell<Employee>>(){

            @Override
            public ListCell<Employee> call(ListView<Employee> p) {

                final ListCell<Employee> cell = new ListCell<Employee>(){

                    @Override
                    protected void updateItem(Employee t, boolean bln) {
                        super.updateItem(t, bln);

                        if(t != null){
                            setText(t.getFname() + ":  " + t.getSsn());
                        }else{
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });
    }

    /**
     * Метод -insertPerson(ActionEvent actionEvent)- привязан к кнопке -Создать- на главной странице
     * при нажатии которой из текста полей -Fname, Lname, Ssn...- формируется
     * запрос добавления(INSERT) в базу данных.
     *
     * Далее значения полей опустошаются и вызывается метод
     * -initializeTableValues();- для заполнения таблицы новыми данными из Базы данных.
     *
     * @param actionEvent
     */
    public void insertDepartment(ActionEvent actionEvent) {
        departmentRepository.updateDepartment(
                Dname.getText(),
                Dnumber.getText(),
                String.valueOf(Mgr_start_date.getValue()),
                Mgr_ssn.getValue().getSsn());

        openDepartmentPage(actionEvent);
    }

    public void openDepartmentPage(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("departmentTable.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            stage.setTitle("База отделов");
            stage.setScene(scene);
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
