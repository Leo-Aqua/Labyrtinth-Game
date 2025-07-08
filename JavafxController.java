import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

public class JavafxController {

    @FXML
    void imgdrag(MouseEvent event) {
        System.out.println("DELETING SYSTEM");
        System.out.println("In");
        System.out.println("3");
        System.out.println("2");
        System.out.println("1");
        System.exit(0);
    }

    @FXML
    void quitGame(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void startGame(ActionEvent event)throws Exception  {
        System.out.println("Game Starten");

        
        // Am Ende dieser Zeile musst du den korrekten Dateinamen als String angeben:
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
        //loader.setController(new JavafxController());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage =new Stage();
        stage.setTitle("Labyrinth");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

}
