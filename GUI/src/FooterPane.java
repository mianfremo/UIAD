import javax.swing.JLabel;
import javax.swing.JPanel;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.FlowLayout;
import java.awt.Color;
import java.io.File;

public class FooterPane extends JPanel{
    private JLabel l1,l2;
    private FlatSVGIcon portStateSvgIcon;
    private CompoundIcon portStateIcon;

    public FooterPane(){
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));

        l1 = new JLabel("Estado de la conexión: ");
        this.add(l1);

        l2 = new JLabel();
        portStateSvgIcon = new FlatSVGIcon(new File("icon/connection.svg"));
        portStateSvgIcon = portStateSvgIcon.derive(15,15);
        portStateSvgIcon.setColorFilter(new ColorFilter(color->new Color(204,204,204)));
        portStateIcon = new CompoundIcon(CompoundIcon.Axis.X_AXIS, 
            new TextIcon(l2, "Desconocido  "),
            portStateSvgIcon);
        l2.setIcon(portStateIcon);

        this.add(l2);

        
    }

    public void updateState(int state){
        if(state==0){
            portStateSvgIcon.setColorFilter(new ColorFilter(color->new Color(0,153,0)));
            portStateIcon = new CompoundIcon(CompoundIcon.Axis.X_AXIS, 
                new TextIcon(l2, "Conexión Exitosa "),
                portStateSvgIcon);
            l2.setIcon(portStateIcon);
        }else if(state==1){
            portStateSvgIcon.setColorFilter(new ColorFilter(color->new Color(255,51,51)));
            portStateIcon = new CompoundIcon(CompoundIcon.Axis.X_AXIS, 
                new TextIcon(l2, "Puerto desconectado "),
                portStateSvgIcon);
            l2.setIcon(portStateIcon);
        }else if(state==2){
            portStateSvgIcon.setColorFilter(new ColorFilter(color->new Color(255,51,51)));
            portStateIcon = new CompoundIcon(CompoundIcon.Axis.X_AXIS, 
                new TextIcon(l2, "Puerto ocupado "),
                portStateSvgIcon);
            l2.setIcon(portStateIcon);
        }else if(state==3){
            portStateSvgIcon.setColorFilter(new ColorFilter(color->new Color(204,204,204)));
            portStateIcon = new CompoundIcon(CompoundIcon.Axis.X_AXIS, 
                new TextIcon(l2, "Desconocido   "),
                portStateSvgIcon);
            l2.setIcon(portStateIcon);
        }
    }
}