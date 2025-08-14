import controller.FileExplorerController;
import model.ExplorerModel;
import model.FileExplorerModel;
import view.FileState;

import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TDDControllerTest {

    @Test
    public void testController() throws Exception {
        File initialFile = new File(TDDModelTest.ENCLOSING_DIR_NAME);
        File[] children = initialFile.listFiles();
        assertThat(children).isNotNull().hasSizeGreaterThan(0);
        ExplorerModel model = new ExplorerModel(initialFile.getAbsolutePath(), false);
        FileExplorerController controller = new FileExplorerController(model);

        File fileToBeClicked = Paths.get(TDDModelTest.SRC_TEST_RESOURCES_CONTROLLER_TEST_TEST_DIR)
                .toFile();
        controller.clickOnFile(fileToBeClicked);

        FileState state = controller.getState();

        assertThat(state.currentDirectory()).endsWith(fileToBeClicked.getName());

        controller.checkShowHidden(true);

        state = controller.getState();
        assertThat(state.showHidden()).isTrue();
        assertThat(state.directoryContents().stream().map(File::getName).toList())
                .asList().contains(".test4");

        controller.checkShowHidden(false);
        state = controller.getState();
        assertThat(state.showHidden()).isFalse();
        assertThat(state.directoryContents().stream().map(File::getName).toList())
                .asList().doesNotContain(".test4");
    }

}
