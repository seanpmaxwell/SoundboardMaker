/**
 * Starter file for the SoundBoard Maker project.
 *
 * created Nov 6, 2018
 */

import javax.swing.*;
import java.io.File;


class Main extends JFrame {

    private Main() {

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

        var pane = this._configureOverallLayout();
        this._configureOverallContainer(pane);
    }

    private JSplitPane _configureOverallLayout() {

        var sboardInner = new SoundboardInner();
        var sboardOuter = new SoundboardSide(sboardInner);
        var ctlrSide = new ControllerSide(sboardOuter, sboardInner);

        var pane = new JSplitPane();
        pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        pane.add(ctlrSide, JSplitPane.LEFT);
        pane.add(sboardOuter, JSplitPane.RIGHT);

        return pane;
    }

    private void _configureOverallContainer(JSplitPane pane) {
        super.getContentPane().add(pane);
        super.setBounds(400, 100, 1300, 600);
        super.setTitle("Soundboard Maker");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }


    // **************************************************************************************************** //
    //                                             Start Program
    // **************************************************************************************************** //

    public static void main(String[] args)
    {
        new Main();
    }
}
