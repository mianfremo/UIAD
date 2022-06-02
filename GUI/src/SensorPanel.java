import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Color;

public class SensorPanel extends JPanel implements ActionListener, ListSelectionListener{
    private JLabel l1;
    private JButton editButton, signalButton, checkButton, disableButton, verifyButton;
    private GridBagConstraints gbc;
    private CompoundIcon checkIcon, disableIcon;
    private FlatSVGIcon editIcon, signalIcon, checkSVG, disableSVG, verifyIcon;
    private boolean diagnosticFlag;

    public SensorPanel(){
        this.setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; //Llenar el espacio completo de la celda
        gbc.insets = new Insets(1,4,1,1);  //Espaciado entre elementos
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        disableSVG = new FlatSVGIcon(new File("icon/disable.svg"));
        disableSVG = disableSVG.derive(30,20);
        disableButton = new JButton();
        disableIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
            disableSVG,
            new TextIcon(disableButton, "Inhabilitar"));
        disableButton.setIcon(disableIcon);
        disableButton.setPreferredSize(new Dimension(80,60));
        disableButton.addActionListener(this);
        this.add(disableButton,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(1,1,1,1);  //Espaciado entre elementos
        checkSVG = new FlatSVGIcon(new File("icon/diagnostic.svg"));
        checkSVG = checkSVG.derive(18,23);
        checkSVG.setColorFilter(new ColorFilter(color -> new Color(0,102,0)));
        checkButton = new JButton();
        checkIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
            checkSVG,
            new TextIcon(checkButton, "Diagnóstico"));
        checkButton.setIcon(checkIcon);
        checkButton.setPreferredSize(new Dimension(80,75));
        checkButton.setEnabled(false);
        checkButton.setToolTipText("Debes comprobar tu conexión con la UIAD antes de verificar el sensor");
        checkButton.addActionListener(this);
        this.add(checkButton,gbc);

        gbc.gridheight = 1;
        gbc.gridx = 2;
        gbc.gridy = 0;
        signalIcon = new FlatSVGIcon(new File("icon/signal.svg"));
        signalIcon = signalIcon.derive(15,15);
        signalButton = new JButton("Tipo");
        signalButton.setIcon(signalIcon);
        signalButton.setEnabled(false);
        signalButton.addActionListener(this);
        this.add(signalButton,gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        editIcon = new FlatSVGIcon(new File("icon/edit.svg"));
        editIcon = editIcon.derive(11,11);
        editButton = new JButton("Editar");
        editButton.setIcon(editIcon);
        editButton.setEnabled(false);
        editButton.addActionListener(this);
        this.add(editButton,gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        verifyIcon = new FlatSVGIcon(new File("icon/check.svg"));
        verifyIcon = verifyIcon.derive(10,15);
        verifyButton = new JButton("Verificar Canal");
        verifyButton.setIcon(verifyIcon);
        verifyButton.setEnabled(false);
        verifyButton.addActionListener(this);
        this.add(verifyButton,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(8,0,0,0);
        l1 = new JLabel("Canales");
        l1.setEnabled(false);
        l1.setHorizontalAlignment(JLabel.CENTER);
        this.add(l1,gbc);

        System.out.println(l1.getFont().getFontName());

        Window.tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Window.tabla.getSelectionModel().addListSelectionListener(this);
    }

    public void actionPerformed(ActionEvent e){
    
        if(e.getSource() == editButton){
            if(Window.tabla.getSelectedRow()!=-1){
                EditDialog editDialog = new EditDialog((JFrame)SwingUtilities.getRoot(this), "Editar Campo", true);
                editDialog.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null,"No has seleccionado ningún canal","Error",JOptionPane.ERROR_MESSAGE);
            }
            
        }else if(e.getSource() == signalButton){
            if(Window.tabla.getSelectedRow()!=-1){
                IOTableModel dtm = (IOTableModel)Window.tabla.getModel();
                IO actual = dtm.getElementById(Window.tabla.getSelectedRow());
                if(actual.getIo().getType()==0){
                    SignalDialog signalDialog = new SignalDialog((JFrame)SwingUtilities.getRoot(this), "Configurar Señal del Sensor", true);
                    signalDialog.setVisible(true);
                }else{
                    OutputDialog outputDialog = new OutputDialog((JFrame)SwingUtilities.getRoot(this), "Configurar Canal de Salida", true);
                    outputDialog.setVisible(true);
                }
            }else{
                JOptionPane.showMessageDialog(null,"No has seleccionado ningún canal","Error",JOptionPane.ERROR_MESSAGE);
            }
            
        }else if(e.getSource() == disableButton){
            if(Window.tabla.getSelectedRow()!=-1){
                IOTableModel dtm = (IOTableModel)Window.tabla.getModel();
                IO actual = dtm.getElementById(Window.tabla.getSelectedRow());

                actual.toggleEnabled();
                ChannelsFile file = new ChannelsFile();
                file.updateRow(actual);
                dtm.llenarTabla();
            }else{
                JOptionPane.showMessageDialog(null,"No has seleccionado ningún canal","Error",JOptionPane.ERROR_MESSAGE);
            }

        }else if(e.getSource() == checkButton){
            boolean flag = true;
            for (IO canal : Window.channels) {
                if(canal.getEnabled() && canal.getTipo().getType()==404){
                    flag = false;
                }
            }

            if(flag){
                ArrayList<IO> habilitados = new ArrayList<IO>();
            
                String msg = "<html>Estás seguro que todos los canales están configurados correctamente?"
                    +"<ul>";
    
                for (int i = 0; i < Window.channels.size(); i++) {
                    IO canal = Window.channels.get(i);
                    if(canal.getEnabled()){
                        habilitados.add(canal);
                        if(canal.getIo().getType()==0){
                            msg = msg
                            +"<li><b>Canal:</b> "+canal.getNombre()
                                +"<ul>"
                                    +"<li><b>Tipo de Señal:</b> "+canal.getTipo().getNombre()+"</li>"
                                    +"<li><b>Rango:</b> "+canal.getMinRange()+" "+canal.getUnity().getNombre()+" - "+canal.getMaxRange()+" "+canal.getUnity().getNombre()+"</li>"
                                +"</ul>"
                            +"</li>";
                        }else{
                            msg = msg
                            +"<li><b>Canal:</b> "+canal.getNombre()
                                +"<ul>"
                                    +"<li><b>Tipo de Salida:</b> "+canal.getTipo().getNombre()+"</li>";
                                
                            if(canal.getTipo().getType()==8){
                                msg = msg
                                    +"<li><b>Frecuencia del PWM:</b> "+canal.getFrequency()+" Hz"+"</li>"
                                    +"<li><b>Valores de Control:</b> Kp: "+canal.getKp()+" Ki: "+canal.getKi()+" Kd: "+canal.getKd()+"</li>";
                            }
    
                            msg = msg
                                    +"</ul>"
                                +"</li>";
                        }
                        
                    }
                }
    
                msg = msg
                    +"</ul>"
                    +"</html>";
                JLabel l = new JLabel(msg);
                
    
                int result = JOptionPane.showConfirmDialog((JFrame)SwingUtilities.getRoot(this), l, "Confirmar prueba del sensor", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if(result==0){
                    this.diagnosticFlag = true;
                    this.diagnostic();
                }
                
            }else{
                JOptionPane.showMessageDialog(null,"Todos los canales deben estar configurados para realizar el diagnóstico","Error",JOptionPane.ERROR_MESSAGE);
            }
            

        }else if(e.getSource()==verifyButton){
            if(Window.connection==0){
                IOTableModel dtm = (IOTableModel)Window.tabla.getModel();
                IO actual = dtm.getElementById(Window.tabla.getSelectedRow());
                
                if(actual.getIo().getType()==0){
                    String msg = "<html>Estás seguro que el canal <b>"+actual.getNombre()
                        +"</b> está configurado correctamente?"
                        +"<ul>"
                            +"<li><b>Tipo de Señal:</b> "+actual.getTipo().getNombre()+"</li>"
                            +"<li><b>Rango de la magnitud:</b> "+actual.getMinRange()+" - "+actual.getMaxRange()+"</li>"
                        +"</ul>"
                        +"</html>";
                    JLabel l = new JLabel(msg);
        
                    byte[] slot = actual.getSlot().getBytes();
        
                    int result = JOptionPane.showConfirmDialog((JFrame)SwingUtilities.getRoot(this), l, "Confirmar prueba del sensor", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if(result==0){
                        byte[] message = new byte[]{
                            (byte)0x30,
                            (byte)0x31,
                            (byte)0x2C,
                            (byte)slot[0],
                            (byte)slot[1],
                            (byte)0x26,
                            (byte)actual.getTipo().getChar(),
                            (byte)0x26,
                            (byte)0x30,
                            (byte)0x0A
                        };

                        Window.listener.setCaller(actual,0);
                        
                        Window.puerto.writeBytes(message,11);
                    }
                }else{
                    String msg = "<html>Estás seguro que el canal <b>"+actual.getNombre()
                        +"</b> está configurado correctamente?"
                        +"<ul>"
                            +"<li><b>Tipo de Salida:</b> "+actual.getTipo().getNombre()+"</li>";
                    
                    if(actual.getTipo().getType()==7){
                        msg = msg+"</ul>"
                        +"</html>";
                    }else{
                        msg = msg +"<li><b>Frecuencia del PWM:</b> "+actual.getFrequency()+" Hz </li>"
                        +"<li><b>Valores de Control:</b> Kp: "+actual.getKp()+" Ki: "+actual.getKi()+" Kd: "+actual.getKd()+"</li>"
                        +"</ul>"
                        +"</html>";
                    }
                            
                    JLabel l = new JLabel(msg);
        
                    byte[] slot = actual.getSlot().getBytes();
                    int result = JOptionPane.showConfirmDialog((JFrame)SwingUtilities.getRoot(this), l, "Confirmar prueba del canal de salida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if(result==0){
                        byte tipo;
                        if(actual.getTipo().getType()==7){
                            tipo = 0x30;
                        }else{
                            tipo = 0x31;
                        }

                        byte freq = Integer.toString((int)actual.getMinRange()).getBytes()[0];

                        byte[] message = new byte[]{
                            (byte)0x30,
                            (byte)0x31,
                            (byte)0x2C,
                            (byte)slot[0],
                            (byte)slot[1],
                            (byte)0x26,
                            tipo,
                            (byte)0x26,
                            freq,
                            (byte)0x0A
                        };
        
                        Window.listener.setCaller(actual,0);
        
                        Window.puerto.writeBytes(message,10);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"No te encuentras conectado con la UIAD","Error",JOptionPane.ERROR_MESSAGE);
                Window.tabla.clearSelection();
                verifyButton.setEnabled(false);
            }
        }

    }

    public void valueChanged(ListSelectionEvent e){
        if(Window.tabla.getSelectedRow()!=-1){
            IOTableModel dtm = (IOTableModel)Window.tabla.getModel();
            IO actual = dtm.getElementById(Window.tabla.getSelectedRow());

            editButton.setEnabled(true);
            signalButton.setEnabled(true);

            if(Window.connection == 0){
                checkButton.setEnabled(true);
            }else{
                checkButton.setEnabled(false);
            }

            if(Window.connection == 0 && actual.getTipo().getType()!=404 && actual.getEnabled()){
                verifyButton.setEnabled(true);
            }else{
                verifyButton.setEnabled(false);
            }

            if(actual.getEnabled()){
                disableIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
                    disableSVG,
                    new TextIcon(disableButton, "Inhabilitar"));
                disableButton.setIcon(disableIcon);

            }else{
                disableIcon = new CompoundIcon(CompoundIcon.Axis.Y_AXIS,
                    disableSVG,
                    new TextIcon(disableButton, "Habilitar"));
                disableButton.setIcon(disableIcon);
            }

            App.ventana.getSide().updateInfo();

        }
    }

    public void diagnostic(){
        int counter = 0;
        for (IO io : Window.channels) {
            if(io.getEnabled() && io.getState()!=1){
                byte[] slot = io.getSlot().getBytes();
                byte[] message = new byte[12];
    
                if(io.getIo().getType()==0){
                    message = new byte[]{
                        (byte)0x30,
                        (byte)0x31,
                        (byte)0x2C,
                        (byte)slot[0],
                        (byte)slot[1],
                        (byte)0x26,
                        (byte)io.getTipo().getChar(),
                        (byte)0x26,
                        (byte)0x30,
                        (byte)0x0A
                    };
                }else{
                    byte tipo;
                    if(io.getTipo().getType()==7){
                        tipo = 0x30;
                    }else{
                        tipo = 0x31;
                    }

                    byte freq = Integer.toString((int)io.getMinRange()).getBytes()[0];

                    message = new byte[]{
                        (byte)0x30,
                        (byte)0x31,
                        (byte)0x2C,
                        (byte)slot[0],
                        (byte)slot[1],
                        (byte)0x26,
                        tipo,
                        (byte)0x26,
                        freq,
                        (byte)0x0A
                    };

                }
    
                System.out.println(new String(message));
                
                Window.listener.setCaller(io,0);
                        
                Window.puerto.writeBytes(message,message.length);
    
                counter++;
    
                break;
            }
            
        }
        if(counter==0){
            diagnosticFlag = false;
            JOptionPane.showMessageDialog(null,"El diagnóstico fue completado de forma exitosa","Diagnóstico exitoso",JOptionPane.INFORMATION_MESSAGE);
            
        }
    }

    public boolean getDiagnosticFlag(){
        return this.diagnosticFlag;
    }

    public JButton getCheckButton() {
        return checkButton;
    }
    
}
