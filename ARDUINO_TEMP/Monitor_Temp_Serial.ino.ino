#include <Wire.h>
#include <LiquidCrystal_I2C.h>


/*________________________________
  |Original Sketch By Aaron Kable|
  --------------------------------
  Monitoreo de PC mediante: Arduino + Open hardware monitor, Con LCD 20x4 
  Sketch modificado Por "Fenrir93" 22/11/2016
  
 */
LiquidCrystal_I2C lcd(0x27,20,4); // Declaramos nuestra LCD

/*Creamos Carateres especiales para nuestro monitor*/
  byte line5[8] =
  {// full
    B11111,
    B00000,
    B11111,
    B11111,
    B11111,
    B11111,
    B00000,
    B11111
  };

  byte line4[8] =
  {// 80%
    B11111,
    B00000,
    B11110,
    B11110,
    B11110,
    B11110,
    B00000,
    B11111
  };

  byte line3[8] =
  {// 60%
    B11111,
    B00000,
    B11100,
    B11100,
    B11100,
    B11100,
    B00000,
    B11111
  };

  byte line2[8] =
  {// 40%
    B11111,
    B00000,
    B11000,
    B11000,
    B11000,
    B11000,
    B00000,
    B11111
  };

  byte line1[8] =
  { //  20%
    B11111,
    B00000,
    B10000,
    B10000,
    B10000,
    B10000,
    B00000,
    B11111
  };

  byte line0[8] =
  { // empty
    B11111,
    B00000,
    B00000,
    B00000,
    B00000,
    B00000,
    B00000,
    B11111
  };
  
  byte loadchar[8] =
  { // Caracter % para la grafica
    B11111,
    B00000,
    B10010,
    B00100,
    B01000,
    B10010,
    B00000,
    B11111
  };
  byte tempchar[8] =
  { // termometro
    B00100,
    B01010,
    B01010,
    B01010,
    B01010,
    B10001,
    B10001,
    B01110
  };

String inputString = "";  //String de entrada compuesto

boolean NewData = false;

//Declaramos strings de datos fraccionando el string compuesto
String CPUT = ""; //Temperatura de CPU
String GPUT = ""; // Temperatura de GPU
String GPUFC = ""; // % de control del ventilador de la GPU RPM GPU
String MotherT= ""; // Temperatura de la MotherBoard
String HDDT = ""; // Temperatura del disco duro
//Variables para conversiones matematicas, a partir de inputString
int Cload= 0; // Variable de la carga actual del Procesador
int Gload= 0; //variable de la carga actual de la GPU
int CFanSpeed= 0; //Variable RPM Ventilador de CPU (revisar conector en motherboard)
int SpeedMap= 0; // Variable de conversion de RPM a %(0 a 100);
int RLoad= 0; // % de Uso de la ram total 0 a 100
int Aviso1= 0; //Variable extra para proporcionar un aviso visual o auditivo, cuando la temperatura llegue a cierto Valor
int Aviso2= 0; //Variable extra para proporcionar un aviso visual o auditivo, cuando la temperatura llegue a cierto Valor

void setup() {
 
  // setup LCD
  lcd.init();
  //lcd.backlight(); //Si borramos los "//" del inicio la retroiluminacion del lcd se activa al encender la PC
  Serial.begin(9600);
  // Reservamos 400 bits para inputString:
  inputString.reserve(400);

  lcd.backlight();
  lcd.setCursor(2,0);
  lcd.print("Monitor de temp.");
  lcd.setCursor(3,1);
  lcd.print("Arduino");
  lcd.setCursor(4,2);
  lcd.print("By jmXoft");
  lcd.setCursor(7,3);
  lcd.print("Para AMD");
  delay(4000);
  lcd.clear();
  lcd.noBacklight();
 /* //Descripcion de los datos que se imprimiran en la LCD  
  lcd.backlight();
  lcd.setCursor(1,0);
  lcd.print("CPU-Temp-Fan-Load");
  lcd.setCursor(1,1);
  lcd.print("GPU-Temp-Fan-Load");
  lcd.setCursor(6,2);
  lcd.print("RAM Load");
  lcd.setCursor(4,3);
  lcd.print("Mother & HDD");
  delay(4000);
  lcd.clear();
  lcd.noBacklight();*/

  
  

  /*Declaramos pines 12 y 13 como salida*/
  pinMode(13, OUTPUT);
  pinMode(12, OUTPUT);
  
  
  //Creamos los carater especiales
  lcd.createChar(6, line0); // No se puede usar"0"
  lcd.createChar(1, line1);
  lcd.createChar(2, line2);
  lcd.createChar(3, line3);
  lcd.createChar(4, line4);
  lcd.createChar(5, line5);
  lcd.createChar(7, loadchar);
  lcd.createChar(8, tempchar);
  

}
int iHome = 1;
int diff = 1;
int count = 5; 
int dely = 0;


//Dibujamos la graficas con los caracteres especiales
void graph(int load, int width){
  float weight = 100/width;
  float full_cells_f = load/weight;
  int full_cells = full_cells_f;

  int i = 0;
  while(i<full_cells){
    lcd.write(5);
    i++;
  }
  // escalamos las celdas
  int wt = weight;
  int test = (load-(wt*full_cells))/2;
  if (test == 0){
    test = 6;
  }
  if (load < 100){
    lcd.write(test);
  }
  i++;
  while(i<width){
    lcd.write(6);
    i++;
  }
  
  }
  
  void loop() {
  
   
   
  if (NewData) {    
    //Limpiamos la LCD al empezar
    //Emepzamos a distribuir nuestros datos en el LCD (Nota: esta distribucion es para una lcd 20x4)
    lcd.backlight(); //encendemos la retroiluminacion del LCD
    lcd.clear();
  
 /* lcd.setCursor(0, 0);
  lcd.print(inputString);*/
  
  
    lcd.setCursor(0, 0);
    lcd.write(8);
    lcd.print(CPUT);
   // Serial.println(CPUT);
    lcd.print((char)223);
    lcd.setCursor(5,0);
    ///lcd.print(SpeedMap);
    lcd.print("CPU");
    lcd.setCursor(0, 1);
    lcd.write(8);
    lcd.print(GPUT);
    lcd.print((char)223);
    lcd.setCursor(5,1);
   // lcd.print(GPUFC);
    lcd.print("GPU");
    lcd.setCursor(9,1);
    lcd.write (7);
    graph(Gload,10);
    lcd.setCursor(9, 0);
    lcd.write(7);
    graph(Cload,10);
    lcd.setCursor(0,2);
    lcd.print("RAM USO:");
    lcd.setCursor(9,2);
    lcd.write(7);
    graph(RLoad,10);
    lcd.setCursor(0,3);
   // lcd.print("Mother:");
    //lcd.write(8);
    lcd.print("GRPM:");
    lcd.write(7);
    lcd.setCursor(5,3);
    lcd.print(GPUFC);
    //lcd.print((char)223);
    lcd.setCursor(7,3);
    lcd.print("%");
    lcd.setCursor(12,3);
    lcd.print("HDD:");
    lcd.write(8);
    lcd.print(HDDT);
    lcd.print((char)223);
    /*Calculo de ventilador del CPU, de RPM a %*/
    SpeedMap= map(CFanSpeed,0,3080,0,100);// En este caso el numero 3080 indica las RPM maxima del ventilador.
 

 /*Valor para activar el led del pin13 cuando la temperatura llegue o supere cierto valor*/
    if(Aviso1 >= 50){
      digitalWrite(13,HIGH);
    }                       //Se enciende al llegar  a los 50 o mas grados en este ejemplo
     else{
        digitalWrite(13,LOW);
      }                      //Se apaga si menor a dicha temperatura
      if(Aviso2 >= 50){
      digitalWrite(12,HIGH);
    }                       //Se enciende al llegar  a los 50 o mas grados en este ejemplo
     else{
        digitalWrite(12,LOW);
      }    

/*Borramos el Input*/
    inputString = "";
    NewData = false;
  }
}

int b[8];// b #0
void serialEvent() {
  b[0] = 0;
  while (Serial.available()) {
    // Obtener el nuevo byte:
    char inChar = (char)Serial.read(); 
     Serial.println(inChar);
    // Agregarlo a inputString:
    inputString += inChar;
    
    // Si el carácter entrante coincide habilitamos:
  
    if (inChar == '\n') {

//posicion datos gload
//int g = inputString.indexOf("CPU");
 //GPUFC =g.toInt();
      
      //Establecemos los separadores ","
      b[1] = inputString.indexOf(',');
      b[2] = inputString.indexOf(',', b[1]+1);
      b[3] = inputString.indexOf(',', b[2]+1);
      b[4] = inputString.indexOf(',', b[3]+1);
      b[5] = inputString.indexOf(',', b[4]+1);
      b[6] = inputString.indexOf(',', b[5]+1);
      b[7] = inputString.indexOf(',', b[6]+1);
      b[8] = inputString.indexOf(',', b[7]+1);
      b[9] = inputString.indexOf(',', b[8]+1);
      b[10] = inputString.indexOf(',', b[9]+1);
      //Seleccionamos los strings de entrada para poder imprimirlos en la lcd
      CPUT = inputString.substring(b[0],b[1]);
      
   
      GPUT = inputString.substring(b[3]+1,b[4]);
     // GPUFC = inputString.substring(b[5]+1,b[6]); //RPM
     GPUFC = inputString.substring(b[4]+1,b[5]); //RPM
      
    //  MotherT = inputString.substring(b[7]+1,b[8]);
   //   HDDT = inputString.substring(b[8]+1,b[9]);
        HDDT = inputString.substring(b[6]+1,b[7]);
      
      //Declaramos strings para conversion a "int" 
      String RPM = inputString.substring(b[2]+1,b[3]);
      String loadHold = inputString.substring(b[1]+1,b[2]);
    //  String loadHold2 = inputString.substring(b[4]+1,b[5]);//CARGA GPU
       String loadHold2 = inputString.substring(b[5]+1,b[6]);//CARGA GPU
   //   String RL =inputString.substring(b[5]+1,b[6]);//RAM
    String RL =inputString.substring(b[2]+1,b[3]);//RAM
      
      //Convertimos Los string a "int" para crear avriables para la funcion de la grafica de Load
      Cload = loadHold.toInt();
      Gload = loadHold2.toInt();
      CFanSpeed = RPM.toInt();
     
      RLoad = RL.toInt();
      Aviso1 = CPUT.toInt();
      Aviso2 = CPUT.toInt();
      Serial.println(CPUT.toInt());
      Serial.flush();
     NewData = true;
     
    } 
  }
  
}
