import model.command.CommandController;
import model.ExplorerModel;
import model.command.WindowsCommandController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ModelTest {

    private static final String TEST_DIR = "model-test";
    private String testAbsPath;
    private ExplorerModel explorer;
    private String emptyDirPath;
    private File emptyDir;

    @Before
    public void setUp() throws IOException, InterruptedException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL testResource = classLoader.getResource(TEST_DIR);
        testAbsPath = testResource.getPath();
        explorer = new ExplorerModel(testAbsPath, false);

        emptyDirPath = Paths.get(testResource.getPath(), "empty").toString();
        emptyDir = new File(emptyDirPath);
        if (emptyDir.exists()) {
            emptyDir.delete();
        }
        emptyDir.mkdir();
    }

    @After
    public void tearDown() throws Exception {
        emptyDir.delete();
    }

    @Test
    public void testGetAllFiles() {
    }

    @Test
    public void testListFiles() {
        List<File> pathsFromCurrentDir = explorer.getChildren();

        assertNotNull(pathsFromCurrentDir);
    }

    @Test
    public void changeDirectory() throws Exception {
        explorer.changeDirectory(emptyDirPath);

        List<File> pathsFromResources = explorer.getChildren();

        if (pathsFromResources != null && pathsFromResources.size() > 0) fail();
    }

    @Test
    public void testCreateDirectory() throws Exception {
        explorer.changeDirectory(testAbsPath);

        explorer.createDirectory("test");

        List<File> children = explorer.getChildren();

        boolean isPass = false;
        for (File f : children) {
            Path relativePath = explorer.getCurrentDir().toPath().relativize(f.toPath());
            if (relativePath.toString().equalsIgnoreCase("test")) {
                isPass = true;
                f.delete();
            }
        }

        if (!isPass) fail();
    }

    @Test
    public void testDelete() throws Exception {
        explorer.changeDirectory(testAbsPath);
        explorer.createDirectory("test");
        explorer.delete(Paths.get(testAbsPath, "test").toString());

        List<File> children = explorer.getChildren();
        for (File f : children) {
            Path relativePath = explorer.getCurrentDir().toPath().relativize(f.toPath());
            if (relativePath.toString().equalsIgnoreCase("test")) {
                f.delete();
                fail();
            }
        }
    }

    @Test
    public void testMoveUp() throws Exception{
        explorer.changeDirectory(testAbsPath);
        explorer.moveUpDirectory();
        assertEquals(explorer.getCurrentDir().getParentFile().toPath().relativize(explorer.getCurrentDir().toPath()).toString(), "test");
    }

    @Test
    public void testChangeDirectory() throws Exception {
        explorer.changeDirectory(testAbsPath);
    }

    private void printOutputToSystemOut(Process process) {
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), process.getErrorStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
    }

}
