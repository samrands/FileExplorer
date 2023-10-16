package controller;

import model.FileExplorerModel;
import model.FileState;
import view.FileExplorerView;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileExplorerController {

    private final FileExplorerModel model;
    private final List<FileExplorerView> views = new LinkedList<>();

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

    public void checkShowHidden() {
        System.out.println("Toggling hidden");
        model.toggleShowHidden();
    }

    public void addView(FileExplorerView view) {
        views.add(view);
    }

    public void act(Runnable action) {
        action.run();

        refreshState();
    }

    public void refreshState() {
        views.forEach(view -> view.refreshState(model.getState()));
    }

    public void showUI() {
        views.forEach(FileExplorerView::showUI);
    }

}
