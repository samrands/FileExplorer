package view;

import controller.FileExplorerController;
import model.FileState;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SwingFileExplorerView implements FileExplorerView {
    private static final String TITLE = "Swing File Explorer";
    private static final String GO_UP = "Go up";
    private static final String HIDDEN_TEXT = "Show Hidden Files";
    private static final String CANNOT_ENTER_FILE_WARNING = "You can only enter directories. %s is a file.";
    private static final int TOP_BAR_X_START = 20;
    private static final int TOP_BAR_Y_START = 20;
    private static final int UP_BUTTON_WIDTH = 50;
    private static final int TOP_BAR_HEIGHT = 50;
    private static final int FILE_BUTTON_WIDTH = 50;
    private static final int FILE_BUTTON_HEIGHT = 50;
    private static final int CURR_DIR_LABEL_X_START = TOP_BAR_X_START + UP_BUTTON_WIDTH + 5;
    private static final int CURR_DIR_LABEL_WIDTH = 200;
    private final FileExplorerController controller;
    private final JFrame frame;
    private final JLabel currentDirectoryLabel;
    private final JButton goUpButton;
    private final JCheckBox showHiddenCheckbox;
    private final Container filesContainer;

    public SwingFileExplorerView(FileExplorerController controller) {
        this.controller = controller;

        frame = createFrame();

        currentDirectoryLabel = createCurrentDirectoryLabel();
        goUpButton = createUpButton();
        showHiddenCheckbox = createShowHiddenCheckbox();
        filesContainer = new Container();

        frame.add(currentDirectoryLabel);
        frame.add(goUpButton);
        frame.add(showHiddenCheckbox);
        frame.add(filesContainer);

        // Let's think about how we want this to look.
        // At the top we want a back (or up) arrow that takes us up one directory, then a label that shows the current
        // directory. This should probably be in a box. Then finally a checkbox to show hidden files.
        // Then, I want to actually list the files. There might be different file layouts we could pick from, so it would
        // make sense to try and decouple the creating the components from this class.
        // For now, let's do the classic approach. A square button for each file. The hard part is going to be wrapping them
        // and then adding a scroll bar.

        // How should creation of this object work? We create the class with the controller. We should initialize everything.
        // This means that we create each frame or component or whatever that we actually need and add it to the frame.
        // We save these in the view object, and update them as necessary. We initialize current directory to null (or
        // empty), show hidden to false, and the file list to just be an empty component.

        // Then, we start the program by getting the state from the model and sending it in here. Then users interact
        // with the UI, that goes to the ActionListener here which forwards that to the controller, model, back to controller
        // then that refreshes the state.

        // So, what needs to be done right now? We need to create each of the components as variables and initialize them
        // Then we need to complete the refreshState method to update those components
        // Then we need to complete the actionlistener logic in controller to refresh state after every change
        // Actions:
        //   Go up (takes none), enter directory (takes one argument), check show hidden
        // Somehow we have to pass the function to controller, in enter directory's case, we have to pass the argument.
        // So we could just pass null. Well, maybe I can just pass a runnable and include the argument in the lambda.
    }

    public JButton createUpButton() {
        return new JButton(GO_UP) {{
            setBounds(TOP_BAR_X_START, TOP_BAR_Y_START, UP_BUTTON_WIDTH, TOP_BAR_HEIGHT);
            addActionListener(event -> {
                controller.act(controller::goUp);
                // How do we refresh the state after going up? Maybe we can have a generic method in the controller that
                // takes in an event and then refreshes state afterward. Or we just have a call to refresh state after
                // every method in controller, but that seems bad. So we need a method in controller that takes an ActionEvent
                // and determines which action to take.
                // Another idea, we use some functional programming. We pass in the update method in controller. Then controller
                //
            });
        }};
    }

    public JLabel createCurrentDirectoryLabel() {
        return new JLabel() {{
            setBounds(CURR_DIR_LABEL_X_START, TOP_BAR_Y_START, CURR_DIR_LABEL_WIDTH, TOP_BAR_HEIGHT);
        }};
    }

    public JCheckBox createShowHiddenCheckbox() {
        return new JCheckBox(HIDDEN_TEXT) {{
            addActionListener(checked -> {
                controller.act(controller::checkShowHidden);
            });
        }};
    }

    public JFrame createFrame() {
        return new JFrame(TITLE) {{
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }};
    }

    @Override
    public void showUI() {
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void refreshState(FileState state) {
        currentDirectoryLabel.setText(state.currentDirectory());

        state.directoryContents().forEach(file -> filesContainer.add(createFileButton(file)));
    }

    public JButton createFileButton(File file) {
        // Future: add different icons based on whether directory or file
        return new JButton(file.getName()) {{
            setSize(FILE_BUTTON_WIDTH, FILE_BUTTON_HEIGHT);
            addActionListener(event -> {
                if (file.isDirectory()) controller.act(() -> controller.fileClicked(file));
                else JOptionPane.showMessageDialog(frame, CANNOT_ENTER_FILE_WARNING);
            });
        }};
    }

}
