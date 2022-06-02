import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class CustomCellRenderer implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column){
        IOTable tabla = (IOTable)table;
        IOTableModel dtm = (IOTableModel)tabla.getModel();
        
        // Creamos la etiqueta
        JLabel etiqueta = new JLabel();
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);

        if (isSelected){
            etiqueta.setBackground (new Color(60,131,197));
        }
        else{
            etiqueta.setBackground (Color.WHITE);

        }
 

        // Si el objeto que nos pasan es un String, lo ponemos en el JLabel.
        if (value instanceof String)
        {

            // Para que el JLabel haga caso al color de fondo, tiene que ser opaco
            etiqueta.setOpaque(true);
            if(dtm.getElementById(row).getEnabled()){
                etiqueta.setForeground(Color.BLACK);
            }else{
                etiqueta.setForeground(Color.GRAY);
            }
            etiqueta.setText((String)value);
        }

        // Devolvemos la etiqueta que acabamos de crear.
        return etiqueta;
    }
    
}