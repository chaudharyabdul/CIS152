package ProjectTracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author [Abdul Chaudhary]
 * 
 * Represents a student project with enhanced tracking capabilities
 */
public class Project {
    private String className;
    private LocalDate dueDate;
    private String description;
    private boolean isCompleted;
    private Priority priority;
    private String timeEstimate;
    private String actualTime;
    private LocalDate lastModified;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Enum for priority levels
    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    /**
     * Constructs a new Project with specified details
     * @param className the name of the class
     * @param dueDate the due date in format MM/dd/yyyy
     * @param description project description
     * @throws IllegalArgumentException if inputs are invalid
     */
    public Project(String className, String dueDate, String description) {
        validateInputs(className, dueDate, description);
        this.className = className;
        this.dueDate = LocalDate.parse(dueDate, DATE_FORMATTER);
        this.description = description;
        this.isCompleted = false;
        this.priority = Priority.MEDIUM;
        this.timeEstimate = "0";
        this.actualTime = "0";
        this.lastModified = LocalDate.now();
    }

    /**
     * Validates all input parameters
     * @throws IllegalArgumentException if any input is invalid
     */
    private void validateInputs(String className, String dueDate, String description) {
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        try {
            LocalDate date = LocalDate.parse(dueDate, DATE_FORMATTER);
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Due date cannot be in the past");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use MM/dd/yyyy");
        }
    }

    // Getters and setters with validation
    public String getClassName() { 
        return className; 
    }

    public void setClassName(String className) {
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be empty");
        }
        this.className = className;
        updateLastModified();
    }

    public LocalDate getDueDate() { 
        return dueDate; 
    }

    public void setDueDate(String dueDate) {
        try {
            LocalDate newDate = LocalDate.parse(dueDate, DATE_FORMATTER);
            if (newDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Due date cannot be in the past");
            }
            this.dueDate = newDate;
            updateLastModified();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use MM/dd/yyyy");
        }
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        this.description = description;
        updateLastModified();
    }

    public boolean isCompleted() { 
        return isCompleted; 
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
        updateLastModified();
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        updateLastModified();
    }

    public String getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(String timeEstimate) {
        if (!timeEstimate.matches("\\d+")) {
            throw new IllegalArgumentException("Time estimate must be a positive number");
        }
        this.timeEstimate = timeEstimate;
        updateLastModified();
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        if (!actualTime.matches("\\d+")) {
            throw new IllegalArgumentException("Actual time must be a positive number");
        }
        this.actualTime = actualTime;
        updateLastModified();
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    private void updateLastModified() {
        this.lastModified = LocalDate.now();
    }

    /**
     * Calculates days remaining until due date
     * @return number of days until due date
     */
    public long getDaysRemaining() {
        return LocalDate.now().until(dueDate).getDays();
    }

    /**
     * Checks if project is overdue
     * @return true if project is overdue and not completed
     */
    public boolean isOverdue() {
        return !isCompleted && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return String.format("Class: %s | Due: %s | Priority: %s | Status: %s | Time Est: %sh | Actual: %sh | Description: %s",
                className,
                dueDate.format(DATE_FORMATTER),
                priority,
                (isCompleted ? "Completed" : isOverdue() ? "OVERDUE" : "Pending"),
                timeEstimate,
                actualTime,
                description);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Project other = (Project) obj;
        return className.equals(other.className) &&
               dueDate.equals(other.dueDate) &&
               description.equals(other.description);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + className.hashCode();
        result = 31 * result + dueDate.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}