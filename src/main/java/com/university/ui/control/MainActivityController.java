package com.university.ui.control;

import com.university.comboBox.FillComboBox;
import com.university.db.control.*;
import com.university.db.entity.*;
import com.university.ui.animation.animation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class MainActivityController implements Initializable {

    /**
     * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     * Панель студенты                                                \\
     * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     */
    public AnchorPane STUDENT_P_FILTER, STUDENT_P_MAIN;
    public Label STUDENT_P_FILTER_HIDE, STUDENT_P_FILTER_SHOW, STUDENT_P_FILTER_CLEAR;
    public ComboBox STUDENT_P_FILTER_GROUP, STUDENT_P_FILTER_CHAIRS, STUDENT_P_FILTER_GROUPTYPE,
            STUDENT_P_FILTER_QUALIFICATION, STUDENT_P_FILTER_FACULTY;
    public Button STUDENT_P_CREATE, STUDENT_P_LOAD;
    private boolean STUDENT_P_IS_FILTER;
    public Label STUDENT_P_FILTER_GROUP_CLEAR, STUDENT_P_FILTER_CHAIRS_CLEAR, STUDENT_P_FILTER_GROUPTYPE_CLEAR,
            STUDENT_P_FILTER_QUALIFICATION_CLEAR;
    @FXML
    TableView<StudentsEntity> tableStudents;
    @FXML
    TableColumn<StudentsEntity, String> tableStudentsLName;
    @FXML
    TableColumn<StudentsEntity, String> tableStudentsFName;
    @FXML
    TableColumn<StudentsEntity, String> tableStudentsMName;
    @FXML
    TableColumn<StudentsEntity, String> tableStudentsGroup;
    @FXML
    TableColumn<StudentsEntity, String> tableStudentsIdCard;

    private void initStudentPane() {
        initStudentTable();

        STUDENT_P_FILTER_HIDE.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new animation().showFilter(STUDENT_P_FILTER, STUDENT_P_FILTER_SHOW, STUDENT_P_FILTER_HIDE, STUDENT_P_MAIN, false);
            }
        });

        STUDENT_P_FILTER_SHOW.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new animation().showFilter(STUDENT_P_FILTER, STUDENT_P_FILTER_SHOW, STUDENT_P_FILTER_HIDE, STUDENT_P_MAIN, true);
            }
        });

        tableStudents.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    openStudent(tableStudents.getSelectionModel().getSelectedItem().getIdstudents());
                }
            }
        });

        STUDENT_P_FILTER_CLEAR.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearStudentFilter();
            }
        });

        SEARCH.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateStudentTableByParameter();
            }
        });

        STUDENT_P_FILTER_CHAIRS.valueProperty().addListener(new ChangeListener<ChairsEntity>() {
            @Override
            public void changed(ObservableValue<? extends ChairsEntity> observable, ChairsEntity oldValue, ChairsEntity newValue) {
                updateStudentTableByParameter();
            }
        });

        STUDENT_P_FILTER_FACULTY.valueProperty().addListener(new ChangeListener<GroupsEntity>() {
            @Override
            public void changed(ObservableValue<? extends GroupsEntity> observable, GroupsEntity oldValue, GroupsEntity newValue) {
                updateStudentTableByParameter();
            }
        });

        STUDENT_P_FILTER_GROUP.valueProperty().addListener(new ChangeListener<GroupsEntity>() {
            @Override
            public void changed(ObservableValue<? extends GroupsEntity> observable, GroupsEntity oldValue, GroupsEntity newValue) {
                updateStudentTableByParameter();
            }
        });

        STUDENT_P_FILTER_GROUPTYPE.valueProperty().addListener(new ChangeListener<GrouptypeEntity>() {
            @Override
            public void changed(ObservableValue<? extends GrouptypeEntity> observable, GrouptypeEntity oldValue, GrouptypeEntity newValue) {
                updateStudentTableByParameter();
            }
        });

        STUDENT_P_FILTER_QUALIFICATION.valueProperty().addListener(new ChangeListener<QualificationlevelEntity>() {
            @Override
            public void changed(ObservableValue<? extends QualificationlevelEntity> observable, QualificationlevelEntity oldValue, QualificationlevelEntity newValue) {
                updateStudentTableByParameter();
            }
        });

        new FillComboBox(STUDENT_P_FILTER_GROUP).fillGroup();
        new FillComboBox(STUDENT_P_FILTER_CHAIRS).fillChair();
        new FillComboBox(STUDENT_P_FILTER_GROUPTYPE).fillGroupForm();
        new FillComboBox(STUDENT_P_FILTER_QUALIFICATION).fillQualificationLevel();

        tableStudents.setRowFactory(new Callback<TableView<StudentsEntity>, TableRow<StudentsEntity>>() {
            @Override
            public TableRow<StudentsEntity> call(TableView<StudentsEntity> param) {
                final TableRow<StudentsEntity> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();
                final ContextMenu tableMenu = tableStudents.getContextMenu();
                if (tableMenu != null) {
                    rowMenu.getItems().addAll(tableMenu.getItems());
                    rowMenu.getItems().add(new SeparatorMenuItem());
                }

                final MenuItem editItem = new MenuItem("Редагувати");
                editItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        openStudent(tableStudents.getSelectionModel().getSelectedItem().getIdstudents());
                    }
                });

                final MenuItem removeItem = new MenuItem("Видалити");
                removeItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        deleteStudent(tableStudents.getSelectionModel().getSelectedItem());
                    }
                });

                final MenuItem createItem = new MenuItem("Створити");
                createItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        createStudent();
                    }
                });

                rowMenu.getItems().addAll(editItem, removeItem);
                row.contextMenuProperty().bind(
                        Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                .then(rowMenu)
                                .otherwise(new ContextMenu(createItem)));
                return row;
            }
        });

        STUDENT_P_CREATE.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                createStudent();
            }
        });

        STUDENT_P_LOAD.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                loadStudent();
            }
        });

        STUDENT_P_FILTER_CHAIRS_CLEAR.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                STUDENT_P_FILTER_CHAIRS.getSelectionModel().clearSelection();
                isClearAllFilterStudent();
            }
        });

        STUDENT_P_FILTER_QUALIFICATION_CLEAR.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                STUDENT_P_FILTER_QUALIFICATION.getSelectionModel().clearSelection();
                isClearAllFilterStudent();
            }
        });

        STUDENT_P_FILTER_GROUPTYPE_CLEAR.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                STUDENT_P_FILTER_GROUPTYPE.getSelectionModel().clearSelection();
                isClearAllFilterStudent();
            }
        });

        STUDENT_P_FILTER_GROUP_CLEAR.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                STUDENT_P_FILTER_GROUP.getSelectionModel().clearSelection();
                isClearAllFilterStudent();
            }
        });

        updateStudentTable();
    }

    private void isClearAllFilterStudent() {
        if (STUDENT_P_FILTER_GROUP.getValue() == null &&
                STUDENT_P_FILTER_GROUPTYPE.getValue() == null &&
                STUDENT_P_FILTER_QUALIFICATION.getValue() == null &&
                STUDENT_P_FILTER_CHAIRS.getValue() == null &&
                STUDENT_P_FILTER_FACULTY.getValue() == null &&
                SEARCH.getText().isEmpty()) {
            STUDENT_P_IS_FILTER = false;
            updateStudentTable();
        } else {
            STUDENT_P_IS_FILTER = true;
            updateStudentTableByParameter();
        }
    }

    private void deleteStudent(StudentsEntity student) {
        if (deleteDialog())
            new dbController().delete(student);

        if (STUDENT_P_IS_FILTER)
            updateStudentTableByParameter();
        else
            updateStudentTable();
    }

    private void clearStudentFilter() {
        STUDENT_P_FILTER_FACULTY.getSelectionModel().clearSelection();
        STUDENT_P_FILTER_QUALIFICATION.getSelectionModel().clearSelection();
        STUDENT_P_FILTER_GROUP.getSelectionModel().clearSelection();
        STUDENT_P_FILTER_GROUPTYPE.getSelectionModel().clearSelection();
        STUDENT_P_FILTER_CHAIRS.getSelectionModel().clearSelection();
        SEARCH.clear();

        updateStudentTable();
        STUDENT_P_IS_FILTER = false;
    }

    private void initUserPane() {

    }

    private void updateStudentTableByParameter() {
        tableStudents.getItems().clear();
        ObservableList<StudentsEntity> students = FXCollections.observableArrayList(
                new StudentController().getStudentByParameter(SEARCH.getText(), STUDENT_P_FILTER_GROUP));

        for (Iterator<StudentsEntity> iterator = students.iterator(); iterator.hasNext(); ) {
            StudentsEntity px = iterator.next();

            if (STUDENT_P_FILTER_GROUPTYPE.getValue() != null
                    && !px.getIdgroups().getIdgroupType().equals(STUDENT_P_FILTER_GROUPTYPE.getValue())
                    || STUDENT_P_FILTER_CHAIRS.getValue() != null
                    && !px.getIdgroups().getIdchairs().equals(STUDENT_P_FILTER_CHAIRS.getValue())
                    || STUDENT_P_FILTER_QUALIFICATION.getValue() != null
                    && !px.getIdgroups().getIdqualificationLevel().equals(STUDENT_P_FILTER_QUALIFICATION.getValue()))
                iterator.remove();
        }


        tableStudents.getItems().addAll(students);
        STUDENT_P_IS_FILTER = true;
    }

    private void updateStudentTable() {
        tableStudents.getItems().clear();
        tableStudents.getItems().addAll(FXCollections.observableArrayList(new StudentController().getAllStudent()));
    }

    private void initStudentTable() {
        tableStudentsLName.setCellValueFactory(new PropertyValueFactory("lastName"));
        tableStudentsFName.setCellValueFactory(new PropertyValueFactory("firstName"));
        tableStudentsMName.setCellValueFactory(new PropertyValueFactory("middleName"));
        tableStudentsGroup.setCellValueFactory(new PropertyValueFactory("idgroups"));
        tableStudentsIdCard.setCellValueFactory(new PropertyValueFactory("studentidcard"));
    }

    private void openStudent(int id) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/studentActivity.fxml"));
            stage.setScene(new Scene((Pane) loader.load()));
            StudentActivityController controller = loader.<StudentActivityController>getController();
            controller.fillForm(id);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (STUDENT_P_IS_FILTER)
                updateStudentTableByParameter();
            else
                updateStudentTable();
        }
    }

    private void createStudent() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/studentActivity.fxml"));
            stage.setScene(new Scene((Pane) loader.load()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (STUDENT_P_IS_FILTER)
                updateStudentTableByParameter();
            else
                updateStudentTable();
        }
    }

    private void loadStudent() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/loadStudent.fxml"));
            stage.setScene(new Scene((Pane) loader.load()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (STUDENT_P_IS_FILTER)
                updateStudentTableByParameter();
            else
                updateStudentTable();
        }
    }

    /**
     * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     * Панель преподы                                                 \\
     * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     */
    public AnchorPane PROFESSOR_P_FILTER, PROFESSOR_P_MAIN;
    public Label PROFESSOR_P_FILTER_HIDE, PROFESSOR_P_FILTER_SHOW, PROFESSOR_P_FILTER_CLEAR;
    public ComboBox PROFESSOR_P_FILTER_CHAIRS, PROFESSOR_P_FILTER_FACULTY;
    public Button PROFESSOR_P_CREATE, PROFESSOR_P_LOAD;
    private boolean PROFESSOR_P_IS_FILTER;
    public Label PROFESSOR_P_FILTER_CHAIRS_CLEAR, PROFESSOR_P_FILTER_FACULTY_CLEAR;
    @FXML
    TableView<TeachersEntity> tableProfessor;
    @FXML
    TableColumn<TeachersEntity, String> tableProfessorLName;
    @FXML
    TableColumn<TeachersEntity, String> tableProfessorFName;
    @FXML
    TableColumn<TeachersEntity, String> tableProfessorMName;
    @FXML
    TableColumn<TeachersEntity, String> tableProfessorChair;

    private void initProfessorPane() {
        initProfessorTable();

        PROFESSOR_P_FILTER_HIDE.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new animation().showFilter(PROFESSOR_P_FILTER, PROFESSOR_P_FILTER_SHOW, PROFESSOR_P_FILTER_HIDE, PROFESSOR_P_MAIN, false);
            }
        });

        PROFESSOR_P_FILTER_SHOW.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new animation().showFilter(PROFESSOR_P_FILTER, PROFESSOR_P_FILTER_SHOW, PROFESSOR_P_FILTER_HIDE, PROFESSOR_P_MAIN, true);
            }
        });

        tableProfessor.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    openProfessor(tableProfessor.getSelectionModel().getSelectedItem().getIdteachers());
                }
            }
        });

        PROFESSOR_P_FILTER_CLEAR.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearProfessorFilter();
            }
        });

        SEARCH.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateProfessorTableByParameter();
            }
        });

        PROFESSOR_P_FILTER_CHAIRS.valueProperty().addListener(new ChangeListener<ChairsEntity>() {
            @Override
            public void changed(ObservableValue<? extends ChairsEntity> observable, ChairsEntity oldValue, ChairsEntity newValue) {
                updateProfessorTableByParameter();
            }
        });

        new FillComboBox(PROFESSOR_P_FILTER_CHAIRS).fillChair();

        tableProfessor.setRowFactory(new Callback<TableView<TeachersEntity>, TableRow<TeachersEntity>>() {
            @Override
            public TableRow<TeachersEntity> call(TableView<TeachersEntity> param) {
                final TableRow<TeachersEntity> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();
                final ContextMenu tableMenu = tableProfessor.getContextMenu();
                if (tableMenu != null) {
                    rowMenu.getItems().addAll(tableMenu.getItems());
                    rowMenu.getItems().add(new SeparatorMenuItem());
                }

                final MenuItem editItem = new MenuItem("Редагувати");
                editItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        openProfessor(tableProfessor.getSelectionModel().getSelectedItem().getIdteachers());
                    }
                });

                final MenuItem removeItem = new MenuItem("Видалити");
                removeItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        deleteProfessor(tableProfessor.getSelectionModel().getSelectedItem());
                    }
                });

                final MenuItem createItem = new MenuItem("Створити");
                createItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        createProfessor();
                    }
                });

                rowMenu.getItems().addAll(editItem, removeItem);
                row.contextMenuProperty().bind(
                        Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                .then(rowMenu)
                                .otherwise(new ContextMenu(createItem)));
                return row;
            }
        });

        PROFESSOR_P_CREATE.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                createProfessor();
            }
        });

        PROFESSOR_P_LOAD.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                loadProfessor();
            }
        });

        PROFESSOR_P_FILTER_CHAIRS_CLEAR.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PROFESSOR_P_FILTER_CHAIRS.getSelectionModel().clearSelection();
                isClearAllFilterProfessor();
            }
        });

        updateProfessorTable();
    }

    private void loadProfessor() {

    }

    private void createProfessor() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/professorActivity.fxml"));
            stage.setScene(new Scene((Pane) loader.load()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (PROFESSOR_P_IS_FILTER)
                updateProfessorTableByParameter();
            else
                updateProfessorTable();
        }
    }

    private void deleteProfessor(TeachersEntity teacher) {
        if (deleteDialog())
            new dbController().delete(teacher);

        if (PROFESSOR_P_IS_FILTER)
            updateProfessorTableByParameter();
        else
            updateProfessorTable();
    }

    private void updateProfessorTableByParameter() {
        tableProfessor.getItems().clear();
        ObservableList<TeachersEntity> teachers = FXCollections.observableArrayList(
                new TeacherController().getTeacherByParameter(SEARCH.getText(), PROFESSOR_P_FILTER_CHAIRS));

        tableProfessor.getItems().addAll(teachers);
        PROFESSOR_P_IS_FILTER = true;
    }

    private void clearProfessorFilter() {
        PROFESSOR_P_FILTER_FACULTY.getSelectionModel().clearSelection();
        PROFESSOR_P_FILTER_CHAIRS.getSelectionModel().clearSelection();
        SEARCH.clear();

        updateStudentTable();
        PROFESSOR_P_IS_FILTER = false;
    }

    private void openProfessor(int id) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/professorActivity.fxml"));
            stage.setScene(new Scene((Pane) loader.load()));
            ProfessorActivityController controller = loader.<ProfessorActivityController>getController();
            controller.fillForm(id);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (PROFESSOR_P_IS_FILTER)
                updateProfessorTableByParameter();
            else
                updateProfessorTable();
        }
    }

    private void updateProfessorTable() {
        tableProfessor.getItems().clear();
        tableProfessor.getItems().addAll(FXCollections.observableArrayList(new TeacherController().getAllTeacher()));
    }

    private void initProfessorTable() {
        tableProfessorLName.setCellValueFactory(new PropertyValueFactory("lastName"));
        tableProfessorFName.setCellValueFactory(new PropertyValueFactory("firstName"));
        tableProfessorMName.setCellValueFactory(new PropertyValueFactory("middleName"));
        tableProfessorChair.setCellValueFactory(new PropertyValueFactory("idchairs"));
    }

    private void isClearAllFilterProfessor() {
        if (PROFESSOR_P_FILTER_FACULTY.getValue() == null &&
                PROFESSOR_P_FILTER_CHAIRS.getValue() == null &&
                SEARCH.getText().isEmpty()) {
            PROFESSOR_P_IS_FILTER = false;
            updateProfessorTable();
        } else {
            PROFESSOR_P_IS_FILTER = true;
            updateProfessorTableByParameter();
        }
    }

    /**
     * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     * Панель темы                                                    \\
     * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     */
    public AnchorPane SUBJECT_P_FILTER, SUBJECT_P_MAIN;
    public Label SUBJECT_P_FILTER_HIDE, SUBJECT_P_FILTER_SHOW, SUBJECT_P_FILTER_CLEAR;
    public ComboBox SUBJECT_P_FILTER_FORM, SUBJECT_P_FILTER_TYPE;
    public Button SUBJECT_P_CREATE;
    private boolean SUBJECT_P_IS_FILTER;
    public Label SUBJECT_P_FILTER_FORM_CLEAR, SUBJECT_P_FILTER_TYPE_CLEAR;
    @FXML
    TableView<DiplomasubjectsEntity> tableSubject;
    @FXML
    TableColumn<DiplomasubjectsEntity, String> tableSubjectSubject;
    @FXML
    TableColumn<DiplomasubjectsEntity, String> tableSubjectDefenceDiploma;
    @FXML
    TableColumn<DiplomasubjectsEntity, String> tableSubjectType;
    @FXML
    TableColumn<DiplomasubjectsEntity, String> tableSubjectStudent;
    @FXML
    TableColumn<DiplomasubjectsEntity, String> tableSubjectCurator;
    @FXML
    TableColumn<DiplomasubjectsEntity, String> tableSubjectReviewr;
    @FXML
    TableColumn<DiplomasubjectsEntity, String> tableSubjectMarks;
    @FXML
    TableColumn<DiplomasubjectsEntity, String> tableSubjectForm;

    private void initSubjectPane() {
        initSubjectTable();

//        SUBJECT_P_FILTER_HIDE.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                new animation().showFilter(SUBJECT_P_FILTER, SUBJECT_P_FILTER_SHOW, SUBJECT_P_FILTER_HIDE, SUBJECT_P_MAIN, false);
//            }
//        });
//
//        SUBJECT_P_FILTER_SHOW.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                new animation().showFilter(SUBJECT_P_FILTER, SUBJECT_P_FILTER_SHOW, SUBJECT_P_FILTER_HIDE, SUBJECT_P_MAIN, true);
//            }
//        });

        tableSubject.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    openSubject(tableSubject.getSelectionModel().getSelectedItem());
                }
            }
        });

//        SUBJECT_P_FILTER_CLEAR.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                clearSubjectFilter();
//            }
//        });

        SEARCH.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 0) {
                    updateSubjectTableByParameter();
                    STUDENT_P_IS_FILTER = true;
                } else {
                    updateSubjectTable();
                    STUDENT_P_IS_FILTER = false;
                }
            }
        });

//        SUBJECT_P_FILTER_TYPE.valueProperty().addListener(new ChangeListener<ChairsEntity>() {
//            @Override
//            public void changed(ObservableValue<? extends ChairsEntity> observable, ChairsEntity oldValue, ChairsEntity newValue) {
//                updateSubjectTableByParameter();
//            }
//        });
//
//        SUBJECT_P_FILTER_FORM.valueProperty().addListener(new ChangeListener<ChairsEntity>() {
//            @Override
//            public void changed(ObservableValue<? extends ChairsEntity> observable, ChairsEntity oldValue, ChairsEntity newValue) {
//                updateSubjectTableByParameter();
//            }
//        });

//        new FillComboBox(SUBJECT_P_FILTER_FORM).fillDiplomaForm();
//        new FillComboBox(SUBJECT_P_FILTER_TYPE).fillDiplomaType();

        tableSubject.setRowFactory(new Callback<TableView<DiplomasubjectsEntity>, TableRow<DiplomasubjectsEntity>>() {
            @Override
            public TableRow<DiplomasubjectsEntity> call(TableView<DiplomasubjectsEntity> param) {
                final TableRow<DiplomasubjectsEntity> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();
                final ContextMenu tableMenu = tableSubject.getContextMenu();
                if (tableMenu != null) {
                    rowMenu.getItems().addAll(tableMenu.getItems());
                    rowMenu.getItems().add(new SeparatorMenuItem());
                }

                final MenuItem editItem = new MenuItem("Редагувати");
                editItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        openSubject(tableSubject.getSelectionModel().getSelectedItem());
                    }
                });

                final MenuItem removeItem = new MenuItem("Видалити");
                removeItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        deleteSubject(tableSubject.getSelectionModel().getSelectedItem());
                    }
                });

                final MenuItem createItem = new MenuItem("Створити");
                createItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        createSubject();
                    }
                });

                rowMenu.getItems().addAll(editItem, removeItem);
                row.contextMenuProperty().bind(
                        Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                .then(rowMenu)
                                .otherwise(new ContextMenu(createItem)));
                return row;
            }
        });

        SUBJECT_P_CREATE.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                createSubject();
            }
        });

//        SUBJECT_P_FILTER_CHAIRS_CLEAR.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                SUBJECT_P_FILTER_CHAIRS.getSelectionModel().clearSelection();
//                isClearAllFilterSubject();
//            }
//        });

        updateSubjectTable();
    }

    private void updateSubjectTable() {
        tableSubject.getItems().clear();
        tableSubject.getItems().addAll(FXCollections.<DiplomasubjectsEntity>observableArrayList(new SubjectController().getAllSubject()));
    }

    private void isClearAllFilterSubject() {
    }

    private void deleteSubject(DiplomasubjectsEntity subject) {
        if (deleteDialog())
            new dbController().delete(subject);

        if (SUBJECT_P_IS_FILTER)
            updateSubjectTableByParameter();
        else
            updateSubjectTable();

    }

    private void clearSubjectFilter() {
    }

    private void initSubjectTable() {
        tableSubjectSubject.setCellValueFactory(new PropertyValueFactory("subject"));
        tableSubjectCurator.setCellValueFactory(new PropertyValueFactory("curator"));
        tableSubjectDefenceDiploma.setCellValueFactory(new PropertyValueFactory("defencediploma"));
        tableSubjectMarks.setCellValueFactory(new PropertyValueFactory("mark"));
        tableSubjectReviewr.setCellValueFactory(new PropertyValueFactory("reviewer"));
        tableSubjectStudent.setCellValueFactory(new PropertyValueFactory("student"));
        tableSubjectType.setCellValueFactory(new PropertyValueFactory("type"));
        tableSubjectForm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiplomasubjectsEntity, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiplomasubjectsEntity, String> p) {
                return new SimpleStringProperty(p.getValue().getStudent().getIdgroups().getIdqualificationLevel().getName());
            }
        });
    }

    private void openSubject(DiplomasubjectsEntity diploma) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/subjectActivity.fxml"));
            stage.setScene(new Scene((Pane) loader.load()));
            SubjectActivityController controller = loader.<SubjectActivityController>getController();
            controller.fillForm(diploma);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (STUDENT_P_IS_FILTER)
                updateSubjectTableByParameter();
            else
                updateSubjectTable();
        }
    }

    private void createSubject() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/subjectActivity.fxml"));
            stage.setScene(new Scene((Pane) loader.load()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (STUDENT_P_IS_FILTER)
                updateSubjectTableByParameter();
            else
                updateSubjectTable();
        }
    }

    private void updateSubjectTableByParameter() {
        tableSubject.getItems().clear();
        tableSubject.getItems().addAll(FXCollections.<DiplomasubjectsEntity>observableArrayList(new SubjectController()
                .getSubjectByParameter(SEARCH.getText())));
    }
    /**
     *\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     *                           Панель пользователи                 \\
     *\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     */


    /**
     * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     * Основное окно                                                  \\
     * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     */
    public static boolean DELETE;
    public AnchorPane HEADER;

    public Label settings;

    //Текущий пользователь
    public Label currentUser_username;
    public Pane currentUserSettings;

    //Элементы меню
    public Pane PROFESSOR, USER, STUDENT, SUBJECT;
    //Панели
    public AnchorPane STUDENT_P, USER_P, PROFESSOR_P, SUBJECT_P;

    //Строка поиска
    public TextField SEARCH;

    //Активный пункт меню до изменения
    private AnchorPane prefPane;
    private Pane prefMenu;

    private Map<Pane, String> menu = new HashMap<Pane, String>();
    private Map<Pane, AnchorPane> pane = new HashMap<Pane, AnchorPane>();
    public Label name_window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        prefPane = SUBJECT_P;
        prefMenu = SUBJECT;
        SUBJECT_P.setVisible(true);
        prefMenu.getStyleClass().add("hover-menu");

        settings.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setting();
            }
        });

        initCurrentUser();

        initMenu();

        initSubjectPane();
    }

    private void openSettingsUser(int id) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/userSettings.fxml"));
            stage.setScene(new Scene((Pane) loader.load()));
            UserSettingsController controller = loader.<UserSettingsController>getController();
            controller.fillForm(id);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            new userController().updateCurrentUser();
            currentUser_username.setText(dbController.currentUser.toString());
        }
    }

    private void initMenu() {
        menu.put(PROFESSOR, "Викладачі");
        menu.put(USER, "Користувачі");
        menu.put(STUDENT, "Студенти");
        menu.put(SUBJECT, "Теми");

        pane.put(STUDENT, STUDENT_P);
        pane.put(USER, USER_P);
        pane.put(PROFESSOR, PROFESSOR_P);
        pane.put(SUBJECT, SUBJECT_P);

        for (final Map.Entry<Pane, String> entry : menu.entrySet())
            entry.getKey().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (!prefMenu.equals(entry.getKey())) {
                        showPane(entry.getKey());
                        initPane(entry.getKey());
                        SEARCH.clear();
                    }
                }
            });
    }

    private void showPane(Pane item) {
        prefMenu.getStyleClass().clear();
        prefMenu.getStyleClass().add("main-menu-item");
        item.getStyleClass().add("hover-menu");
        prefPane.setVisible(false);
        name_window.setText(menu.get(item));
        pane.get(item).setVisible(true);
        prefPane = pane.get(item);
        prefMenu = item;
    }

    private void initPane(Pane pane) {
        switch (pane.getId()) {
            case "STUDENT":
                initStudentPane();
                break;
            case "USER":
                initUserPane();
                break;
            case "PROFESSOR":
                initProfessorPane();
                break;
            case "SUBJECT":
                initSubjectPane();
                break;
        }
    }

    private boolean deleteDialog() {
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/deleteDialog.fxml"));
            Scene scene = new Scene((Pane) loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return DELETE;
        }
    }

    private void initCurrentUser() {
        currentUser_username.setText(dbController.currentUser.toString());

        currentUserSettings.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                openSettingsUser(dbController.currentUser.getIdusers());
            }
        });
    }

    private void setting() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/settingsActivity.fxml"));
            stage.setScene(new Scene((Pane) loader.load()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(SEARCH.getScene().getWindow());
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainActivityController() {
    }
}