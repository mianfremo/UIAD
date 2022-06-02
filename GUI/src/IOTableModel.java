import java.io.File;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class IOTableModel extends DefaultTableModel{
    private ArrayList<IO> channels;

    public IOTableModel(){  
        String[] titulos = {"#","I/O","Nombre", "Descripción", "Tipo","Slot"};
        this.setColumnIdentifiers(titulos);
        llenarTabla();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }


    public void llenarTabla(){
        this.channels = new ChannelsFile().getChannels();
        this.setRowCount(0);
        FlatSVGIcon icon = new FlatSVGIcon(new File("icon/play.svg"));
        icon = icon.derive(12,12);
        for (IO canal : this.channels) {
            Object[] row = {
                Integer.toString(canal.getId()),
                canal.getIo().getNombre(),
                canal.getNombre(),
                canal.getDesc(),
                canal.getTipo().getNombre(),
                canal.getSlot()
            };

            this.addRow(row);

        }
    }

    public IO getElementById(int id){
        return this.channels.get(id);
    }
}
