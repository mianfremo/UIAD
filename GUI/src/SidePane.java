import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.Box;

public class SidePane extends JPanel implements ActionListener{
    private JLabel l1,l2,l3,l4,l5,l6,l7, l8, l9;
    private JButton b1;
    private JLabel nombre, desc, tipo, variable, unidad, range, control;

    public SidePane(){
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5,15,5,15));
        this.setBackground(Color.WHITE);


        l1 = new JLabel("Información del Canal:");
        l1.setForeground(Color.GRAY);
        this.add(l1);

        Box vbox = Box.createVerticalBox ();
        Component vStrut1 = vbox.createVerticalStrut(10);
        vbox.add(vStrut1);

        this.add(vbox);

        // this.add(new JSeparator(SwingConstants.HORIZONTAL));

        l2 = new JLabel("Nombre");
        l2.setFont(l2.getFont().deriveFont(l2.getFont().getStyle() | Font.BOLD));
        this.add(l2);

        nombre = new JLabel("-");
        this.add(nombre);

        l3 = new JLabel("Descripción");
        l3.setFont(l3.getFont().deriveFont(l3.getFont().getStyle() | Font.BOLD));
        this.add(l3);

        desc = new JLabel("-");
        this.add(desc);

        l4 = new JLabel("Tipo de Señal");
        l4.setFont(l4.getFont().deriveFont(l4.getFont().getStyle() | Font.BOLD));
        this.add(l4);

        tipo = new JLabel("-");
        this.add(tipo);

        l5 = new JLabel("Variable Medida");
        l5.setFont(l5.getFont().deriveFont(l5.getFont().getStyle() | Font.BOLD));
        this.add(l5);

        variable = new JLabel("-");
        this.add(variable);

        l6 = new JLabel("Unidad");
        l6.setFont(l6.getFont().deriveFont(l6.getFont().getStyle() | Font.BOLD));
        this.add(l6);

        unidad = new JLabel("-");
        this.add(unidad);

        l7 = new JLabel("Rango de Medida");
        l7.setFont(l7.getFont().deriveFont(l7.getFont().getStyle() | Font.BOLD));
        this.add(l7);

        range = new JLabel("-");
        this.add(range);

        l8 = new JLabel();
        l8.setFont(l8.getFont().deriveFont(l8.getFont().getStyle() | Font.BOLD));
        this.add(l8);

        control = new JLabel();
        this.add(control);

        l9 = new JLabel("Manipulación Manual");
        l9.setFont(l9.getFont().deriveFont(l9.getFont().getStyle() | Font.BOLD));
        l9.setVisible(false);
        this.add(l9);

        b1 = new JButton("Activar");
        b1.setPreferredSize(new Dimension((int)this.getSize().getWidth(), 30));
        b1.setVisible(false);
        b1.addActionListener(this);
        this.add(b1);



    }

    public void updateInfo(){
        IOTableModel dtm = (IOTableModel)Window.tabla.getModel();

        IO canal = dtm.getElementById(Window.tabla.getSelectedRow());

        nombre.setText(canal.getNombre());
        desc.setText(canal.getDesc());

        if(canal.getIo().getType()==0){
            l4.setText("Tipo de Señal");
            l5.setText("Variable Medida");
            variable.setVisible(true);
            l6.setVisible(true);
            unidad.setVisible(true);
            l7.setVisible(true);
            l7.setText("Rango de Medida");
            range.setVisible(true);
            range.setText(canal.getMinRange()+" "+canal.getUnity().getNombre()+" - "+canal.getMaxRange()+" "+canal.getUnity().getNombre());
            l8.setVisible(false);
            control.setVisible(false);
            l9.setVisible(false);
            b1.setVisible(false);
            
        }else{
            l4.setText("Tipo de Salida");
            l5.setVisible(false);
            variable.setVisible(false);
            l6.setVisible(false);
            unidad.setVisible(false);
            l7.setVisible(false);
            range.setVisible(false);
            l8.setVisible(false);
            control.setVisible(false);
            l9.setVisible(false);
            b1.setVisible(false);
            
            if(canal.getTipo().getType()==8){
                l5.setVisible(true);
                l5.setText("Variable Controlada");
                variable.setVisible(true);
                l7.setVisible(true);
                range.setVisible(true);
                l7.setText("Frecuencia");
                range.setText(canal.getFrequency()+" Hz");
                l8.setVisible(true);
                l8.setText("Valores de Kp, Ki y Kd");
                control.setVisible(true);
                control.setText(canal.getKp()+" - "+canal.getKi()+" - "+canal.getKd());

            }else if(canal.getTipo().getType()==7){
                l5.setVisible(true);
                l5.setText("Variable Controlada");
                variable.setVisible(true);
                b1.setVisible(true);
                l9.setVisible(true);

            }
        }
        tipo.setText(canal.getTipo().getNombre());
        variable.setText(canal.getUnity().getMagnitud());
        unidad.setText(canal.getUnity().getNombre());
    }

    public void setDefault(){

        nombre.setText("-");

        desc.setText("-");

        tipo.setText("-");

        variable.setText("-");

        unidad.setText("-");

        range.setText("-");

        control.setText("-");
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==b1){
            if(Window.connection==0){
                IO actual = Window.channels.get(Window.tabla.getSelectedRow());
    
                byte[] slot = actual.getSlot().getBytes();
    
                if(b1.getText().equals("Activar")){
        
                    byte[] message = new byte[]{
                        (byte)0x30,
                        (byte)0x33,
                        (byte)0x2C,
                        (byte)0x30,
                        (byte)0x26,
                        (byte)slot[0],
                        (byte)slot[1],
                        (byte)0x26,
                        (byte)0x31,
                        (byte)0x0A
                    };
                    // Window.controllers.get(actual.getIntSlot()).setState(1);
                    // actual.getController().setState(1);
                    Window.puerto.writeBytes(message,10);
                }else{
                    byte[] message = new byte[]{
                        (byte)0x30,
                        (byte)0x33,
                        (byte)0x2C,
                        (byte)0x30,
                        (byte)0x26,
                        (byte)slot[0],
                        (byte)slot[1],
                        (byte)0x26,
                        (byte)0x30,
                        (byte)0x0A
                    };
                    
                    Window.controllers.get(actual.getIntSlot()).setState(0);
                    Window.puerto.writeBytes(message,10);
                }
            }else{
                JOptionPane.showMessageDialog(null, "No existe conexión con la UIAD","Error",
                        JOptionPane.ERROR_MESSAGE);
                        System.out.println(Window.tabla.getSelectedRow());
            }
            
            
        }
    }

    public JButton getB1() {
        return b1;
    }
}
