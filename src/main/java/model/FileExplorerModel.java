package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileExplorerModel {

    private File currentLocation;
    private boolean showHidden = false;

    public FileExplorerModel(File startLocation) {
        if (startLocation == null) {
            throw new IllegalArgumentException("File provided was null");
        }

        if (startLocation.exists()) {
            currentLocation = startLocation;
            return;
        }

        throw new IllegalArgumentException(String.format("File %s does not exist", startLocation.getAbsolutePath()));
    }

    public void enterDirectory(File arg) {
        File[] filesWithSamePath = currentLocation.listFiles(file ->
                file.getAbsolutePath().equalsIgnoreCase(arg.getAbsolutePath())
        );

        if (filesWithSamePath == null || filesWithSamePath.length != 1) {
            throw new IllegalArgumentException(String.format("Unable to enter file %s from %s",
                    arg.getAbsolutePath(), currentLocation.getAbsolutePath()));
        }

        currentLocation = filesWithSamePath[0];
    }

    public FileState getState() {
        File[] filesAtLocation = currentLocation.listFiles();

        List<File> files = new ArrayList<>();

        if (filesAtLocation != null) {
            files = Arrays.stream(filesAtLocation)
                        .filter(file -> !file.isHidden() || showHidden)
                        .toList();
        }

        return new FileState(currentLocation.getAbsolutePath(), files, showHidden);
    }

    public void goUp() {
        File parent = currentLocation.getParentFile();

        if (parent != null) currentLocation = parent;
    }

    public void toggleShowHidden() {
        this.showHidden = !showHidden;
    }

}
