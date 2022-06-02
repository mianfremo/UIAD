import javax.swing.JLabel;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;


class PlayLabel extends JLabel implements MouseMotionListener{


    public PlayLabel(String text){
        super.setText(text);

        addMouseMotionListener(this);

    
    }
    
    
    
    public void mouseDragged(MouseEvent mme) {
    
        setLocation(
        
            this.getX() + mme.getX() - this.getWidth() / 2,
            
            this.getY() + mme.getY() - this.getHeight() / 2
        
        );
    }

    public void mouseMoved(MouseEvent mme) {}

}
    