
public class Control {
    private int type, state;
    private double p1,p2,p3,p4 = 0;
    private double sp;
    private IO canal;
    private int in;

    public Control(int type, IO canal){
        this.type = type;
        this.canal = canal;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setP1(double p1) {
        this.p1 = p1;
    }
    
    public void setP2(double p2) {
        this.p2 = p2;
    }

    public void setP3(double p3) {
        this.p3 = p3;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCanal(IO canal) {
        this.canal = canal;
    }

    public void setSp(double sp) {
        this.sp = sp;
    }

    public void setIn(int in) {
        this.in = in;
    }
    
    public int getIn() {
        return in;
    }

    public IO getCanal() {
        return canal;
    }

    public double getP1() {
        return p1;
    }

    public double getP2() {
        return p2;
    }

    public double getP3() {
        return p3;
    }

    public int getState() {
        return state;
    }

    public double getSp() {
        return sp;
    }

    public int getType() {
        return type;
    }

    public void makeControl(double lectura){
        String io = canal.getSlot();

        if(this.type==0){ // Caso de ser ON/OFF

            if(state==1 && lectura>p1){

                byte[] message = new byte[]{
                    (byte)0x30,
                    (byte)0x33,
                    (byte)0x2C,
                    (byte)0x30,
                    (byte)0x26,
                    (byte)io.charAt(0),
                    (byte)io.charAt(1),
                    (byte)0x26,
                    (byte)0x30,
                    (byte)0x0A
                };
                this.state = 0;

                Window.puerto.writeBytes(message, message.length);
                App.ventana.getSide().getB1().setText("Activar");

            }else if(state==0 && lectura<p2){
                byte[] message = new byte[]{
                    (byte)0x30,
                    (byte)0x33,
                    (byte)0x2C,
                    (byte)0x30,
                    (byte)0x26,
                    (byte)io.charAt(0),
                    (byte)io.charAt(1),
                    (byte)0x26,
                    (byte)0x31,
                    (byte)0x0A
                };
                this.state = 1;

                Window.puerto.writeBytes(message, message.length);
                App.ventana.getSide().getB1().setText("Desactivar");

            }
        }else{
            p1 = sp-lectura;
            
            double k1 = p1*(canal.getKp()+canal.getKi()+canal.getKd());
            double k2 = p2*(-2*canal.getKd()-canal.getKp());
            double k3 = p3*canal.getKd();

            double salida = k1+k2+k3+p4;

            p2 = p1;
            p3 = p2;
            p4 = salida;
        
            if(salida>=255){
                salida = 255;
            }else if(salida<=0){
                salida= 0;
            }

            byte[] duty = Integer.toString((int)salida).getBytes();

            byte[] message = new byte[9+duty.length];
            message[0] = (byte)0x30;
            message[1] = (byte)0x33;
            message[2] = (byte)0x2C;
            message[3] = (byte)0x31;
            message[4] = (byte)0x26;
            message[5] = (byte)io.charAt(0);
            message[6] = (byte)io.charAt(1);
            message[7] = (byte)0x26;
            for (int i = 0; i < duty.length; i++) {
                message[8+i] = duty[i];
            }
            message[message.length-1] = (byte)0x0A;

            Window.puerto.writeBytes(message, message.length);
                

        }
    }
}
