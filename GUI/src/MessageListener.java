import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

public class MessageListener implements SerialPortMessageListener{

    private ArrayList<Object> caller = new ArrayList<Object>();
    private int counter = 1;

    @Override
    public int getListeningEvents() { 
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED | SerialPort.LISTENING_EVENT_DATA_WRITTEN;
    }


    @Override
    public byte[] getMessageDelimiter(){ 
        return new byte[] {
            (byte)0x0A
        }; 
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage(){ 
        return true; 
    }

    @Override
    public void serialEvent(SerialPortEvent event){
        switch (event.getEventType()) {
            //Evento para puerto desconectado
            case SerialPort.LISTENING_EVENT_PORT_DISCONNECTED:
                System.out.println("Desconectado");
            //Evento para datos enviados
            case SerialPort.LISTENING_EVENT_DATA_WRITTEN:
                System.out.println("All bytes were successfully transmitted!");
                break;
            
            //Evento para datos recibidos
            case SerialPort.LISTENING_EVENT_DATA_RECEIVED:
                byte[] delimitedMessage = event.getReceivedData();

                //Se convierte a string el mensaje y se eliminan los caracteres adicionales
                String message = new String(delimitedMessage,StandardCharsets.UTF_8);
                message = message.replaceFirst("\n", "");
                message = message.replaceFirst("\r", "");

                System.out.println("Mensaje recibido: "+message);

                String[] decoded = message.split(",",3);
                String req = decoded[0];
                String res = decoded[1];
                String data = decoded[2];

                if(req.equals("00")){
                    ConfigDialog config = (ConfigDialog)caller.get(0);
                    FooterPane f = (FooterPane)caller.get(1);

                    if(res.equals("200")){
                        config.updateLabelState(0);
                        f.updateState(0);
                        
                    }else{
                        config.updateLabelState(1);
                        f.updateState(1);
                    }
                    this.caller.clear();
                }else if(req.equals("01")){
                    if(res.equals("200")){
                        String[] dataDecoded = data.split("&",3);
                        String io = dataDecoded[0];
                        double lectura = Double.parseDouble(dataDecoded[2]);
                        
                        IO signal = (IO)this.caller.get(0);
                        
                        Window.channels.get(signal.getId()-1).setState(1);
                        signal.setScale();
                        
                        if(io.charAt(0)=="I".charAt(0)){
                            String pattern = "#.###";
                            DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                            
                            double lecturaConvertida = lectura*signal.getScale()+signal.getMinRange();
                            JOptionPane.showMessageDialog((JFrame)App.ventana, "La lectura del sensor es: "+decimalFormat.format(lecturaConvertida),"Verificación Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            int result = JOptionPane.showConfirmDialog((JFrame)App.ventana, "El canal de salida "+signal.getNombre()+" se encuentra encendido?", "Confirmar prueba del canal de salida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            if(result == 0){
                                byte[] response = new byte[10];
                                if(signal.getTipo().getType()==7){
                                    response = new byte[]{
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
                                }else{
                                    response = new byte[]{
                                        (byte)0x30,
                                        (byte)0x33,
                                        (byte)0x2C,
                                        (byte)0x31,
                                        (byte)0x26,
                                        (byte)io.charAt(0),
                                        (byte)io.charAt(1),
                                        (byte)0x26,
                                        (byte)0x30,
                                        (byte)0x0A
                                    };
                                }

                                Window.puerto.writeBytes(response,10);
                            }
                        }
                        this.caller.clear();
                        if(App.ventana.getTb().getChannels().getSensor().getDiagnosticFlag()){
                            App.ventana.getTb().getChannels().getSensor().diagnostic();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "El sensor está presentando errores (lectura errónea)","Verificación Fallida",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }else if(req.equals("02")){
                    String[] tmp = data.split("&");

                    for (int i = 0; i < tmp.length; i++) {
                        String lectura = tmp[i];

                        if(!lectura.equals("X")){

                            IO signal = Window.channels.get(i);
                            signal.setScale();
                            double lecturaConvertida = Double.parseDouble(lectura)*signal.getScale()+signal.getMinRange();
                            
                            double[] copia = new double[Window.medidas.get(i).length];

                            System.arraycopy(Window.medidas.get(i), 1, copia, 0, Window.medidas.get(i).length-1);

                            System.arraycopy(copia, 0, Window.medidas.get(i), 0, copia.length);

                            Window.medidas.get(i)[299] = lecturaConvertida;

                            PlayDialog graph = (PlayDialog)this.caller.get(0);
                            PlotDialog plot = (PlotDialog)this.caller.get(1);
                            graph.updateChannel(signal);
                            plot.updateChart(signal,counter);

                            if(signal.getController()!=null){
                                signal.getController().makeControl(lecturaConvertida);
                            }
                        }
                    }
                    counter++;
                }else if(req.equals("03")){
                    String[] tmp = data.split("&");
                    if(tmp[1].equals("1")){
                        App.ventana.getSide().getB1().setText("Desactivar");
                    }else{
                        App.ventana.getSide().getB1().setText("Activar");
                    }
                }

                

        }
        
    }

    public void setCaller(Object caller,int index) {
        try {
            this.caller.set(index,caller);
        } catch (Exception e) {
            this.caller.add(caller);
        }
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
