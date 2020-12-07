/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author educacionit
 */
public class MyThread extends Thread {

     @FXML
    private Label label3;
    
 private boolean alive=true;
 public boolean bandera=true;
 
 
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
  
   
    @Override
    public void run() {
   
         //sobreescribimos metodo Run al hacer llamada en maion con start
         //  for (int i = 0; i< 1000 ;i++) {
         //          System.out.println("Thread 0"+i);
         //    }
  //       String text = label3.getText();
    //    label3.setText("HOLA");
               SimpleDateFormat sdf= new SimpleDateFormat("HH:MM:ss");      
    while(alive){
        System.out.println(sdf.format(new Date())); 
        //label3.setText(String.valueOf(sdf.format(new Date())));
//otro ejemplo    
      
               try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    
        
    
    }
   
    
       
    
    
  
    
    
}
