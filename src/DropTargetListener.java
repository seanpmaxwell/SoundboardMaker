/**
 * Drop Target Listener sound a button will be linked to a particular sound.
 *
 * created Nov 18, 2018
 */

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.datatransfer.DataFlavor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class DropTargetListener extends DropTargetAdapter {

    private SoundButton soundButton;
    private String fileFullPath;


    DropTargetListener(SoundButton soundButton) {
        this.soundButton = soundButton;
        new DropTarget(this.soundButton, DnDConstants.ACTION_COPY, this, true, null);
        this.fileFullPath = Constants.SOUNDS_DIR + "/" + soundButton.getSoundLabel() + ".wav";
    }


    @Override
    public void drop(DropTargetDropEvent event) {
        try {
            DataFlavor fileFlavor = TransferableFile.FILE_FLAVOR;
            // Check the type of file being dragged
            if (!event.isDataFlavorSupported(fileFlavor)) {
                event.rejectDrop();
                return;
            }
            event.acceptDrop(DnDConstants.ACTION_COPY);
            // Configure soundbutton
            if (this.soundButton.getTrack() == null) {
                this.soundButton.setTrack(this.fileFullPath);
            }
            // Copy file to new location
            Transferable transferable = event.getTransferable();
            var tempClip = (File)transferable.getTransferData(fileFlavor);
            var waveFile = new File(this.fileFullPath);
            StandardCopyOption opt = StandardCopyOption.REPLACE_EXISTING;
            Files.copy(tempClip.toPath(), waveFile.toPath(), opt);
            event.dropComplete(true);
        } catch (Exception e) {
            e.printStackTrace();
            event.rejectDrop();
        }
    }
}
