/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;




import java.awt.Toolkit;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

  
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
            Scene scene = new Scene(root);
            //agrego icono al formulario  
            Image image = new Image("resources/jmxoft3.jpg");
            primaryStage.getIcons().add(image);
            
            primaryStage.setScene(scene);
            primaryStage.show();
         //   primaryStage.setTitle("MONITOR DE TEMPERATURA");
      
          //  primaryStage.getIcons.add(new Image("/resources/jmxoft.ico"));
            
            
            
            
            
 //evento cierre de anchor pane
         primaryStage.setOnCloseRequest( event ->
    {
        System.out.println("CLOSING");
          primaryStage.close();
        System.exit(0);
    });
     
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    /*  public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("resources/jmxoft.ico"));
        return retValue;
    } */

    private void add(Image image) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}