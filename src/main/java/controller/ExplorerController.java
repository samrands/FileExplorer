package controller;

import model.ExplorerModel;

public class ExplorerController {

    private final ExplorerModel explorerModel;
    private final ExplorerView explorerView;

    public ExplorerController(ExplorerModel explorerModel, ExplorerView explorerView) {
        this.explorerModel = explorerModel;
        this.explorerView = explorerView;
    }



}
