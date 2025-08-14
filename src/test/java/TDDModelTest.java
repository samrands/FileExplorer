import model.FileExplorerModel;
import view.FileState;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TDDModelTest {

    public static final String LAST_DIR_NAME = "test-dir";
    public static final String ENCLOSING_DIR_NAME = "src/test/resources/controller-test/";
    public static final String SRC_TEST_RESOURCES_CONTROLLER_TEST_TEST_DIR = 
            Paths.get(ENCLOSING_DIR_NAME, LAST_DIR_NAME).toString();

    @BeforeClass
    public static void setUp() throws IOException {
        if (System.getProperty("os.name").contains("Windows")) {
            Path path = Paths.get("src/test/resources/test/.test4");
            Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        }
    }

    @Test
    public void testEnterDirectory() {
        File initialFile = new File(SRC_TEST_RESOURCES_CONTROLLER_TEST_TEST_DIR);
        FileExplorerModel model = new FileExplorerModel(initialFile);

        FileState state = model.getState();

        assertThat(state.currentDirectory()).endsWith(LAST_DIR_NAME);
        assertThat(state.directoryContents().size()).isEqualTo(3);
        assertThat(state.directoryContents().stream().map(File::getName).toList()).asList().contains("test1", "test2", "test3");

        model.enterDirectory(state.directoryContents().stream()
                .filter(fileLambda -> fileLambda.getName().equalsIgnoreCase("test1"))
                .findAny()
                .orElseThrow());

        FileState stateAfterEntering = model.getState();
        assertThat(stateAfterEntering.currentDirectory()).endsWith("test1");
        assertThat(stateAfterEntering.directoryContents().size()).isEqualTo(0);

        model.goUp();
        FileState stateAfterGoingUp = model.getState();
        assertThat(stateAfterGoingUp.currentDirectory()).endsWith(LAST_DIR_NAME);
        assertThat(stateAfterGoingUp.directoryContents().stream().map(File::getName).toList()).asList().contains("test1", "test2", "test3");

        model.setShowHidden(true);

        FileState stateWithHidden = model.getState();
        assertThat(stateWithHidden.directoryContents().stream().map(File::getName).toList()).asList().contains("test1", "test2", "test3", ".test4");


    }


}
