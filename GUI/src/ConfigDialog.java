import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import com.fazecast.jSerialComm.SerialPort;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;
import java.awt.event.ItemEvent;
import java.awt.Color;


public class ConfigDialog extends JDialog implements ActionListener,ItemListener{
    private Container co;
    private GridBagConstraints gbc;
    private JLabel l1, l2, l3, l4;
    private JTextField t1;
    private JComboBox<String> selectPuertos;
    private JButton reloadButton, testButton;
    private SerialPort[] puertos;
    private String[] nombrePuertos;
    private FlatSVGIcon portStateSvgIcon;
    private CompoundIcon portStateIcon;

    public ConfigDialog(JFrame f, String nombre, boolean modal){
        super(f, nombre, modal);
        this.setSize(500,400);
        this.setLocationRelativeTo(null);

        co = this.getContentPane();
        co.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; //Llenar el espacio completo de la celda
        gbc.insets = new Insets(2,4,2,4);  //Espaciado entre elementos

        gbc.gridwidth = 2;

        l1 = new JLabel("Puerto de la UIAD");

        co.add(l1,gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;

        selectPuertos = new JComboBox<>();

        selectPuertos.addItemListener(this);

        co.add(selectPuertos,gbc);
        
        gbc.gridx = 2;
        gbc.gridwidth = 1;

        reloadButton = new JButton("Recargar");
        reloadButton.addActionListener(this);

        co.add(reloadButton,gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        l4 = new JLabel("Número de muestras por segundo");
        co.add(l4, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        t1 = new JTextField("2");
        co.add(t1, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.insets = new Insets(10,4,2,4);

        testButton = new JButton("Probar Conexión");
        testButton.addActionListener(this);
        co.add(testButton,gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 1;
        l2 = new JLabel("Estado de la conexión: ");
        co.add(l2,gbc);

        l3 = new JLabel();
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        portStateSvgIcon = new FlatSVGIcon(new File("icon/connection.svg"));
        portStateSvgIcon = portStateSvgIcon.derive(15,15);
        if(Window.connection==0){
            updateLabelState(0);
        }else{
            updatePuertos(selectPuertos);
            updateLabelState(3);
            l3.setIcon(portStateIcon);
        }
        
        co.add(l3, gbc);

    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==reloadButton){
            updatePuertos(selectPuertos);
        }else if (e.getSource()==testButton){
            if(testButton.getText().equals("Probar Conexión")){
                if(Window.puerto.openPort()){
                    Window.samples = Integer.parseInt(t1.getText());
                    Window.listener = new MessageListener();
                    Window.puerto.addDataListener(Window.listener);
    
                    byte[] message = new byte[]{
                        (byte)0x30,
                        (byte)0x30,
                        (byte)0x0A
                    };
    
    
                    Window.listener.setCaller(this,0);
                    Window.listener.setCaller(App.ventana.getFooter(),1);
    
                    
                    Window.puerto.writeBytes(message,3);
    
                }else{
                    this.updateLabelState(2);
                }
            }else{
                Window.puerto.closePort();
                updateLabelState(3);
                updatePuertos(selectPuertos);
                selectPuertos.setEnabled(true);
                reloadButton.setEnabled(true);
                t1.setEnabled(true);
                App.ventana.getFooter().updateState(3);
            }
        }
    }

    public void updateLabelState(int state){
        switch (state) {
            case 0:
                portStateSvgIcon.setColorFilter(new ColorFilter(color->new Color(0,153,0)));
                portStateIcon = new CompoundIcon(CompoundIcon.Axis.X_AXIS, 
                    new TextIcon(l3, "Conexión Exitosa "),
                    portStateSvgIcon);
                l3.setIcon(portStateIcon);
                selectPuertos.addItem(Window.puerto.getSystemPortName()+"("+Window.puerto.getPortDescription()+")");
                testButton.setText("Finalizar Conexión");
                reloadButton.setEnabled(false);
                selectPuertos.setEnabled(false);
                t1.setEnabled(false);
                App.ventana.getTb().getChannels().getSensor().getCheckButton().setEnabled(true);
                Window.connection = 0;
                Window.tabla.clearSelection();
                break;
        
            case 1:
                portStateSvgIcon.setColorFilter(new ColorFilter(color->new Color(255,51,51)));
                portStateIcon = new CompoundIcon(CompoundIcon.Axis.X_AXIS, 
                    new TextIcon(l3, "Error de Conexión "),
                    portStateSvgIcon);
                l3.setIcon(portStateIcon);
                break;
            case 2:
                portStateSvgIcon.setColorFilter(new ColorFilter(color->new Color(255,51,51)));
                portStateIcon = new CompoundIcon(CompoundIcon.Axis.X_AXIS, 
                    new TextIcon(l3, "Puerto Ocupado "),
                    portStateSvgIcon);
                l3.setIcon(portStateIcon);
                Window.tabla.clearSelection();
                break;
            case 3:
                Window.connection = 4;
                testButton.setText("Probar Conexión");
                portStateSvgIcon.setColorFilter(new ColorFilter(color->new Color(204,204,204)));
                portStateIcon = new CompoundIcon(CompoundIcon.Axis.X_AXIS, 
                    new TextIcon(l3, "Desconocido   "),
                    portStateSvgIcon);
                l3.setIcon(portStateIcon);
                App.ventana.getTb().getChannels().getSensor().getCheckButton().setEnabled(false);
                break;
        }
    }

    public void updatePuertos(JComboBox<String> c){
        puertos = SerialPort.getCommPorts();
        nombrePuertos = new String[puertos.length];
        c.removeAllItems();

        for(int i=0; i<puertos.length; i++){
            nombrePuertos[i] = puertos[i].getSystemPortName()+" ("+puertos[i].getPortDescription()+")";
            c.addItem(nombrePuertos[i]);
        }

        if(puertos.length==0){
            testButton.setEnabled(false);
        }else{
            Window.puerto = puertos[0];
        }
    }
    
    public void itemStateChanged(ItemEvent e){
        if(e.getStateChange() == ItemEvent.SELECTED){
            if(this.puertos!=null){
                Window.puerto = puertos[selectPuertos.getSelectedIndex()];
            }
        }
    }


}
