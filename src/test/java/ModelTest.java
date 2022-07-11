import model.command.CommandController;
import model.command.WindowsCommandController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.Assert.fail;

public class ModelTest {

    private static final String RESOURCES_PATH = "./../../resources";
    private static final Runtime currentEnv = Runtime.getRuntime();

//    @Before
//    public void setUp() throws IOException, InterruptedException {
//        currentEnv.exec(String.format("cd %s", RESOURCES_PATH)).waitFor();
//
//        currentEnv.exec("mkdir test && cd test").waitFor();
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < 10; i++) {
//            currentEnv.exec(String.format("echo \"%s\" > %d.txt", builder.toString(), i));
//
//            builder.append("test");
//        }
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        currentEnv.exec("cd .. && rm -rf test");
//    }

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

        processBuilder.command(windowsInterpreter.changeDirectory("%HOMEPATH%"));
        Process currentProc = processBuilder.start();
        printOutputToSystemOut(currentProc);
        int exitCode = currentProc.waitFor();

        if (exitCode != 0) fail();

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
    }

    private void printOutputToSystemOut(Process process) {
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), process.getErrorStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
    }

}
