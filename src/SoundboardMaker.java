/**
 * Overall container for the SoundboardMaker
 *
 * created Nov 6, 2018
 */

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.io.File;


class SoundboardMaker extends JFrame {

    private final String GUI_TITLE = "Soundboard Maker";
    private static final long serialVersionUID = 1L;

    void start() {
        // Intialize directories
        var defaultDir = new File(Constants.DEFAULT_DIR);
        var projectsDir = new File(Constants.PROJECTS_DIR);
        var soundPath = new File(Constants.SOUNDS_DIR);
        if (!defaultDir.exists()) {
            defaultDir.mkdir();
        }
        if (!projectsDir.exists()) {
            projectsDir.mkdir();
        }
        if (!soundPath.exists()) {
            soundPath.mkdir();
        }
        // Arrange GUI sub-components
        var rightInnerContainer = new RightInnerContainer();
        var rightOuterContainer = new RightOuterContainer(rightInnerContainer);
        var leftContainer = new LeftContainer(rightOuterContainer, rightInnerContainer);
        var pane = new JSplitPane();
        pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        pane.add(leftContainer, JSplitPane.LEFT);
        pane.add(rightOuterContainer, JSplitPane.RIGHT);
        // Initialize the SoundboardMaker GUI setttings
        super.getContentPane().add(pane);
        super.setBounds(400, 100, 1300, 600);
        super.setTitle(this.GUI_TITLE);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }
}
