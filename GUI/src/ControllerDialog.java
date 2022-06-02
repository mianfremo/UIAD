import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

public class ControllerDialog extends JDialog implements ItemListener,ActionListener{
    private Control actual;
    private ArrayList<Control> controllers;
    private JTextField t1, t2, t3;
    private JComboBox<String> channel, tipo;
    private JLabel l1,l2,l3,l4,l5;
    private GridBagConstraints gbc;
    private JButton save;

    public ControllerDialog(JFrame f, String nombre, boolean modal, Control actual, ArrayList<Control> controllers){
        super(f,nombre,modal);
        this.setSize(400,400);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());

        this.setActual(actual);
        this.setControllers(controllers);

        
        this.gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; //Llenar el espacio completo de la celda
        gbc.insets = new Insets(1,1,2,1);

        l1 = new JLabel("Canal de Salida");
        this.add(l1,gbc);

        gbc.gridwidth = 4;
        gbc.gridy = 1;
        t1 = new JTextField(actual.getCanal().getSlot());
        t1.setEnabled(false);
        this.add(t1,gbc);

        gbc.gridy = 2;
        l2 = new JLabel("Canal Controlado");
        this.add(l2,gbc);

        gbc.gridy = 3;

        String[] canales = new String[]{
            "",
            "IO",
            "I1",
            "I2",
            "I3",
            "I4",
            "I5",
            "I6",
            "I7",
        };

        channel = new JComboBox<String>(canales);

        this.add(channel,gbc);

        gbc.gridy = 4;
        l3 = new JLabel("Tipo");
        this.add(l3,gbc);

        gbc.gridy = 5;
        String[] tipos = new String[]{"ON/OFF", "PWM"};
        tipo = new JComboBox<String>(tipos);
        
        if(actual.getType()==0){
            tipo.setSelectedIndex(0);
        }else if(actual.getType()==1){
            tipo.setSelectedIndex(1);
        }

        tipo.addItemListener(this);

        this.add(tipo,gbc);

        gbc.gridy = 6;
        l4 = new JLabel();
        this.add(l4,gbc);

        gbc.gridy = 7;
        t2 = new JTextField("0.0");
        this.add(t2,gbc);

        gbc.gridy = 8;
        l5 = new JLabel("Límite Inferior");
        l5.setVisible(false);
        this.add(l5,gbc);

        gbc.gridy = 9;
        t3 = new JTextField(Double.toString(actual.getP2()));
        t3.setVisible(false);
        this.add(t3,gbc);

        if(tipo.getSelectedIndex()==0){
            l4.setText("Límite Superior");
            t2.setText(Double.toString(actual.getP1()));
            l5.setVisible(true);
            t3.setVisible(true);
        }else{
            l4.setText("Set Point");
            t2.setText(Double.toString(actual.getSp()));
        }

        gbc.gridy = 10;
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20,1,1,1);
        save = new JButton("Guardar Cambios");
        save.addActionListener(this);
        this.add(save,gbc);


    }

    public void itemStateChanged(ItemEvent e){
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String item = (String)e.getItem();
            if(item=="ON/OFF"){
                l4.setText("Límite Superior");
                l5.setVisible(true);
                t2.setText(Double.toString(actual.getP1()));
                t3.setText(Double.toString(actual.getP2()));
                t3.setVisible(true);
            }else{
                l4.setText("Set Point");
                t2.setText(Double.toString(actual.getSp()));
                l5.setVisible(false);
                t3.setVisible(false);
            }

        }
    }

    public void setActual(Control actual) {
        this.actual = actual;
    }

    public void setControllers(ArrayList<Control> controllers) {
        this.controllers = controllers;
    }

    public ArrayList<Control> getControllers() {
        return controllers;
    }
    
    public Control getActual() {
        return actual;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(save)){
            try{
                actual.setType(tipo.getSelectedIndex());
                if(channel.getSelectedItem().equals("")){
                    actual.setIn(404);
                }else{
                    actual.setIn(channel.getSelectedIndex()-1);
                }
                if(tipo.getSelectedIndex()==0){
                    actual.setP1(Double.parseDouble(t2.getText()));
                    actual.setP2(Double.parseDouble(t3.getText()));
                }else{
                    actual.setSp(Double.parseDouble(t2.getText()));
                }

                int id = Integer.parseInt(Character.toString(actual.getCanal().getSlot().charAt(1)));

                controllers.set(id,actual);

                ControlFile file = new ControlFile();
                file.updateFile(controllers);

                ControllerListDialog cld = new ControllerListDialog(null, "Lista de Controladores", true);

                this.dispose();
                
                cld.setVisible(true);


            }catch (Exception error) {
                JOptionPane.showMessageDialog(null,"Los valores introducidos no son valores numéricos válidos (Los valores decimales deben ser escritos con '.')","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
