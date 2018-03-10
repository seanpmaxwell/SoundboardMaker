import java.awt.datatransfer.*;
import java.io.File;

class TransferableFile implements Transferable {

    protected static DataFlavor fileFlavor = new DataFlavor(File.class, "A File Object");
    protected static DataFlavor[] supportedFlavors = {fileFlavor};

    private File file;

    public TransferableFile(File file)
    {
        this.file = file;
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        return supportedFlavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return flavor.equals(fileFlavor) || flavor.equals(DataFlavor.stringFlavor) ? true : false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
    {
        if( flavor.equals(fileFlavor) )
            return file;
        else
            throw new UnsupportedFlavorException(flavor);
    }
}
