/**
 * Enables files to be drag and dropped for the DropTargetListener class.
 *
 * created Nov 18, 2018
 */

import java.awt.datatransfer.*;
import java.io.File;


class TransferableFile implements Transferable
{
    static final DataFlavor FILE_FLAVOR = new DataFlavor(File.class, "A File Object");
    private static DataFlavor[] _supportedFlavors = {FILE_FLAVOR};

    private File _file;

    TransferableFile(File file)
    {
        this._file = file;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return this._supportedFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return flavor.equals(FILE_FLAVOR) || flavor.equals(DataFlavor.stringFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
    {
        if(flavor.equals(FILE_FLAVOR)) {
            return this._file;
        }
        else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
