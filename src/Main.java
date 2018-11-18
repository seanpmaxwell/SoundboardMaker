/**
 * Starter file for the SoundBoard Maker project.
 *
 * created Nov 6, 2018
 */

import javax.swing.*;
import java.io.File;


class Main extends JFrame
{
    private Main()
    {
        this._configureDirectory();
        var pane = this._configureOverallLayout();
        this._configureOverallContainer(pane);
    }

    private void _configureDirectory()
    {
        var defaultDir = new JFileChooser().getFileSystemView().getDefaultDirectory();
        var dir = new File(defaultDir + "\\SoundboardMaker");

        if(!dir.exists()) {
            dir.mkdir();
        }
    }

    private JSplitPane _configureOverallLayout()
    {
        var sboardInner = new SoundboardInner();
        var sboardOuter = new SoundboardSide(sboardInner);
        var ctlrSide = new ControllerSide(sboardOuter, sboardInner);

        var pane = new JSplitPane();
        pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        pane.add(ctlrSide, JSplitPane.LEFT);
        pane.add(sboardOuter, JSplitPane.RIGHT);

        return pane;
    }

    private void _configureOverallContainer(JSplitPane pane)
    {
        super.getContentPane().add(pane);
        super.setBounds(400, 100, 1300, 600);
        super.setTitle("Soundboard Maker");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }

    public static void main(String[] args)
	{
	    new Main();
	}
}

