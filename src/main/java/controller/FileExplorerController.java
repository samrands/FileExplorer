package controller;

import model.FileExplorerModel;
import model.FileState;

import java.io.File;

public class FileExplorerController {

    private final FileExplorerModel model;

    public FileExplorerController(FileExplorerModel model) {
        this.model = model;
    }

    public void fileClicked(File clickedFile) {
        System.out.printf("Clicked file: %s%n", clickedFile.getAbsolutePath());
        model.enterDirectory(clickedFile);
    }

    public FileState getState() {
        return model.getState();
    }

    public void goUp() {
        System.out.println("Going up directory");
        model.goUp();
    }

    public void checkShowHidden(boolean showHidden) {
        System.out.printf("Showing hidden: %b%n", showHidden);
        model.setShowHidden(showHidden);
    }

}
