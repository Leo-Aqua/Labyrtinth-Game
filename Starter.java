import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;


public class Starter extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Programmstart....");
        // Am Ende dieser Zeile musst du den korrekten Dateinamen als String angeben:
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        //loader.setController(new JavafxController());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage.setTitle("FXML Willkommen");
        stage.setScene(scene);
        stage.show();
    }
}
