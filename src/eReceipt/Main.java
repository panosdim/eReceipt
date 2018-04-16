package eReceipt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MainUI.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Edit .idea/artifacts/eReceipt.xml in order to set icon for native bundle.
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/eReceipt.png")));
        stage.setTitle("eReceipt");
        stage.setResizable(false);
        stage.show();
    }

}
