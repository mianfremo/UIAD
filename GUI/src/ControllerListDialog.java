import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;



public class ControllerListDialog extends JDialog implements ActionListener{
    private GridBagConstraints gbc;
    private JLabel l1,l2,l3,l4;
    private ArrayList<Control> controllers;
    private ArrayList<JButton> buttons;

    public ControllerListDialog(JFrame f, String nombre, boolean modal){
        super(f,nombre,modal);
        this.setSize(600,400);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
        this.gbc.insets = new Insets(1,8,1,8);

        this.buttons = new ArrayList<JButton>();

        ControlFile file = new ControlFile();
        controllers = file.getControllers();

        l1 = new JLabel("Controlador");
        l1.setFont(l1.getFont().deriveFont(l1.getFont().getStyle() | Font.BOLD));
        this.add(l1,gbc);

        gbc.gridx = 1;
        l2 = new JLabel("Canal de Salida");
        l2.setFont(l2.getFont().deriveFont(l2.getFont().getStyle() | Font.BOLD));
        this.add(l2,gbc);

        gbc.gridx = 2;
        l3 = new JLabel("Canal Controlado");
        l3.setFont(l3.getFont().deriveFont(l3.getFont().getStyle() | Font.BOLD));
        this.add(l3,gbc);

        gbc.gridx = 3;
        l4 = new JLabel("Tipo");
        l4.setFont(l4.getFont().deriveFont(l4.getFont().getStyle() | Font.BOLD));
        this.add(l4,gbc);

        gbc.gridx = 4;
        JLabel l5 = new JLabel("");
        this.add(l5,gbc);

        for (int i = 1; i < 9; i++) {
            gbc.gridy = i;

            gbc.gridx = 0;
            JLabel id = new JLabel(Integer.toString(i));
            this.add(id,gbc);

            gbc.gridx = 1;
            JLabel out = new JLabel("O"+(i-1));
            this.add(out,gbc);

            gbc.gridx = 2;
            JLabel io = new JLabel();
            if(controllers.get(i-1).getIn()==404){
                io.setText("-");
            }else{
                io.setText("I"+controllers.get(i-1).getIn());
            }
            this.add(io,gbc);

            gbc.gridx = 3;
            JLabel tipo = new JLabel();
            if(controllers.get(i-1).getType()==404){
                tipo.setText("-");
            }else if(controllers.get(i-1).getType()==0){
                tipo.setText("ON/OFF");
            }else{
                tipo.setText("PWM");
            }
            this.add(tipo,gbc);

            gbc.gridx = 4;
            JButton b = new JButton("Editar");
            b.setActionCommand(Integer.toString(i-1));
            b.addActionListener(this);
            buttons.add(b);
            this.add(b,gbc);
        }


    }

    public void actionPerformed(ActionEvent e){
        Control c = controllers.get(Integer.parseInt(e.getActionCommand()));
        ControllerDialog edit = new ControllerDialog(null, "Configurar Controlador "+(Integer.parseInt(e.getActionCommand())+1), true, c, controllers);
        this.dispose();
        edit.setVisible(true);

    }
}
