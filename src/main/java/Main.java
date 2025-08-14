import controller.FileExplorerController;
import model.ExplorerModel;
import view.SwingFileExplorerView;

import java.io.File;

public class Main {
    private static final String APP_NAME = "File Explorer";

    public static void main(String[] args) {
        File initialFile = new File(System.getProperty("user.dir"));

        ExplorerModel model = new ExplorerModel(initialFile.getAbsolutePath(), false);
        FileExplorerController controller = new FileExplorerController(model);
        SwingFileExplorerView view = new SwingFileExplorerView(
            APP_NAME,
            controller::getState, 
            controller::checkShowHidden,
            controller::changeCurrentDirectory,
            controller::goUp,
            controller::clickOnFile
        );

        view.showUI();
    }

}
