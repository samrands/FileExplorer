package model.command;

public class WindowsCommandController extends CommandController {

    private static final String COMMAND_EXE = "cmd.exe";
    private static final String RUN_COMMAND_FLAG = "/c";

    public String[] listFiles() {
        return getQualifiedCommand("dir");
    }

    public String[] getLocation() {
        return getQualifiedCommand("cd");
    }

    protected String getShell() {
        return COMMAND_EXE;
    }

    protected String getCommandFlag() {
        return RUN_COMMAND_FLAG;
    }
}
