/**
 * Constants for the entire project
 *
 * created Nov 6, 2018
 */

import javax.swing.*;

class Constants
{
    final static String DEFAULT_DIR = new JFileChooser().getFileSystemView().getDefaultDirectory() +
            "/SoundboardMaker";

    final static String DEFAULT_RECORDING_NAME = "recordedClip.wav";
    final static String DEFAULT_RECORDING_PATH = DEFAULT_DIR + "/" + DEFAULT_RECORDING_NAME;
}
