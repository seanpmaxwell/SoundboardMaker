/**
 * Drop Target Listener sound a button will be linked to a particular sound.
 *
 * created Nov 18, 2018
 */

import java.awt.dnd.*;
import java.io.File;
import java.nio.file.*;


public class DropTargetListener extends DropTargetAdapter
{
    private SoundButton _soundButton;
    private String _fileNameAndPath;

    DropTargetListener(SoundButton soundButton)
    {
        this._soundButton = soundButton;
        new DropTarget(this._soundButton, DnDConstants.ACTION_COPY, this, true, null);
        this._fileNameAndPath = Constants.DEFAULT_DIR + "/" + soundButton.getSoundLabel() + ".wav";
    }

    @Override
    public void drop(DropTargetDropEvent event)
    {
        try {
            var transferable = event.getTransferable();

            if(event.isDataFlavorSupported(TransferableFile.FILE_FLAVOR)) {
                event.acceptDrop(DnDConstants.ACTION_COPY);

                if(this._soundButton.getTrack() == null) {
                    this._soundButton.setTrack(this._fileNameAndPath);
                }

                var fileFlavor = TransferableFile.FILE_FLAVOR;
                var tempClip = (File)transferable.getTransferData(fileFlavor);
                var waveFile = new File(this._fileNameAndPath);

                var opt = StandardCopyOption.REPLACE_EXISTING;
                Files.copy(tempClip.toPath(), waveFile.toPath(), opt);
                event.dropComplete(true);
            }

            event.rejectDrop();
        }
        catch(Exception e) {
            e.printStackTrace();
            event.rejectDrop();
        }
    }
}
