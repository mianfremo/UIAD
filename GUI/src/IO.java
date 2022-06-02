public class IO {
    private int id;
    private TipoIO io;
    private String nombre;
    private String desc;
    private Tipo tipo;
    private String slot;
    private double minRange, maxRange;
    private Unidad unity;
    private double scale;
    private int state;
    private boolean enabled;
    private double kp, ki, kd;
    private Control controller;

    public IO(int id,TipoIO io, String nombre, String desc , Tipo tipo, String slot , double minRange, double maxRange, Unidad unity, boolean enabled, double kp, double ki, double kd){
        this.id = id;
        this.io = io;
        this.nombre = nombre;
        this.desc = desc;
        this.tipo = tipo;
        this.slot = slot;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.unity = unity;
        this.enabled = enabled;
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public void setScale(){
        this.scale = (this.getMaxRange()-this.getMinRange())/1023.0;
    }

    public double getScale() {
        return scale;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setIo(TipoIO io){
        this.io = io;
    }

    public TipoIO getIo(){
        return this.io;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }
    
    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return this.desc;
    }

    public void setTipo(Tipo tipo){
        this.tipo = tipo;
    }

    public void setController(Control controller) {
        this.controller = controller;
    }

    public Control getController() {
        return controller;
    }

    public Tipo getTipo(){
        return this.tipo;
    }

    public void setSlot(String slot){
        this.slot = slot;
    }

    public String getSlot(){
        return this.slot;
    }

    public void setMinRange(double minRange){
        this.minRange = minRange;
    }

    public void setMaxRange(double maxRange){
        this.maxRange = maxRange;
    }

    public double getMinRange(){
        return this.minRange;
    }

    public int getIntSlot(){
        return(Integer.parseInt(Character.toString(this.slot.charAt(1))));
    }

    public double getFrequency(){
        double freq = 0.0;

        String freqArray[];
        
        if(this.getId()!=11){
            freqArray = new String[]{"3921.16","980.39", "490.2", "245.1", "122.55", "30.64"};
        }else{
            freqArray = new String[]{"7812.5","976.56", "244.14", "61.04"};
        }

        freq = Double.parseDouble(freqArray[(int)this.getMinRange()]);
        
        return freq;
    }
    
    public double getMaxRange(){
        return this.maxRange;
    }

    public void setUnity(Unidad unity){
        this.unity = unity;
    }

    public Unidad getUnity(){
        return this.unity;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled(){
        return this.enabled;
    }

    public void toggleEnabled(){
        this.enabled = !this.enabled;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setKd(double kd) {
        this.kd = kd;
    }
    
    public void setKi(double ki) {
        this.ki = ki;
    }

    public void setKp(double kp) {
        this.kp = kp;
    }

    public double getKd() {
        return kd;
    }

    public double getKi() {
        return ki;
    }

    public double getKp() {
        return kp;
    }

}
