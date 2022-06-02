import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Container;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignalDialog extends JDialog implements ItemListener,ActionListener{
    private Container co;
    private GridBagConstraints gbc;
    private JComboBox<String> signalSelector, magnitudeSelector, varSelector, unitySelector;
    private JLabel l1,l2,l3,l4,l5,l6,l7;
    private JTextField minTextField, maxTextField;
    private String[] signalArray, magnitudesArray, varArray, unityArray;
    private JButton saveButton;
    private IO actualValue;
    private IOTableModel dtm;
    private double actualMinRange, actualMaxRange;
    private Unidad actualVar;
    private Tipo actualSignalValue;


    public SignalDialog(JFrame f, String nombre, boolean modal){
        super(f,nombre,modal);
        this.setSize(400,400);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.dtm = (IOTableModel)Window.tabla.getModel();
        this.actualValue = dtm.getElementById(Window.tabla.getSelectedRow());
        this.actualMinRange = actualValue.getMinRange();
        this.actualMaxRange = actualValue.getMaxRange();
        this.actualVar = actualValue.getUnity();
        this.actualSignalValue = actualValue.getTipo();
        this.unityArray = Unidad.detUnidad(actualVar.getMagnitud());

        co = this.getContentPane();
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,2,2,2);
        gbc.fill = GridBagConstraints.BOTH; //Llenar el espacio completo de la celda
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;

        l1 = new JLabel("Magnitud de la medición:");
        co.add(l1,gbc);

        gbc.gridy = 1;
        magnitudesArray = new String[]{"Voltaje","Corriente"};
        magnitudeSelector = new JComboBox<String>(magnitudesArray);
        magnitudeSelector.setSelectedItem(actualSignalValue.getMagnitude());
        magnitudeSelector.addItemListener(this);
        co.add(magnitudeSelector,gbc);

        gbc.gridy = 2;
        l2 = new JLabel("Rango de la señal:");
        co.add(l2, gbc);

        gbc.gridy = 3;
        
        if(actualSignalValue.getMagnitude()=="Voltaje"){
            signalArray = new String[]{"-5 V a 5 V","0 V a 10 V", "1 V a 5 V"};
        }else{
            signalArray = new String[]{"4 mA a 20 mA","0 mA a 20 mA", "10 mA a 50 mA"};
        }

        signalSelector = new JComboBox<String>(signalArray);
        signalSelector.setSelectedItem(actualSignalValue.getNombre());
        co.add(signalSelector,gbc);

        gbc.gridy = 4;
        l3 = new JLabel("Escalamiento de la Variable:");
        co.add(l3,gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 1;
        l4 = new JLabel("Valor Min: ");
        co.add(l4, gbc);

        gbc.gridx = 1;
        minTextField = new JTextField(Double.toString(actualMinRange));
        co.add(minTextField, gbc);

        gbc.gridx = 2;
        l5 = new JLabel("Valor Max: ");
        co.add(l5, gbc);

        gbc.gridx = 3;
        maxTextField = new JTextField(Double.toString(actualMaxRange));
        co.add(maxTextField, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        l6 = new JLabel("Variable: ");
        co.add(l6, gbc);

        gbc.gridx = 1;
        varArray = new String[]{"Temperatura", "Presión", "Caudal", "Nivel"};
        varSelector = new JComboBox<>(varArray);
        varSelector.setSelectedItem(actualVar.getMagnitud());
        varSelector.addItemListener(this);
        co.add(varSelector, gbc);

        gbc.gridx = 2;
        l7 = new JLabel("Unidad: ");
        co.add(l7, gbc);

        gbc.gridx = 3;
        unitySelector = new JComboBox<>(this.unityArray);
        unitySelector.setSelectedItem(this.actualVar.getNombre());
        co.add(unitySelector, gbc);

        gbc.gridy = 7;
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(24,2,2,2);
        saveButton = new JButton("Guardar Cambios");
        saveButton.addActionListener(this);
        co.add(saveButton,gbc);
    }

    public void itemStateChanged(ItemEvent e){
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String item = (String)e.getItem();
            if(item=="Voltaje"){
                signalSelector.removeAllItems();
                signalArray = new String[]{"-5 V a 5 V","0 V a 10 V", "1 V a 5 V"};
                signalSelector.setModel(new DefaultComboBoxModel<>(signalArray));
            }else if(item=="Corriente"){
                signalSelector.removeAllItems();
                signalArray = new String[]{"4 mA a 20 mA","0 mA a 20 mA", "10 mA a 50 mA"};
                signalSelector.setModel(new DefaultComboBoxModel<>(signalArray));
            }

            if(item=="Temperatura"){
                unitySelector.removeAllItems();
                unitySelector.setModel(new DefaultComboBoxModel<>(Unidad.arrayTemp));
            }else if(item=="Presión"){
                unitySelector.removeAllItems();
                unitySelector.setModel(new DefaultComboBoxModel<>(Unidad.arrayPres));
            }else if(item=="Caudal"){
                unitySelector.removeAllItems();
                unitySelector.setModel(new DefaultComboBoxModel<>(Unidad.arrayCaudal));
            }else if(item=="Nivel"){
                unitySelector.removeAllItems();
                unitySelector.setModel(new DefaultComboBoxModel<>(Unidad.arrayNivel));
            }
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==saveButton){
            
            try {
                actualValue.setTipo(new Tipo(signalSelector.getSelectedItem().toString(), magnitudeSelector.getSelectedItem().toString()));
                actualValue.setMinRange(Double.parseDouble(minTextField.getText()));
                actualValue.setMaxRange(Double.parseDouble(maxTextField.getText()));
                actualValue.setScale();
                actualValue.setUnity(new Unidad(unitySelector.getSelectedItem().toString(), varSelector.getSelectedItem().toString()));
                ChannelsFile file = new ChannelsFile();
                file.updateRow(actualValue);
                dtm.llenarTabla();
                Window.tabla.clearSelection();
                App.ventana.getSide().setDefault();
                this.dispose();
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null,"Los valores introducidos no son valores numéricos válidos","Error",JOptionPane.ERROR_MESSAGE);

            }
            
        }
    }


}
