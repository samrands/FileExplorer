package view;

import view.function.ChangeCurrentDirectory;
import view.function.FileClicker;
import view.function.StateChanger;
import view.function.UpMover;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class SwingFileExplorerView {
    private static final String TITLE = "Swing File Explorer";
    private static final String GO_UP = "Go up";
    private static final String HIDDEN_TEXT = "Show Hidden Files";
    private static final int TOP_BAR_X_START = 20;
    private static final int TOP_BAR_Y_START = 20;
    private static final int UP_BUTTON_WIDTH = 50;
    private static final int TOP_BAR_HEIGHT = 50;
    private static final int FILE_BUTTON_WIDTH = 50;
    private static final int FILE_BUTTON_HEIGHT = 50;
    private static final int CURR_DIR_LABEL_X_START = TOP_BAR_X_START + UP_BUTTON_WIDTH + 5;
    private static final int CURR_DIR_LABEL_WIDTH = 200;
    private static final int FRAME_HEIGHT = 400;
    private static final int FRAME_WIDTH = 600;
    private final JFrame frame;
    private final JTextField currentDirectoryLabel;
    private final JButton goUpButton;
    private final JCheckBox showHiddenCheckbox;
    private final JScrollPane filesScrollPane;
    private final JPanel filesContainer;
    private final JPanel topPane;
    private final Supplier<FileState> refreshStateCallback;
    private final Consumer<Boolean> changeHidden; 
    private final ChangeCurrentDirectory changeCurrentDirectory;
    private final FileClicker fileClicker;
    private final UpMover moveUp;
    private final String appName;

    // Instead of tying the View implementation to the Controller, let's have it take the callback functions in the constructor.
    // If it's large enough we can put it in a container, but I don't think that's really all that useful.
    public SwingFileExplorerView(
        String appName,
        Supplier<FileState> refreshStateCallback,
        Consumer<Boolean> changeHidden, 
        ChangeCurrentDirectory changeCurrentDirectory,
        UpMover moveUp,
        FileClicker fileClicker
    ) {
        this.appName = appName;
        this.refreshStateCallback = refreshStateCallback;
        this.changeHidden = changeHidden;
        this.changeCurrentDirectory = changeCurrentDirectory;
        this.moveUp = moveUp;
        this.fileClicker = fileClicker;

        frame = createFrame();

        currentDirectoryLabel = createCurrentDirectoryLabel();
        goUpButton = createUpButton();
        showHiddenCheckbox = createShowHiddenCheckbox();
        filesContainer = new JPanel();
        filesScrollPane = new JScrollPane(filesContainer);
        filesScrollPane.setPreferredSize(new Dimension(400, 400));
        topPane = new JPanel();
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.LINE_AXIS));
        topPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        topPane.add(Box.createHorizontalGlue());
        topPane.add(goUpButton);
        topPane.add(Box.createRigidArea(new Dimension(10, 0)));
        topPane.add(currentDirectoryLabel);
        topPane.add(Box.createRigidArea(new Dimension(10, 0)));
        topPane.add(showHiddenCheckbox);

        refreshState();

        addComponentsToFrame();
    }

    private void addComponentsToFrame() {
        Container framePane = frame.getContentPane();
        framePane.add(topPane, BorderLayout.PAGE_START);
        framePane.add(filesScrollPane, BorderLayout.CENTER);
    }

    private JButton createUpButton() {
        return new JButton(GO_UP) {{
            setBounds(TOP_BAR_X_START, TOP_BAR_Y_START, UP_BUTTON_WIDTH, TOP_BAR_HEIGHT);
            addActionListener(event -> {
                changeState(moveUp::move);
            });
        }};
    }

    private JTextField createCurrentDirectoryLabel() {
        return new JTextField() {{
            setBounds(CURR_DIR_LABEL_X_START, TOP_BAR_Y_START, CURR_DIR_LABEL_WIDTH, TOP_BAR_HEIGHT);
            addActionListener(event -> {
                changeState(() -> changeCurrentDirectory.change(getText()));
            });
        }};
    }

    private JCheckBox createShowHiddenCheckbox() {
        return new JCheckBox(HIDDEN_TEXT) {{
            addActionListener(event -> {
                changeState(() -> changeHidden.accept(isSelected()));
            });
        }};
    }

    private JFrame createFrame() {
        return new JFrame(TITLE) {{
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        }};
    }

    public void showUI() {
        frame.pack();
        frame.setVisible(true);
    }

    private JButton createFileButton(File file) {
        // Future: add different icons based on whether directory or file
        System.out.println("Creating button with width %s and height %s".formatted(FILE_BUTTON_WIDTH, FILE_BUTTON_HEIGHT));
        return new JButton(file.getName()) {{
            setSize(FILE_BUTTON_WIDTH, FILE_BUTTON_HEIGHT);
            addActionListener(event -> {
                changeState(() -> fileClicker.click(file));
            });
        }};
    }

    private void changeState(StateChanger callback) {
        try {
            callback.change();

            refreshState();
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, appName, JOptionPane.ERROR_MESSAGE);
    }

    private void refreshState() {
        FileState currentState = refreshStateCallback.get();

        applyState(currentState);
    }

    public void applyState(FileState state) {
        currentDirectoryLabel.setText(state.currentDirectory());

        filesContainer.removeAll();
        state.directoryContents().forEach(file -> filesContainer.add(createFileButton(file)));

        showHiddenCheckbox.setSelected(state.showHidden());

        frame.revalidate();
        frame.repaint();
    }

}
