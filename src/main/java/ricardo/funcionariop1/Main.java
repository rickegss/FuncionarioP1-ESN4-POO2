package ricardo.funcionariop1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ricardo.funcionariop1.utils.PathFXML;

import java.io.FileInputStream;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(new FileInputStream(PathFXML.pathBase() + "\\main-view.fxml"));
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Cadastro de Funcionário");
        stage.setScene(scene);
        stage.show();
    }
}