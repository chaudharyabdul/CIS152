package ProjectTracker;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import ProjectTracker.Project;
import ProjectTracker.ProjectTracker;

/**
 * @author Abdul Chaudhary
 * 
 * Test class for ProjectTracker application
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectTrackerTest {
    private ProjectTracker tracker;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @BeforeEach
    void setUp() {
        tracker = new ProjectTracker();
    }

    @Test
    @Order(1)
    @DisplayName("Test Project Creation")
    void testProjectCreation() {
        Project project = new Project("Test Class", "12/31/2024", "Test Description");
        assertNotNull(project);
        assertEquals("Test Class", project.getClassName());
        assertEquals("Test Description", project.getDescription());
        assertEquals(Project.Priority.MEDIUM, project.getPriority());
        assertFalse(project.isCompleted());
    }
    
    @Test
    @Order(2)
    @DisplayName("Test Invalid Project Creation")
    void testInvalidProjectCreation() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Project("", "12/31/2024", "Description"));
        assertThrows(IllegalArgumentException.class, () -> 
            new Project("Class", "13/31/2024", "Description"));
        assertThrows(IllegalArgumentException.class, () -> 
            new Project("Class", "12/31/2024", ""));
    }

    @Test
    @Order(3)
    @DisplayName("Test Project Addition")
    void testProjectAddition() {
        Project project = new Project("Test Class", "12/31/2024", "Description");
        tracker.addProject(project);
        assertEquals(1, tracker.getActiveProjectCount());
        assertTrue(tracker.getProjects().contains(project));
    }

    @Test
    @Order(4)
    @DisplayName("Test Project Update")
    void testProjectUpdate() {
        Project oldProject = new Project("Old Class", "12/31/2024", "Old Description");
        Project newProject = new Project("New Class", "12/31/2024", "New Description");
        tracker.addProject(oldProject);
        tracker.updateProject(oldProject, newProject);
        
        List<Project> projects = tracker.getProjects();
        assertTrue(projects.contains(newProject));
        assertFalse(projects.contains(oldProject));
    }

    @Test
    @Order(5)
    @DisplayName("Test Project Deletion")
    void testProjectDeletion() {
        Project project = new Project("Test Class", "12/31/2024", "Description");
        tracker.addProject(project);
        assertTrue(tracker.deleteProject(project));
        assertEquals(0, tracker.getActiveProjectCount());
    }

    @Test
    @Order(6)
    @DisplayName("Test Project Completion")
    void testProjectCompletion() {
        Project project = new Project("Test Class", "12/31/2024", "Description");
        tracker.addProject(project);
        tracker.markProjectCompleted(project);
        
        assertEquals(0, tracker.getActiveProjectCount());
        assertEquals(1, tracker.getCompletedProjectCount());
        assertTrue(project.isCompleted());
    }

    @Test
    @Order(7)
    @DisplayName("Test Priority Filtering")
    void testPriorityFiltering() {
        Project highPriority = new Project("High", "12/31/2024", "Description");
        highPriority.setPriority(Project.Priority.HIGH);
        Project lowPriority = new Project("Low", "12/31/2024", "Description");
        lowPriority.setPriority(Project.Priority.LOW);
        
        tracker.addProject(highPriority);
        tracker.addProject(lowPriority);
        
        List<Project> highPriorityProjects = tracker.filterByPriority(Project.Priority.HIGH);
        assertEquals(1, highPriorityProjects.size());
        assertTrue(highPriorityProjects.contains(highPriority));
    }

    @Test
    @Order(8)
    @DisplayName("Test Due Date Filtering")
    void testDueDateFiltering() {
        Project soonDue = new Project("Soon", "12/15/2024", "Description");
        Project laterDue = new Project("Later", "12/31/2024", "Description");
        
        tracker.addProject(soonDue);
        tracker.addProject(laterDue);
        
        List<Project> dueWithinWeek = tracker.getProjectsDueWithin(7);
        assertTrue(dueWithinWeek.contains(soonDue));
        assertFalse(dueWithinWeek.contains(laterDue));
    }

    @Test
    @Order(9)
    @DisplayName("Test Project Search")
    void testProjectSearch() {
        Project project1 = new Project("Java Class", "12/31/2024", "Description");
        Project project2 = new Project("Python Class", "12/31/2024", "Description");
        
        tracker.addProject(project1);
        tracker.addProject(project2);
        
        List<Project> searchResults = tracker.searchByClassName("Java");
        assertEquals(1, searchResults.size());
        assertTrue(searchResults.contains(project1));
    }

    @Test
    @Order(10)
    @DisplayName("Test Project Sorting")
    void testProjectSorting() {
        Project project1 = new Project("A", "12/31/2024", "Description");
        Project project2 = new Project("B", "12/15/2024", "Description");
        
        tracker.addProject(project1);
        tracker.addProject(project2);
        tracker.sortProjects();
        
        List<Project> sorted = tracker.getProjects();
        assertEquals(project2, sorted.get(0)); // Earlier due date should be first
    }

    @Test
    @Order(11)
    @DisplayName("Test Priority Sorting")
    void testPrioritySorting() {
        Project mediumPriority = new Project("Medium", "12/31/2024", "Description");
        Project highPriority = new Project("High", "12/31/2024", "Description");
        highPriority.setPriority(Project.Priority.HIGH);
        
        tracker.addProject(mediumPriority);
        tracker.addProject(highPriority);
        tracker.sortByPriority();
        
        List<Project> sorted = tracker.getProjects();
        assertEquals(highPriority, sorted.get(0)); // High priority should be first
    }

    @Test
    @Order(12)
    @DisplayName("Test Project Statistics")
    void testProjectStatistics() {
        Project active = new Project("Active", "12/31/2024", "Description");
        Project completed = new Project("Completed", "12/31/2024", "Description");
        
        tracker.addProject(active);
        tracker.addProject(completed);
        tracker.markProjectCompleted(completed);
        
        assertEquals(2, tracker.getTotalProjects());
        assertEquals(1, tracker.getActiveProjectCount());
        assertEquals(1, tracker.getCompletedProjectCount());
    }
}