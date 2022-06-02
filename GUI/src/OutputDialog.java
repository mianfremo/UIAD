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

public class OutputDialog extends JDialog implements ItemListener,ActionListener{
    private Container co;
    private GridBagConstraints gbc;
    private JComboBox<String> outputSelector, freqSelector;
    private JLabel l1,l2,l3,l4,l5,l6;
    private JTextField kpTextField, kiTextField, kdTextField;
    private String[] outputArray, freqArray;
    private JButton saveButton;
    private IO actualValue;
    private IOTableModel dtm;
    private Tipo actualTypeValue;


    public OutputDialog(JFrame f, String nombre, boolean modal){
        super(f,nombre,modal);
        this.setSize(400,400);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.dtm = (IOTableModel)Window.tabla.getModel();
        this.actualValue = Window.channels.get(Window.tabla.getSelectedRow());
        this.actualTypeValue = actualValue.getTipo();

        co = this.getContentPane();
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,2,2,2);
        gbc.fill = GridBagConstraints.BOTH; //Llenar el espacio completo de la celda
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 6;

        l1 = new JLabel("Tipo de salida deseada:");
        co.add(l1,gbc);

        gbc.gridy = 1;
        outputArray = new String[]{"ON/OFF","PWM"};
        outputSelector = new JComboBox<String>(outputArray);
        if(actualTypeValue.getType()==7 || actualTypeValue.getType()==8){
            outputSelector.setSelectedItem(actualTypeValue.getNombre());
        }else{
            outputSelector.setSelectedItem("ON/OFF");
        }
        
        outputSelector.addItemListener(this);
        co.add(outputSelector,gbc);

        gbc.gridy = 2;
        l2 = new JLabel("Frecuencia del PWM (Hz):");
        co.add(l2, gbc);

        gbc.gridy = 3;
        
        if(actualValue.getId()!=11){
            freqArray = new String[]{"3921.16","980.39", "490.2", "245.1", "122.55", "30.64"};
        }else{
            freqArray = new String[]{"7812.5","976.56", "244.14", "61.04"};
        }

        freqSelector = new JComboBox<String>(freqArray);
        freqSelector.setSelectedIndex((int)actualValue.getMinRange());
        co.add(freqSelector,gbc);

        gbc.gridy = 4;
        l3 = new JLabel("Valores de Control:");
        co.add(l3,gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 5;
        l4 = new JLabel("Kp: ");
        co.add(l4,gbc);

        gbc.gridx = 1;
        kpTextField = new JTextField(Double.toString(actualValue.getKp()));
        co.add(kpTextField,gbc);

        gbc.gridx = 2;
        l5 = new JLabel("Ki: ");
        co.add(l5,gbc);

        gbc.gridx = 3;
        kiTextField = new JTextField(Double.toString(actualValue.getKi()));
        co.add(kiTextField,gbc);

        gbc.gridx = 4;
        l6 = new JLabel("Kd: ");
        co.add(l6,gbc);

        gbc.gridx = 5;
        kdTextField = new JTextField(Double.toString(actualValue.getKd()));
        co.add(kdTextField,gbc);

        if(outputSelector.getSelectedItem()=="ON/OFF"){
            freqSelector.setEnabled(false);
            kpTextField.setEnabled(false);
            kiTextField.setEnabled(false);
            kdTextField.setEnabled(false);
        }

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(24,2,2,2);
        saveButton = new JButton("Guardar Cambios");
        saveButton.addActionListener(this);
        co.add(saveButton,gbc);
    }

    public void itemStateChanged(ItemEvent e){
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String item = (String)e.getItem();
            if(item=="ON/OFF"){
                freqSelector.setEnabled(false);
                kpTextField.setEnabled(false);
                kiTextField.setEnabled(false);
                kdTextField.setEnabled(false);
            }else{
                freqSelector.setEnabled(true);
                kpTextField.setEnabled(true);
                kiTextField.setEnabled(true);
                kdTextField.setEnabled(true);
            }

        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==saveButton){
            double kp = 0;
            double ki = 0;
            double kd = 0;

            if(!kpTextField.getText().equals("")){
                kp = Double.parseDouble(kpTextField.getText()); 
                System.out.println("Hello");
            }
            
            if(!kiTextField.getText().equals("")){
                ki = Double.parseDouble(kiTextField.getText()); 
            }
            
            if(!kdTextField.getText().equals("")){
                kd = Double.parseDouble(kdTextField.getText()); 
            }
            
            try {
                actualValue.setTipo(new Tipo(outputSelector.getSelectedItem().toString(), "NC"));
                actualValue.setMinRange(freqSelector.getSelectedIndex());
                actualValue.setKp(kp);
                actualValue.setKi(ki);
                actualValue.setKd(kd);

                ChannelsFile file = new ChannelsFile();
                file.updateRow(actualValue);
                dtm.llenarTabla();
                Window.tabla.clearSelection();
                App.ventana.getSide().setDefault();

                this.dispose();
            } catch (Exception error) {
                error.getStackTrace();
                JOptionPane.showMessageDialog(null,"Los valores introducidos no son valores numéricos válidos (Los valores decimales deben ser escritos con '.')","Error",JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }


}
