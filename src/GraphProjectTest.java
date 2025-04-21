import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GraphProjectTest {
    private GraphProject project;
    
    @Before
    public void setUp() {
        project = new GraphProject(10);
    }
    
    @Test
    public void testProcessInsert() {
        project.processCommand("insert Artist1<SEP>Song1");
        // Verify artist and song were added to respective tables
        // Would need package-private accessors to verify
    }
    
    @Test
    public void testProcessInsertMalformed() {
        project.processCommand("insert Artist1Song1"); // Missing separator
        // Verify appropriate error message
    }
    
    @Test
    public void testProcessRemoveArtist() {
        project.processCommand("insert Artist1<SEP>Song1");
        project.processCommand("remove artist Artist1");
        // Verify artist was removed
    }
    
    @Test
    public void testProcessRemoveNonExistent() {
        project.processCommand("remove artist Nonexistent");
        // Verify appropriate "not found" message
    }
    
    @Test
    public void testProcessPrintArtist() {
        project.processCommand("insert Artist1<SEP>Song1");
        project.processCommand("print artist");
        // Verify output
    }
    
    @Test
    public void testProcessPrintGraph() {
        project.processCommand("insert Artist1<SEP>Song1");
        project.processCommand("print graph");
        // Verify output
    }
    
    @Test
    public void testProcessInvalidCommand() {
        project.processCommand("invalid command");
        // Verify appropriate error message
    }
}
