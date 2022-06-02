import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class IOTable extends JTable{

    public IOTable(IOTableModel dtm){
        super(dtm);
        // this.setDefaultRenderer(String.class, new CustomCellRenderer());
        this.getTableHeader().setReorderingAllowed(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setDefaultRenderer(Object.class, new CustomCellRenderer());

    }

}


