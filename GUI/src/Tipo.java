public class Tipo {
    private String nombre;
    private int type;
    private String magnitude;

    public Tipo(int type){

        this.type = type;

        switch (type) {
            //
            case 0:
                this.nombre = "4 mA a 20 mA";
                this.magnitude = "Corriente";
                break;
            case 1:
                this.nombre = "0 mA a 20 mA";
                this.magnitude = "Corriente";
                break;
            case 2:
                this.nombre = "10 mA a 50 mA"; //Sensor de corriente por efecto hall ACS712
                this.magnitude = "Corriente";
                break;
            case 3:
                this.nombre = "-5 V a 5 V";
                this.magnitude = "Voltaje";
                break;
            case 4:
                this.nombre = "0 V a 10 V";
                this.magnitude = "Voltaje";
                break;
            case 5:
                this.nombre = "1 V a 5 V";
                this.magnitude = "Voltaje";
                break;

            case 7:
                this.nombre = "ON/OFF";
                this.magnitude = "NC";
                break;

            case 8:
                this.nombre = "PWM";
                this.magnitude = "NC";
                break;
                
            case 404:
                this.nombre = "NC";
                this.magnitude = "Voltaje";
                break;
        }

    }

    public Tipo(String nombre, String magnitude){
        this.nombre = nombre;
        this.magnitude = magnitude;

        switch (nombre) {
            case "4 mA a 20 mA":
                this.type = 0;
                break;
            case "0 mA a 20 mA":
                this.type = 1;
                break;
            case "10 mA a 50 mA":
                this.type = 2;
                break;


            case "-5 V a 5 V":
                this.type = 3;
                break;
            case "0 V a 10 V":
                this.type = 4;
                break;
            case "1 V a 5 V":
                this.type = 5;
                break;

            
            case "ON/OFF":
                this.type = 7;
                break;
            case "PWM":
                this.type = 8;
                break;
        }
    }

    public String getNombre(){
        return this.nombre;
    }

    public int getType(){
        return this.type;
    }

    public String getMagnitude(){
        return this.magnitude;
    }

    public char getChar(){
        String cadena = Integer.toString(this.type);
        return cadena.charAt(0);
    }
}
