import model.command.CommandController;
import model.ExplorerModel;
import model.command.WindowsCommandController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ModelTest {

    private static final String RESOURCES_PATH = "C:\\Users\\Sam\\IdeaProjects\\PersonalCodingProjects\\FileExplorer\\src\\main\\resources";
    private ExplorerModel explorer;
    private File emptyDir;

    @Before
    public void setUp() throws IOException, InterruptedException {
        explorer = new ExplorerModel();

        emptyDir = new File("C:\\Users\\Sam\\IdeaProjects\\PersonalCodingProjects\\FileExplorer\\src\\main\\resources\\empty");
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
    public void testCommandInterpreter() throws Exception {
        CommandController windowsInterpreter = new WindowsCommandController();

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command(windowsInterpreter.getLocation());
        Process newProc = processBuilder.start();
        printOutputToSystemOut(newProc);
        int exitCodeNewProc = newProc.waitFor();

//        processBuilder.command(windowsInterpreter.changeDirectory("%HOMEPATH%"));
//        Process currentProc = processBuilder.start();
//        printOutputToSystemOut(currentProc);
//        int exitCode = currentProc.waitFor();

//        if (exitCode != 0) fail();

        processBuilder.command(windowsInterpreter.createDirectory("test"));
        Process locProc = processBuilder.start();
        printOutputToSystemOut(locProc);
        locProc.waitFor();

        if (exitCodeNewProc != 0) fail();

        processBuilder.command(windowsInterpreter.listFiles());
        Process listFilesProc = processBuilder.start();
        printOutputToSystemOut(listFilesProc);
        int exitCodeListFiles = listFilesProc.waitFor();

        if (exitCodeListFiles != 0) fail();

        processBuilder.command(windowsInterpreter.removeDirectory("test"));
        Process rmdirProc = processBuilder.start();
        printOutputToSystemOut(rmdirProc);
        rmdirProc.waitFor();

        processBuilder.command(windowsInterpreter.listFiles());
        Process newListFilesProc = processBuilder.start();
        printOutputToSystemOut(newListFilesProc);
        newListFilesProc.waitFor();

        // LOL so I don't even need to write separate commands! I can just let the OS do everything for me.
    }

    @Test
    public void testGetHomeDir() {
        assertEquals(explorer.getCurrentDir().getAbsolutePath(), System.getProperty("user.dir"));
    }

    @Test
    public void testListFiles() {
        File[] pathsFromCurrentDir = explorer.getChildren();

        assertNotNull(pathsFromCurrentDir);
    }

    @Test
    public void changeDirectory() throws Exception {
        explorer.changeDirectory("C:\\Users\\Sam\\IdeaProjects\\PersonalCodingProjects\\FileExplorer\\src\\main\\resources\\empty");

        File[] pathsFromResources = explorer.getChildren();

        if (pathsFromResources != null && pathsFromResources.length > 0) fail();
    }

    @Test
    public void testCreateDirectory() throws Exception {
        explorer.changeDirectory(RESOURCES_PATH);

        explorer.createDirectory("test");

        File[] children = explorer.getChildren();

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
        explorer.changeDirectory(RESOURCES_PATH);
        explorer.createDirectory("test");
        explorer.delete(RESOURCES_PATH + "\\test");

        File[] children = explorer.getChildren();
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
        explorer.changeDirectory(RESOURCES_PATH);
        explorer.moveUpDirectory();
        assertEquals(explorer.getCurrentDir().getParentFile().toPath().relativize(explorer.getCurrentDir().toPath()).toString(), "main");
    }

    private void printOutputToSystemOut(Process process) {
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), process.getErrorStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
    }

}
