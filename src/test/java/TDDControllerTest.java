import controller.FileExplorerController;
import model.FileExplorerModel;
import model.FileState;
import org.junit.Test;

import java.io.File;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TDDControllerTest {

    @Test
    public void testController() {
        File initialFile = new File("src/test/resources/test/");
        File[] children = initialFile.listFiles();
        assertThat(children).isNotNull().hasSizeGreaterThan(0);
        FileExplorerModel model = new FileExplorerModel(initialFile);
        FileExplorerController controller = new FileExplorerController(model);

        File fileToBeClicked = children[0];
        controller.fileClicked(fileToBeClicked);

        FileState state = controller.getState();

        assertThat(state.currentDirectory()).endsWith(fileToBeClicked.getName());

        controller.goUp();
        state = controller.getState();

        assertThat(state.currentDirectory()).endsWith("test");

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
