package controller;

import model.ExplorerModel;

import java.io.File;
import java.util.Map;

public class ExplorerController {
    private Map<String, File> namesToFiles;

    private final ExplorerModel explorerModel;
    private final ExplorerView explorerView;

    public ExplorerController(ExplorerModel explorerModel, ExplorerView explorerView) {
        this.explorerModel = explorerModel;
        this.explorerView = explorerView;
    }

    public void createDirectory(String directoryName) throws Exception {
        explorerModel.createDirectory(directoryName);
    }

    public void changeDirectory(File file) throws Exception {
        explorerModel.changeDirectory(file.getAbsolutePath());

        refreshFiles();
    }

    public void moveUpDirectory() throws Exception {
        explorerModel.moveUpDirectory();

        refreshFiles();
    }

    private void refreshFiles() {
        File[] updatedFiles = explorerModel.getChildren();

        explorerView.updateFiles(updatedFiles);
    }



}
