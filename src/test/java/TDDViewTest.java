import controller.FileExplorerController;
import model.FileExplorerModel;
import org.junit.Test;
import view.FileExplorerView;
import view.SwingFileExplorerView;

import java.io.File;

public class TDDViewTest {

    @Test
    public void testView() {
        // What do we want to test for the view?
        // We want to test that when the user clicks up, they actually go up.
        // We want to test that if they click on a directory, we actually go into that directory (the files are what we expect).
        // More importantly, we want to make sure that the UI renders as we expect. No overlaps, etc. I've never coded
        // something like this before. So I think the only way to proceed is to try and create something and then once
        // I have something to start with write some tests.
        File initialFile = new File("src/test/resources/test/");
        FileExplorerModel model = new FileExplorerModel(initialFile);
        FileExplorerController controller = new FileExplorerController(model);

        FileExplorerView view = new SwingFileExplorerView(controller);

        int a = 1;
    }

}
