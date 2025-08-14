package controller;

import model.ExplorerModel;
import view.FileState;

import java.io.File;

public class FileExplorerController {

    private final ExplorerModel model;

    public FileExplorerController(
        ExplorerModel model) {
        this.model = model;
    }

    public void clickOnFile(File clickedFile) throws Exception {
        System.out.printf("Clicked file: %s%n", clickedFile.getAbsolutePath());
        model.changeDirectory(clickedFile.getAbsolutePath());
    }

    public FileState getState() {
        return new FileState(
            model.getCurrentDir().getAbsolutePath(), 
            model.getChildren(), 
            model.isShowHidden());
    }

    public void goUp() throws Exception {
        System.out.println("Going up directory");
        model.moveUpDirectory();
    }

    public void checkShowHidden(boolean isSelected) {
        System.out.println("Toggling hidden");
        model.setShowHidden(isSelected);
    }

    public void changeCurrentDirectory(String currentDirectory) throws Exception {
        System.out.printf("Changing directory to %s", currentDirectory);

        model.changeDirectory(currentDirectory);
    }

}
