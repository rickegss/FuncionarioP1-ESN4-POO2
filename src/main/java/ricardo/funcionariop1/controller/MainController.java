package ricardo.funcionariop1.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import ricardo.funcionariop1.utils.PathFXML;

public class MainController {
    @FXML   
    private StackPane areaConteudo;
    
    @FXML 
    private Button btnCadastro;
    
    @FXML 
    private Button btnListagem;

    @FXML 
    private Button btnRelatorios;

    @FXML
    private void initialize(){
        abrirCadastro();
    }

    @FXML 
    private void abrirCadastro(){
        carregarView("cadastro-view.fxml");
    }

    @FXML
    private void abrirListagem() {
        carregarView("listagem-view.fxml");
    }

    @FXML
    private void abrirRelatorios() {
        carregarView("relatorio-view.fxml");
    }

    private void carregarView(String nomeArquivo){
        try{
            String caminho = PathFXML.pathBase() + File.separator + nomeArquivo;
            FXMLLoader loader = new FXMLLoader();
            StackPane.setAlignment(areaConteudo, javafx.geometry.Pos.TOP_LEFT);

            javafx.scene.Parent view = loader.load(new FileInputStream(caminho));
            areaConteudo.getChildren().setAll(view);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
