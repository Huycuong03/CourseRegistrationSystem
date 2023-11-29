
package LoginScene;

import AdminScene.AdminRootController;
import FacultyScene.FacultyRootController;
import StudentScene.StudentRootController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import entity.User;

public class LoginController implements Initializable {
    
    @FXML
    private PasswordField passwordField;
    
    public void tryLogin(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sys as sysdba", "huycuong03");
            Statement state = con.createStatement();
            String queryUser = String.format("select * from userx where username = '%s' and pin = '%s'",usernameField.getText(),passwordField.getText());
            ResultSet res = state.executeQuery(queryUser);
            if(res.next()){
                usernameField.getScene().getWindow().hide();               
                openUserScene(new User(res));
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Massage");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect username or password");
                alert.show();
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private TextField usernameField;
    
    public void focusToPasswordField(){
        passwordField.requestFocus();
    }
    
    public void openUserScene(User user){
        try {
            String userRootSourse = "fxml file goes here";
            switch(user.getId().substring(0, 3)){
                case "ADM" -> userRootSourse = "/AdminScene/AdminRoot.fxml";
                case "STU" -> userRootSourse = "/StudentScene/StudentRoot.fxml";
                case "FAC" -> userRootSourse = "/FacultyScene/FacultyRoot.fxml";
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(userRootSourse));
            Parent root = loader.load();
            
            switch(user.getId().substring(0, 3)){
                case "ADM" -> {
                    AdminRootController controller = loader.getController();
                    controller.displayUserNameLabel(user.getName().toUpperCase());
                }
                case "STU" -> {
                    StudentRootController controller = loader.getController();
                    controller.initLater(user);
                }
                case "FAC" -> {
                    FacultyRootController controller = loader.getController();
                    controller.initLater(user);
                }
            }
            
            Stage stage = new Stage();
            stage.setTitle("Course Registration System");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();                       
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
