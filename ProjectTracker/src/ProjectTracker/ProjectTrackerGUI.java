package ProjectTracker;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.format.DateTimeFormatter;

public class ProjectTrackerGUI extends Application {
    private ProjectTracker tracker;
    private ListView<Project> projectListView;
    private TextField classNameField;
    private TextField dueDateField;
    private TextArea descriptionArea;
    private ComboBox<Project.Priority> priorityComboBox;
    private TextField timeEstimateField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        tracker = new ProjectTracker();
        
        // Create main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        // Create input section
        GridPane inputGrid = createInputGrid();
        
        // Create buttons
        HBox buttonBox = createButtonBox();
        
        // Create list view
        projectListView = new ListView<>();
        projectListView.setPrefHeight(400);
        
        // Create filter section
        HBox filterBox = createFilterBox();
        
        // Create status section
        statusLabel = new Label("No projects");
        
        // Add all components to main layout
        mainLayout.getChildren().addAll(
            new Label("Project Management System"),
            inputGrid,
            buttonBox,
            filterBox,
            projectListView,
            statusLabel
        );

        // Create scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle("Project Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add context menu to list view
        addContextMenu();
        
        // Update status
        updateStatus();
    }

    private GridPane createInputGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        // Class name input
        grid.add(new Label("Class Name:"), 0, 0);
        classNameField = new TextField();
        grid.add(classNameField, 1, 0);

        // Due date input
        grid.add(new Label("Due Date (MM/dd/yyyy):"), 0, 1);
        dueDateField = new TextField();
        dueDateField.setPromptText("Example: 12/31/2024");
        grid.add(dueDateField, 1, 1);

        // Priority input
        grid.add(new Label("Priority:"), 0, 2);
        priorityComboBox = new ComboBox<>(
            FXCollections.observableArrayList(Project.Priority.values())
        );
        priorityComboBox.setValue(Project.Priority.MEDIUM);
        grid.add(priorityComboBox, 1, 2);

        // Time estimate input
        grid.add(new Label("Time Estimate (hours):"), 0, 3);
        timeEstimateField = new TextField();
        timeEstimateField.setPromptText("Enter estimated hours");
        grid.add(timeEstimateField, 1, 3);

        // Description input
        grid.add(new Label("Description:"), 0, 4);
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);
        grid.add(descriptionArea, 1, 4);

        return grid;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));

        Button addButton = new Button("Add Project");
        addButton.setOnAction(e -> addProject());

        Button clearButton = new Button("Clear Fields");
        clearButton.setOnAction(e -> clearFields());

        buttonBox.getChildren().addAll(addButton, clearButton);
        return buttonBox;
    }

    private HBox createFilterBox() {
        HBox filterBox = new HBox(10);
        filterBox.setPadding(new Insets(10));

        ComboBox<String> sortComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "Sort by Due Date", "Sort by Priority"
        ));
        sortComboBox.setOnAction(e -> {
            if (sortComboBox.getValue().equals("Sort by Due Date")) {
                tracker.sortProjects();
            } else {
                tracker.sortByPriority();
            }
            updateProjectList();
        });

        ComboBox<String> filterComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "All Projects", "High Priority", "Due Within Week", "Overdue"
        ));
        filterComboBox.setValue("All Projects");
        filterComboBox.setOnAction(e -> filterProjects(filterComboBox.getValue()));

        TextField searchField = new TextField();
        searchField.setPromptText("Search by class name");
        searchField.textProperty().addListener((obs, old, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                ObservableList<Project> filtered = FXCollections.observableArrayList(
                    tracker.searchByClassName(newValue)
                );
                projectListView.setItems(filtered);
            } else {
                updateProjectList();
            }
        });

        filterBox.getChildren().addAll(sortComboBox, filterComboBox, searchField);
        return filterBox;
    }

    private void addContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(e -> editProject(projectListView.getSelectionModel().getSelectedItem()));
        
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> deleteProject(projectListView.getSelectionModel().getSelectedItem()));
        
        MenuItem completeItem = new MenuItem("Mark Complete");
        completeItem.setOnAction(e -> markProjectComplete(projectListView.getSelectionModel().getSelectedItem()));
        
        contextMenu.getItems().addAll(editItem, deleteItem, completeItem);
        projectListView.setContextMenu(contextMenu);
    }

    private void addProject() {
        try {
            Project project = new Project(
                classNameField.getText(),
                dueDateField.getText(),
                descriptionArea.getText()
            );
            project.setPriority(priorityComboBox.getValue());
            project.setTimeEstimate(timeEstimateField.getText());
            
            tracker.addProject(project);
            updateProjectList();
            clearFields();
            showAlert("Project added successfully!", Alert.AlertType.INFORMATION);
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void editProject(Project project) {
        if (project == null) return;

        Dialog<Project> dialog = new Dialog<>();
        dialog.setTitle("Edit Project");
        dialog.setHeaderText("Edit Project Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField className = new TextField(project.getClassName());
        TextField dueDate = new TextField(
            project.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        );
        TextArea description = new TextArea(project.getDescription());
        ComboBox<Project.Priority> priority = new ComboBox<>(
            FXCollections.observableArrayList(Project.Priority.values())
        );
        priority.setValue(project.getPriority());
        TextField timeEstimate = new TextField(project.getTimeEstimate());

        grid.add(new Label("Class Name:"), 0, 0);
        grid.add(className, 1, 0);
        grid.add(new Label("Due Date:"), 0, 1);
        grid.add(dueDate, 1, 1);
        grid.add(new Label("Priority:"), 0, 2);
        grid.add(priority, 1, 2);
        grid.add(new Label("Time Estimate:"), 0, 3);
        grid.add(timeEstimate, 1, 3);
        grid.add(new Label("Description:"), 0, 4);
        grid.add(description, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    Project newProject = new Project(
                        className.getText(),
                        dueDate.getText(),
                        description.getText()
                    );
                    newProject.setPriority(priority.getValue());
                    newProject.setTimeEstimate(timeEstimate.getText());
                    tracker.updateProject(project, newProject);
                    updateProjectList();
                    return newProject;
                } catch (IllegalArgumentException e) {
                    showAlert(e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteProject(Project project) {
        if (project == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Project");
        alert.setHeaderText("Delete Project");
        alert.setContentText("Are you sure you want to delete this project?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                tracker.deleteProject(project);
                updateProjectList();
                updateStatus();
            }
        });
    }

    private void markProjectComplete(Project project) {
        if (project == null) return;
        tracker.markProjectCompleted(project);
        updateProjectList();
        updateStatus();
    }

    private void filterProjects(String filter) {
        ObservableList<Project> filtered;
        switch (filter) {
            case "High Priority":
                filtered = FXCollections.observableArrayList(
                    tracker.filterByPriority(Project.Priority.HIGH)
                );
                break;
            case "Due Within Week":
                filtered = FXCollections.observableArrayList(
                    tracker.getProjectsDueWithin(7)
                );
                break;
            case "Overdue":
                filtered = FXCollections.observableArrayList(
                    tracker.getOverdueProjects()
                );
                break;
            default:
                filtered = FXCollections.observableArrayList(tracker.getProjects());
        }
        projectListView.setItems(filtered);
    }

    private void updateProjectList() {
        projectListView.setItems(
            FXCollections.observableArrayList(tracker.getProjects())
        );
        updateStatus();
    }

    private void clearFields() {
        classNameField.clear();
        dueDateField.clear();
        descriptionArea.clear();
        priorityComboBox.setValue(Project.Priority.MEDIUM);
        timeEstimateField.clear();
    }

    private void updateStatus() {
        statusLabel.setText(String.format(
            "Total Projects: %d | Active: %d | Completed: %d",
            tracker.getTotalProjects(),
            tracker.getActiveProjectCount(),
            tracker.getCompletedProjectCount()
        ));
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}