import controller.FileExplorerController;
import model.FileExplorerModel;
import view.FileExplorerView;
import view.SwingFileExplorerView;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File initialFile = new File("src/test/resources/test/");
        FileExplorerModel model = new FileExplorerModel(initialFile);
        FileExplorerController controller = new FileExplorerController(model);
        FileExplorerView view = new SwingFileExplorerView(controller);

        controller.addView(view);
        controller.refreshState();
        controller.showUI();
    }

}
