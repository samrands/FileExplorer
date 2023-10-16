import model.FileExplorerModel;
import model.FileState;
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

    @BeforeClass
    public static void setUp() throws IOException {
        if (System.getProperty("os.name").contains("Windows")) {
            Path path = Paths.get("src/test/resources/test/.test4");
            Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        }
    }

    @Test
    public void testEnterDirectory() {
        File initialFile = new File("src/test/resources/test/");
        FileExplorerModel model = new FileExplorerModel(initialFile);

        FileState state = model.getState();

        assertThat(state.currentDirectory()).endsWith("test");
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
        assertThat(stateAfterGoingUp.currentDirectory()).endsWith("test");
        assertThat(stateAfterGoingUp.directoryContents().stream().map(File::getName).toList()).asList().contains("test1", "test2", "test3");

        model.toggleShowHidden();

        FileState stateWithHidden = model.getState();
        assertThat(stateWithHidden.directoryContents().stream().map(File::getName).toList()).asList().contains("test1", "test2", "test3", ".test4");


    }


}
