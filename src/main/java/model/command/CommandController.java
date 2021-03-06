package model.command;

public abstract class CommandController {

    public abstract String[] listFiles();
    public abstract String[] getLocation();

    public String[] createDirectory(String directory) {
        return getQualifiedCommand(String.format("mkdir %s", directory));
    }

    public String[] removeDirectory(String directory) {
        return getQualifiedCommand(String.format("rmdir %s", directory));
    }

    // We can't write a command that changes directory. Whoever controls the ProcessBuilder controls the directory.
    // CommandController just gets qualified commands depending on OS.
//    public String[] changeDirectory(String directory)  {
//        return getQualifiedCommand(String.format("cd %s", directory));
//    }

    protected String[] getQualifiedCommand(String command) {
        return new String[]{ getShell(), getCommandFlag(), command};
    }

    protected abstract String getShell();
    protected abstract String getCommandFlag();

}
