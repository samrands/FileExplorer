package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExplorerModel {
    private File currentDir;
    private boolean showHidden;

    public ExplorerModel(String currentDirectory, boolean showHidden) {
        currentDir = new File(currentDirectory);
        this.showHidden = showHidden;
    }

    public File getCurrentDir() {
        return currentDir;
    }

    public List<File> getChildren() {
        File[] filesAtLocation = currentDir.listFiles();

        List<File> files = new ArrayList<>();

        if (filesAtLocation != null) {
            files = Arrays
                        .stream(filesAtLocation)
                        .filter(file -> !file.isHidden() || showHidden)
                        .toList();
        }

        return files;
    }

    public void changeDirectory(String newDirectoryPath) throws Exception {
        File newDirectory = new File(newDirectoryPath);

        if (!newDirectory.exists()) throw new Exception("Directory %s does not exist.".formatted(newDirectoryPath));

        if (!newDirectory.isDirectory()) throw new Exception("%s is not a directory.".formatted(newDirectoryPath));

        currentDir = newDirectory;
    }

    public void moveUpDirectory() throws Exception {
        changeDirectory(currentDir.getParent());
    }

    public void createDirectory(String directoryPath) throws Exception{
        File directoryToBeMade = currentDir.toPath().resolve(directoryPath).toFile();

        boolean isCreated = directoryToBeMade.mkdirs();

        if (!isCreated) throw new Exception("Unable to create directory.");
    }

    public void delete(String fileOrDirectory) throws Exception {
        File toBeDeleted = new File(fileOrDirectory);

        if (!toBeDeleted.exists()) throw new Exception("File or directory does not exist.");

        boolean isDeleted = toBeDeleted.delete();

        if (!isDeleted) throw new Exception("Unable to delete directory.");
    }

    public void setShowHidden(boolean showHidden) {
        this.showHidden = showHidden;
    }

    public boolean isShowHidden() {
        return this.showHidden;
    }
}
