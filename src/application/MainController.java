/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.components.Disk;
import com.profesorfalken.jsensors.model.components.Gpu;
import com.profesorfalken.jsensors.model.sensors.Fan;
import com.profesorfalken.jsensors.model.sensors.Load;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import static java.awt.Color.RED;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import static java.lang.System.exit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.ImageIcon;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class MainController {

    @FXML
    private TextArea console;
    private PrintStream ps ;
    @FXML
    private Label label;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
 
    
    @FXML
    private Button b1;
    
// Aquí se pone en marcha el timer cada segundo.
   Timer timer = new Timer();
    //protocolo de timer como parametro a pasar
    TimerTask timerTask ; 
 
  public boolean bandera=true;
  public boolean detener=true;
  
 public boolean comIno=true;
  
  
  
  
  
 //Constructor de la clase y metodo run para timer    
//public MainController() {
   private PanamaHitek_Arduino ino = new PanamaHitek_Arduino();
  public void button(ActionEvent event) throws InterruptedException, ArduinoException {
  // MyThread t = new MyThread();
   //t.start();
    
      String valorb1 = b1.getText();
     
if (valorb1.equals("INICIAR")){
    b1.setText("DETENER");
    b1.setStyle("-fx-background-color: CYAN");
    detener=false;
    //this.timerTask.run();
  
       
     console.setText(" ");
      
        Image imageCpuAmd = new Image("resources/amd.jpg");
        Image imageCpuIntel = new Image("resources/intel.jpg");
        Image imageGpuAmd = new Image("resources/ati_rad.jpg");
        Image imageGpuIntel = new Image("resources/intel_video.jpg");
        Image imageGpuNvidia = new Image("resources/nvidia.jpg");
        
       //lista de datos para enviar por serie
       List dato = new ArrayList();    
       //lista de datos con coma
       List dato2 = new ArrayList();    
       //lista a ordenar datos
       List dato3 = new ArrayList();
       
       Components components2 = JSensors.get.components();    
        bandera = false;
 


if (bandera==false){
//Aqui pinemos lo q qremos q haga
   
     List<Cpu> cpus = components2.cpus;
if (cpus != null) {
     
    for (final Cpu cpu : cpus) {
        //System.out.println("Found CPU component: " + cpu.name);  
        if (cpu.sensors != null) {
          if (cpu.name.contains("Amd")||cpu.name.contains("amd".toLowerCase()) ||cpu.name.contains("amd".toUpperCase())){
           //label.setText(cpu.name);   
           label.setGraphic(new ImageView(imageCpuAmd));
            bandera = false;
          }else if(cpu.name.contains("Intel")||cpu.name.contains("intel".toLowerCase()) ||cpu.name.contains("intel".toUpperCase())){
             //label.setText("Intel");  
             label.setGraphic(new ImageView(imageCpuIntel));
             bandera = false;
          }
            
            
        } 
        
    } 
    
}
List<Gpu> gpus = components2.gpus;
if (gpus != null) {
    for (final Gpu gpu : gpus) {
  //System.out.println("Found CPU component: " + cpu.name);  
        if (gpu.sensors != null) {
          if (gpu.name.contains("Intel")||gpu.name.contains("intel".toLowerCase()) ||gpu.name.contains("intel".toUpperCase())||gpu.name.contains("HD")){
           label2.setGraphic(new ImageView(imageGpuIntel));    
          }else if(gpu.name.contains("Ati")||gpu.name.contains("ati".toLowerCase()) ||gpu.name.contains("ati".toUpperCase())||gpu.name.contains("Radeon")){
             label2.setGraphic(new ImageView(imageGpuAmd));  
          }else if(gpu.name.contains("Nvidia")||gpu.name.contains("nvidia".toLowerCase()) ||gpu.name.contains("nvidia".toUpperCase())){
             label2.setGraphic(new ImageView(imageGpuNvidia));
          }else  if (gpu.name.contains("Amd")||gpu.name.contains("amd".toLowerCase()) ||gpu.name.contains("amd".toUpperCase())){
          label2.setGraphic(new ImageView(imageGpuAmd));
            
          } 
        } 
    } 
    
}

}

if(comIno==true){
//Inicia comunicacion con arduino    
  comIno=false;
    try {
          
   
//Se inicia la comunicación con el Puerto Serie
            ino.arduinoTX("COM9", 9600);
        
      } catch (ArduinoException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
}
    
this.timerTask = new TimerTask() {       
  
@Override
public void run() {
     Components components = JSensors.get.components();         
       
    console.setText(" ");
    System.setOut(ps);
    System.setErr(ps);
System.out.println("---------------------------------------------------");
System.out.println("DATOS CPU");
System.out.println("---------------------------------------------------");
double cpu1=0;
int cont =0;
List<Cpu> cpus = components.cpus;
if (cpus != null) {
     
    for (final Cpu cpu : cpus) {
        System.out.println("Found CPU component: " + cpu.name);  
        if (cpu.sensors != null) {
            System.out.println("Sensors: ");
            
            //Print temperatures
            List<Temperature> temps = cpu.sensors.temperatures;
            for (final Temperature temp : temps) {
                System.out.println(temp.name + ": " + temp.value + " C");
          
           //    cpu1=temp.value;
            //    label3.setText(String.valueOf(cpu1));
                dato.add(temp.name);
                dato.add(temp.value);
                //dato2.add(",");
                  dato2.add(temp.value.toString().substring(0,2));
                // dato2.add(",");
                
            }
           
            //Print fan speed
            List<Fan> fans = cpu.sensors.fans;
            for (final Fan fan : fans) {
                System.out.println(fan.name + ": " + fan.value + " RPM");
                dato.add(fan.name);
                dato.add(fan.value);
                /*    ta.appendText("\n");
               ta.appendText(fan.name);
               ta.appendText(": ");
               ta.appendText(fan.value.toString()); 
               ta.appendText(" RPM");
               ta.appendText("\n"); */
                  dato2.add(fan.value.toString().substring(0,2));
                 //dato2.add(",");
            }
            
            List<Load> loads = cpu.sensors.loads;
            for (final Load load : loads) {
                System.out.println(load.name + ":" + load.value + " ");
                 cont++; 
                dato.add(load.name);
                dato.add(load.value);
                 if (cont==12 || cont==13){
                dato2.add(load.value.toString().substring(0,3));
                //dato2.add(",");
               
                } if (cont==14){
                dato2.add(load.value.toString().substring(0,2));
                //dato2.add(",");
               
                }
                 
            }
            
        }
        
    }
    
}
 cont=0;
System.out.println("---------------------------------------------------");
System.out.println("DATOS GPU");
System.out.println("---------------------------------------------------");
List<Gpu> gpus = components.gpus;
if (gpus != null) {
    for (final Gpu gpu : gpus) {
        System.out.println("Found GPU component: " + gpu.name);
        if (gpu.sensors != null) {
            System.out.println("Sensors: ");
        /*     ta.appendText(gpu.name);
              ta.appendText("\n");*/
            //Print temperatures
            List<Temperature> temps =gpu.sensors.temperatures;
            for (final Temperature temp : temps) {
                System.out.println(temp.name + ": " + temp.value + " C");
                dato.add(temp.name);
                dato.add(temp.value);
                dato2.add(temp.value.toString().substring(0,2));
                 //dato2.add(",");
                //Print fan speed
                List<Fan> fans = gpu.sensors.fans;
                for (final Fan fan : fans) {
                    System.out.println(fan.name + ": " + fan.value + " RPM");
              dato.add(fan.name);
               dato.add(fan.value);
               dato2.add(fan.value.toString().substring(0,2));
                 //dato2.add(",");
                }  
                List<Load> loads = gpu.sensors.loads;
                for (final Load load : loads) {
                    System.out.println(load.name + ":LOADS: " + load.value + " ");
              dato.add(load.name);
                dato.add(load.value);
                dato2.add(load.value.toString().substring(0,3));
                // dato2.add(",");
               
                }
            }
        }
    }
}  
System.out.println("---------------------------------------------------");
System.out.println("DATOS DISCOS");
System.out.println("---------------------------------------------------");
List<Disk> disks = components.disks;
if (disks != null) {
    for (final Disk disk : disks) {
        System.out.println("Found DISKS component: " + disk.name);
        if (disk.sensors != null) {
            System.out.println("Sensors: ");
             // ta.appendText(disk.name);
            //Print temperatures
            List<Temperature> temps =disk.sensors.temperatures;
            for (final Temperature temp : temps) {
                System.out.println(temp.name + ": " + temp.value + " C");
             dato.add(temp.name);
                 dato.add(temp.value);
                 dato2.add(temp.value.toString().substring(0,2));
                // dato2.add(",");
                //Print fan speed
                List<Fan> fans = disk.sensors.fans;
                for (final Fan fan : fans) {
                    System.out.println(fan.name + ": " + fan.value + " RPM");
             dato.add(fan.name);
               dato.add(fan.value);
               dato2.add(fan.value.toString().substring(0,2));
                // dato2.add(",");
                } 
                List<Load> loads = disk.sensors.loads;
                for (final Load load : loads) {
                    System.out.println(load.name + ":LOADS: " + load.value + " ");
                  dato.add(load.name);
               dato.add(load.value); 
               dato2.add(load.value.toString().substring(0,2));
                 //dato2.add(",");
                }
            }
        }
    }
     
   }
dato.add("\n");
dato2.add("\n");
/*
// Iterator to traverse the list 
        Iterator iterator = dato2.iterator(); 
        System.out.println("List elements : "); 
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + ""); 
            
            
        System.out.println(); 
        }*/
//System.out.println("CANT ELEMENTOS: "+dato2.size());
// ordenamiento de datos
String result = "";   //<== needs to be outside the loop
for (int i = 0; i < dato2.size()-1; i++) {
    
String word = (String) dato2.get(i);
result = result + word + ",";  // <== need to append
}
//hago cieere de fila
result+="\n";
    
System.out.print("result:"+result);



        try {
          
           // ino.sendData(dato2.toString());
            ino.sendData(result);
          result="";
           dato2.clear();
           System.out.flush();
           
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
             dato2.clear();
        }
        
            }
       
          }; 
 
    // Dentro de 0 milisegundos avísame cada 3000 milisegundos
     timer.scheduleAtFixedRate(timerTask, 0, 3000);
  }else{
   b1.setText("INICIAR");
   b1.setStyle("-fx-background-color: WHITE");
   detener = true;
   console.setText(" ");
   
   this.timerTask.cancel();

}
    
    
    

  }
   
    
    
    public void initialize() {
        ps = new PrintStream(new Console(console)) ;
        
    }

   
    
    
    


    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }
    
      
  
    
    
    
}