import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.nio.file.*;

public class DropTargetListener extends DropTargetAdapter
{
    private DropTarget _dropTarget;
    private SoundButton _soundButton;
    private Transferable tr;
    private File temp_clip;
    private File wave_file;
    private String directory;

    DropTargetListener(SoundButton soundButton)
    {
        this._soundButton = soundButton;
        drop_target = new DropTarget(sound_button, DnDConstants.ACTION_COPY, this, true, null);
        directory = new JFileChooser().getFileSystemView().getDefaultDirectory() + "\\SoundboardMaker\\"
                                + sound_button.Get_Sound_Label() + ".wav";
    }

    @Override
    public void drop( DropTargetDropEvent event )
    {
        try
        {
            tr = event.getTransferable();
            temp_clip = (File)tr.getTransferData( TransferableFile.fileFlavor );

            if( event.isDataFlavorSupported(TransferableFile.fileFlavor) )
            {
                event.acceptDrop( DnDConstants.ACTION_COPY );

                if ( sound_button.Get_Track() == null )
                    sound_button.Set_Track( directory );

                wave_file = new File( directory );
                Files.copy( temp_clip.toPath(), wave_file.toPath(), StandardCopyOption.REPLACE_EXISTING );
                event.dropComplete( true );
            }
            event.rejectDrop();
        }
        catch( Exception e )
        {
            e.printStackTrace();
            event.rejectDrop();
        }
    }
}