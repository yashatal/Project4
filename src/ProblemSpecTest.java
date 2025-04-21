import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import student.TestCase;

/**
 * @author Yash Atal
 * @version 1.0
 */
public class ProblemSpecTest extends TestCase {
    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        // Nothing here
    }


    /**
     * Read contents of a file into a string
     * 
     * @param path
     *            File name
     * @return the string
     * @throws IOException
     */
    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }


    /**
     * Example 1: This method runs on a command sample IO file
     * You will write similar test cases
     * using different text files
     * 
     * @throws IOException
     */
    public void testPostedSample1() throws IOException {
        // Setting up all the parameters
        String[] args = new String[2];
        args[0] = "10";
        args[1] = "solutionTestData/P4_sampleInput.txt";

        // Invoke main method of our Points Project
        GraphProject.main(args);

        // Actual output from your System console
        String output = systemOut().getHistory();

        // Expected output from file
        String referenceOutput = readFile(
            "solutionTestData/P4_sampleOutput.txt");

        // Compare the two outputs
        // TODO: uncomment the following line
        // once you have implemented your project
 //assertFuzzyEquals(referenceOutput, output);
    }
}
