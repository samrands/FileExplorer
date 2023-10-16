package view;

import model.FileState;

public interface FileExplorerView {

    void refreshState(FileState state);

    void showUI();

}
