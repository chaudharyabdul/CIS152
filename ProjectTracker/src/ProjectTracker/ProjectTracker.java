package ProjectTracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

/**
 * @author Abdul Chaudhary
 * 
 * Manages and tracks academic projects with enhanced functionality
 */
public class ProjectTracker {
    private DynamicArray<Project> projects;
    private LinkedList<Project> completedProjects;
    private int totalProjects;

    /**
     * Constructs a new ProjectTracker
     */
    public ProjectTracker() {
        projects = new DynamicArray<>();
        completedProjects = new LinkedList<>();
        totalProjects = 0;
    }

    /**
     * Adds a new project to the tracker
     * @param project project to add
     * @throws IllegalArgumentException if project is null
     */
    public void addProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }
        projects.add(project);
        totalProjects++;
    }

    /**
     * Updates an existing project
     * @param oldProject project to update
     * @param newProject updated project data
     * @throws IllegalArgumentException if either project is null
     */
    public void updateProject(Project oldProject, Project newProject) {
        if (oldProject == null || newProject == null) {
            throw new IllegalArgumentException("Projects cannot be null");
        }
        
        int index = -1;
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).equals(oldProject)) {
                index = i;
                break;
            }
        }
        
        if (index != -1) {
            projects.set(index, newProject);
        }
    }

    /**
     * Deletes a project from the tracker
     * @param project project to delete
     * @return true if project was found and deleted
     */
    public boolean deleteProject(Project project) {
        boolean removed = false;
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).equals(project)) {
                projects.remove(i);
                totalProjects--;
                removed = true;
                break;
            }
        }
        return removed;
    }

    /**
     * Marks a project as completed and moves it to completed projects list
     * @param project project to mark as completed
     */
    public void markProjectCompleted(Project project) {
        if (deleteProject(project)) {
            project.setCompleted(true);
            completedProjects.add(project);
            totalProjects++;
        }
    }

    /**
     * Sorts projects by due date
     */
    public void sortProjects() {
        List<Project> projectList = getProjects();
        Collections.sort(projectList, 
            (p1, p2) -> p1.getDueDate().compareTo(p2.getDueDate()));
        
        // Rebuild dynamic array
        projects = new DynamicArray<>();
        for (Project p : projectList) {
            projects.add(p);
        }
    }

    /**
     * Sorts projects by priority
     */
    public void sortByPriority() {
        List<Project> projectList = getProjects();
        Collections.sort(projectList, 
            (p1, p2) -> p1.getPriority().compareTo(p2.getPriority()));
        
        projects = new DynamicArray<>();
        for (Project p : projectList) {
            projects.add(p);
        }
    }

    /**
     * Gets all active projects
     * @return list of active projects
     */
    public List<Project> getProjects() {
        List<Project> projectList = new ArrayList<>();
        for (int i = 0; i < projects.size(); i++) {
            projectList.add(projects.get(i));
        }
        return projectList;
    }

    /**
     * Gets all completed projects
     * @return list of completed projects
     */
    public List<Project> getCompletedProjects() {
        List<Project> completed = new ArrayList<>();
        for (int i = 0; i < completedProjects.size(); i++) {
            completed.add(completedProjects.get(i));
        }
        return completed;
    }

    /**
     * Filters projects by priority level
     * @param priority priority level to filter by
     * @return list of projects with specified priority
     */
    public List<Project> filterByPriority(Project.Priority priority) {
        return getProjects().stream()
            .filter(p -> p.getPriority() == priority)
            .collect(Collectors.toList());
    }

    /**
     * Gets projects due within specified days
     * @param days number of days
     * @return list of projects due within specified days
     */
    public List<Project> getProjectsDueWithin(int days) {
        LocalDate cutoff = LocalDate.now().plusDays(days);
        return getProjects().stream()
            .filter(p -> !p.getDueDate().isAfter(cutoff))
            .collect(Collectors.toList());
    }

    /**
     * Gets overdue projects
     * @return list of overdue projects
     */
    public List<Project> getOverdueProjects() {
        return getProjects().stream()
            .filter(Project::isOverdue)
            .collect(Collectors.toList());
    }

    /**
     * Searches projects by class name
     * @param searchTerm search term
     * @return list of matching projects
     */
    public List<Project> searchByClassName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return getProjects().stream()
            .filter(p -> p.getClassName().toLowerCase()
                .contains(searchTerm.toLowerCase()))
            .collect(Collectors.toList());
    }

    /**
     * Gets total number of projects (active and completed)
     * @return total number of projects
     */
    public int getTotalProjects() {
        return totalProjects;
    }

    /**
     * Gets number of active projects
     * @return number of active projects
     */
    public int getActiveProjectCount() {
        return projects.size();
    }

    /**
     * Gets number of completed projects
     * @return number of completed projects
     */
    public int getCompletedProjectCount() {
        return completedProjects.size();
    }
}