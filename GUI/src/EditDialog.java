import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class EditDialog extends JDialog implements ActionListener{
    private Container co;
    private JLabel l1,l2;
    private JButton saveButton;
    private JTextField t1,t2;
    private GridBagConstraints gbc;
    private String name, desc;
    private IOTable tabla;
    private IOTableModel tm;
    private IO row;

    public EditDialog(JFrame f, String nombre, boolean modal){
        super(f, nombre, modal);
        this.setSize(400,250);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());

        this.tabla = Window.tabla;

        this.tm = (IOTableModel)tabla.getModel();
        this.row = this.tm.getElementById(tabla.getSelectedRow());

        name = row.getNombre();
        desc = row.getDesc();

        co = this.getContentPane();
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; //Llenar el espacio completo de la celda
        gbc.insets = new Insets(2,2,2,2);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        l1 = new JLabel("Nombre del Canal");
        co.add(l1,gbc);
    
        gbc.gridwidth = 2;
        gbc.gridy = 1;
        t1 = new JTextField(name);
        co.add(t1,gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        l2 = new JLabel("Descripción");
        co.add(l2,gbc);
        
        gbc.gridwidth = 2;
        gbc.gridy = 3;
        t2 = new JTextField(desc);
        co.add(t2,gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(20,2,2,2);
        saveButton = new JButton("Guardar Cambios");
        saveButton.addActionListener(this);
        co.add(saveButton,gbc);


    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==saveButton){
            String updatedName = t1.getText();
            String updatedDesc = t2.getText();
            ChannelsFile file = new ChannelsFile();
            this.row.setNombre(updatedName);
            this.row.setDesc(updatedDesc);
            file.updateRow(this.row);
            this.tm.llenarTabla();
            Window.tabla.clearSelection();
            App.ventana.getSide().setDefault();
            this.dispose();
        }
    }
}
