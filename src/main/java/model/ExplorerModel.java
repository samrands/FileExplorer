package model;

import java.io.File;

public class ExplorerModel {
    private File currentDir;

    public ExplorerModel() {
        currentDir = new File(System.getProperty("user.dir"));
    }

    public File getCurrentDir() {
        return currentDir;
    }

    public File[] getChildren() {
        return currentDir.listFiles();
    }

    public void changeDirectory(String newDirectoryPath) throws Exception {
        File newDirectory = new File(newDirectoryPath);

        if (!newDirectory.exists()) throw new Exception("Directory does not exist.");

        if (!newDirectory.isDirectory()) throw new Exception("Not a directory.");

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
}
