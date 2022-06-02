
#include <StringSplitter.h>
#include <Utilities.h>

const unsigned int MAX_MESSAGE_LENGTH = 30;
const String SUCCESS = "200";
const String DELIMITER = ",";
int digitalOutput0;
int digitalOutput1;
int digitalOutput2;
int controlOutput;
int analogInput;
static char message[MAX_MESSAGE_LENGTH];
static unsigned int message_pos = 0;

void channel(String in);

void control(String out);

void setup()
{
    
    Serial.begin(9600);
    pinMode(2, OUTPUT);
    pinMode(3, OUTPUT);
    pinMode(4, OUTPUT);
    pinMode(5, OUTPUT);
    pinMode(6, OUTPUT);
    pinMode(7, OUTPUT);
    pinMode(8, OUTPUT);
    pinMode(9, OUTPUT);
    
    pinMode(22, OUTPUT);
    pinMode(23, OUTPUT);
    pinMode(24, OUTPUT);
    pinMode(25, OUTPUT);
    pinMode(26, OUTPUT);
    pinMode(27, OUTPUT);
    pinMode(28, OUTPUT);
    pinMode(29, OUTPUT);
    pinMode(30, OUTPUT);
    pinMode(31, OUTPUT);
    pinMode(32, OUTPUT);
    pinMode(33, OUTPUT);
    pinMode(34, OUTPUT);
    pinMode(35, OUTPUT);
    pinMode(36, OUTPUT);
    pinMode(37, OUTPUT);
    pinMode(38, OUTPUT);
    pinMode(39, OUTPUT);
    pinMode(40, OUTPUT);
    pinMode(41, OUTPUT);
    pinMode(42, OUTPUT);
    pinMode(43, OUTPUT);
    pinMode(44, OUTPUT);
    pinMode(45, OUTPUT);
    
}

void loop()
{
    while (Serial.available() > 0){
        

        // Lee el siguiente byte disponible del buffer serial
        char inByte = Serial.read();

        // Si el byte no es igual al caracter de final de mensajes y la posición del caracter es menor que el tamaño maximo del mensaje entonces:
        if (inByte != '\n' && (message_pos < MAX_MESSAGE_LENGTH - 1)){
            // Se agrega el nuevo byte al mensaje
            message[message_pos] = inByte;
            message_pos++;
        }
        // Una vez se recibe el mensaje completo
        else{
            // Se agrega un caracter nulo al final del array
            message[message_pos] = '\0';

            String messageConverted = message;

            
            
            StringSplitter *splitter = new StringSplitter(messageConverted, ',', 2);
            String codigo = splitter->getItemAtIndex(0);
            String data = splitter->getItemAtIndex(1);

            // Procesamiento del mensaje con el protocolo

            if (codigo == "00")
            {
                Serial.print(message);
                Serial.print(DELIMITER);
                Serial.print(SUCCESS);
                Serial.print(DELIMITER);
                Serial.println("00");

            }else if (codigo == "01"){

                StringSplitter *dataSplitter = new StringSplitter(data, '&', 3);
                String io = dataSplitter->getItemAtIndex(0);

                // Verificar si es una entrada o una salida
                if (String(io[0]) == "I"){
                    // Entrada
                    channel(String(io[1]));
                    String signal = dataSplitter->getItemAtIndex(1);

                    //Señal de 4 a 20 mA
                    if (signal == "0"){

                        digitalWrite(digitalOutput0, HIGH);
                        digitalWrite(digitalOutput1, LOW);
                        digitalWrite(digitalOutput2, LOW);
                        if(analogRead(analogInput)>195){
                            Serial.print(codigo);
                            Serial.print(DELIMITER);
                            Serial.print(SUCCESS);
                            Serial.print(DELIMITER);
                            Serial.print(io+"&"+signal+"&");
                            Serial.println(analogRead(analogInput));
                        }else{
                            Serial.print(codigo);
                            Serial.print(DELIMITER);
                            Serial.print(400);
                            Serial.print(DELIMITER);
                            Serial.print(io+"&"+signal+"&");
                            Serial.println("00");
                        }
                        
                    //Señal de 0 a 20 mA
                    }else if(signal == "1"){
                        digitalWrite(digitalOutput0, HIGH);
                        digitalWrite(digitalOutput1, LOW);
                        digitalWrite(digitalOutput2, LOW);
                        Serial.print(codigo);
                        Serial.print(DELIMITER);
                        Serial.print(SUCCESS);
                        Serial.print(DELIMITER);
                        Serial.print(io+"&"+signal+"&");
                        Serial.println(analogRead(analogInput));

                    //Señal de 10 a 50 mA
                    }else if(signal == "2"){
                        digitalWrite(digitalOutput0, LOW);
                        digitalWrite(digitalOutput1, HIGH);
                        digitalWrite(digitalOutput2, LOW);
                        if(analogRead(analogInput)>195){
                            Serial.print(codigo);
                            Serial.print(DELIMITER);
                            Serial.print(SUCCESS);
                            Serial.print(DELIMITER);
                            Serial.print(io+"&"+signal+"&");
                            Serial.println(analogRead(analogInput));
                        }else{
                            Serial.print(codigo);
                            Serial.print(DELIMITER);
                            Serial.print(400);
                            Serial.print(DELIMITER);
                            Serial.print(io+"&"+signal+"&");
                            Serial.println("00");
                        }

                    //Señal de -5V a 5V
                    }else if (signal == "3"){
                        digitalWrite(digitalOutput0, HIGH);
                        digitalWrite(digitalOutput1, HIGH);
                        digitalWrite(digitalOutput2, LOW);
                        Serial.print(codigo);
                        Serial.print(DELIMITER);
                        Serial.print(200);
                        Serial.print(DELIMITER);
                        Serial.print(io+"&"+signal+"&");
                        Serial.println(analogRead(analogInput));

                    // Señal de 0 a 10V
                    }else if(signal == "4"){
                        digitalWrite(digitalOutput0, LOW);
                        digitalWrite(digitalOutput1, LOW);
                        digitalWrite(digitalOutput2, HIGH);
                        Serial.print(codigo);
                        Serial.print(DELIMITER);
                        Serial.print(200);
                        Serial.print(DELIMITER);
                        Serial.print(io+"&"+signal+"&");
                        Serial.println(analogRead(analogInput));

                    // Señal de 1V a 5V
                    }else if(signal == "5"){
                        digitalWrite(digitalOutput0, HIGH);
                        digitalWrite(digitalOutput1, LOW);
                        digitalWrite(digitalOutput2, HIGH);
                        if(analogRead(analogInput)>195){
                            Serial.print(codigo);
                            Serial.print(DELIMITER);
                            Serial.print(SUCCESS);
                            Serial.print(DELIMITER);
                            Serial.print(io+"&"+signal+"&");
                            Serial.println(analogRead(analogInput));
                        }else{
                            Serial.print(codigo);
                            Serial.print(DELIMITER);
                            Serial.print(400);
                            Serial.print(DELIMITER);
                            Serial.print(io+"&"+signal+"&");
                            Serial.println("00");
                        }
                    }
                    delete dataSplitter;
                }else{
                    //Salida
                    String tipo = dataSplitter->getItemAtIndex(1);
                    control(String(io[1]));

                    Serial.print(codigo);
                    Serial.print(DELIMITER);
                    Serial.print(SUCCESS);
                    Serial.print(DELIMITER);

                    if(tipo=="0"){
                        digitalWrite(controlOutput, HIGH);
                        Serial.println(io+"&"+tipo+"&1");
                    }else{
                        String freq = dataSplitter->getItemAtIndex(2);
                        frequency(String(io[1]), freq);
                        analogWrite(controlOutput, 127);
                        Serial.println(io+"&"+tipo+"&127");
                    }
                    
                }
            }else if(codigo == "02"){
                Serial.print(codigo);
                Serial.print(DELIMITER);
                Serial.print(SUCCESS);
                Serial.print(DELIMITER);

                StringSplitter *dataSplitter = new StringSplitter(data, '&', 8);

                for (int i = 0; i < 8; i++){
                    String io = dataSplitter->getItemAtIndex(i);
                    if(String(io[1])=="1"){
                        if(i!=7){
                            channel(String(io[0]));
                            Serial.print(analogRead(analogInput));
                            Serial.print("&");
                        }else{
                            channel(String(io[0]));
                            Serial.println(analogRead(analogInput));
                        }
                    }else{
                        if(i!=7){
                            Serial.print("X&");
                        }else{
                            Serial.println("X");
                        }   
                    }
                }

                delete dataSplitter;
                
            }else if(codigo=="03"){
                StringSplitter *dataSplitter = new StringSplitter(data, '&', 3);
                String tipo = dataSplitter->getItemAtIndex(0); //Determina si la señal es de tipo ON/OFF o PWM
                String canal = dataSplitter->getItemAtIndex(1); //Determina el canal a controlar
                String valor = dataSplitter->getItemAtIndex(2); //Determina el valor a establecer en la salida

                control(String(canal[1]));

                if(tipo=="0"){
                    digitalWrite(controlOutput,valor.toInt());
                    Serial.print(codigo);
                    Serial.print(DELIMITER);
                    Serial.print(SUCCESS);
                    Serial.print(DELIMITER);
                    Serial.println(canal+"&"+valor);
                }else{
                    analogWrite(controlOutput,valor.toInt());
                    Serial.print(codigo);
                    Serial.print(DELIMITER);
                    Serial.print(SUCCESS);
                    Serial.print(DELIMITER);
                    Serial.println(canal+"&"+valor);
                }

                delete dataSplitter;
              
              
            }

            delete splitter;
            // Reiniciar la posición para el siguiente mensaje
            message_pos = 0;
        }


    }

}

void channel(String in)
{
    if(in=="0"){
        digitalOutput0 = 22;
        digitalOutput1 = 23;
        digitalOutput2 = 24;
        analogInput = 54;
    }else if(in=="1"){
        digitalOutput0 = 25;
        digitalOutput1 = 26;
        digitalOutput2 = 27;
        analogInput = 55;
    }else if(in=="2"){
        digitalOutput0 = 28;
        digitalOutput1 = 29;
        digitalOutput2 = 30;
        analogInput = 56;
    }else if(in=="3"){
        digitalOutput0 = 31;
        digitalOutput1 = 32;
        digitalOutput2 = 33;
        analogInput = 57;  
    }else if(in=="4"){
        digitalOutput0 = 34;
        digitalOutput1 = 35;
        digitalOutput2 = 36;
        analogInput = 58;   
    }else if(in=="5"){
        digitalOutput0 = 37;
        digitalOutput1 = 38;
        digitalOutput2 = 39;
        analogInput = 59;
    }else if(in=="6"){
        digitalOutput0 = 40;
        digitalOutput1 = 41;
        digitalOutput2 = 42;
        analogInput = 60;
    }else if(in=="7"){
        digitalOutput0 = 43;
        digitalOutput1 = 44;
        digitalOutput2 = 45;
        analogInput = 61;               
    }
      
}

void control(String out){
    if(out=="0"){
        controlOutput = 2;
    }else if(out == "1"){
        controlOutput = 3;
    }else if(out == "2"){
        controlOutput = 4;
    }else if(out == "3"){
        controlOutput = 5;
    }else if(out == "4"){
        controlOutput = 6;
    }else if(out == "5"){
        controlOutput = 7;
    }else if(out == "6"){
        controlOutput = 8;
    }else if(out == "7"){
        controlOutput = 9;
    }
}

void frequency(String channel, String freq){
    if(channel=="0" || channel == "1" || channel == "3"){
        if(freq =="0"){ 
            TCCR3B = TCCR3B & B11111000 | B00000010;    // Establecer un divisor al timer 3 de 8 para una frecuencia de 3921.16 Hz
        }else if(freq == "1"){
            TCCR3B = TCCR3B & B11111000 | B00000011;    // Establecer un divisor al timer 3 de 32 para una frecuencia de 980.39 Hz
        }else if(freq == "2"){
            TCCR3B = TCCR3B & B11111000 | B00000100;    // Establecer un divisor al timer 3 de 64 para una frecuencia de 490.20 Hz
        }else if(freq == "3"){
            TCCR3B = TCCR3B & B11111000 | B00000101;    // Establecer un divisor al timer 3 de 128 para una frecuencia de 245.10 Hz
        }else if(freq == "4"){
            TCCR3B = TCCR3B & B11111000 | B00000110;    // Establecer un divisor al timer 3 de 256 para una frecuencia de 122.55 Hz
        }else if(freq == "5"){
            TCCR3B = TCCR3B & B11111000 | B00000111;    // Establecer un divisor al timer 3 de 1024 para una frecuencia de 30.64 Hz
        }
    }

    if(channel == "2"){
        if(freq =="0"){
            TCCR0B = TCCR0B & B11111000 | B00000010;    // Establecer un divisor al timer 0 de 8 para una frecuencia de 7812.50 Hz
        }else if(freq == "1"){
            TCCR0B = TCCR0B & B11111000 | B00000011;    // Establecer un divisor al timer 0 de 64 para una frecuencia de 976.56 Hz
        }else if(freq == "2"){
            TCCR0B = TCCR0B & B11111000 | B00000100;    // Establecer un divisor al timer 0 de 256 para una frecuencia de 244.14 Hz
        }else if(freq == "3"){
            TCCR0B = TCCR0B & B11111000 | B00000101;    // Establecer un divisor al timer 0 de 1024 para una frecuencia de 61.04 Hz
        }
    }

    if(channel=="4" || channel == "5" || channel == "6"){
        if(freq =="0"){
            TCCR4B = TCCR4B & B11111000 | B00000010;    // Establecer un divisor al timer 4 de 8 para una frecuencia de 3921.16 Hz
        }else if(freq == "1"){
            TCCR4B = TCCR4B & B11111000 | B00000011;    // Establecer un divisor al timer 4 de 32 para una frecuencia de 980.39 Hz
        }else if(freq == "2"){
            TCCR4B = TCCR4B & B11111000 | B00000100;    // Establecer un divisor al timer 4 de 64 para una frecuencia de 490.20 Hz
        }else if(freq == "3"){
            TCCR4B = TCCR4B & B11111000 | B00000101;    // Establecer un divisor al timer 4 de 128 para una frecuencia de 245.10 Hz
        }else if(freq == "4"){
            TCCR4B = TCCR4B & B11111000 | B00000110;    // Establecer un divisor al timer 4 de 256 para una frecuencia de 122.55 Hz
        }else if(freq == "5"){
            TCCR4B = TCCR4B & B11111000 | B00000111;    // Establecer un divisor al timer 4 de 1024 para una frecuencia de 30.64 Hz
        }
    }

    if(channel == "7"){
       if(freq =="0"){
            TCCR2B = TCCR2B & B11111000 | B00000010;    // Establecer un divisor al timer 2 de 8 para una frecuencia de 3921.16 Hz
        }else if(freq == "1"){
            TCCR2B = TCCR2B & B11111000 | B00000011;    // Establecer un divisor al timer 2 de 32 para una frecuencia de 980.39 Hz
        }else if(freq == "2"){
            TCCR2B = TCCR2B & B11111000 | B00000100;    // Establecer un divisor al timer 2 de 64 para una frecuencia de 490.20 Hz
        }else if(freq == "3"){
            TCCR2B = TCCR2B & B11111000 | B00000101;    // Establecer un divisor al timer 2 de 128 para una frecuencia de 245.10 Hz
        }else if(freq == "4"){
            TCCR2B = TCCR2B & B11111000 | B00000110;    // Establecer un divisor al timer 2 de 256 para una frecuencia de 122.55 Hz
        }else if(freq == "5"){
            TCCR2B = TCCR2B & B11111000 | B00000111;    // Establecer un divisor al timer 2 de 1024 para una frecuencia de 30.64 Hz
        }
        
    }
}
