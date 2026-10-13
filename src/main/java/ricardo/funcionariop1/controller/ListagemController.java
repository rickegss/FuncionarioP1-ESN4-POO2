package ricardo.funcionariop1.controller;

import ricardo.funcionariop1.model.Funcionario;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class ListagemController {

    @FXML
    private TextField txtBuscaMatricula;

    @FXML
    private Button btnBuscar;

    @FXML
    private TableView<Funcionario> tabelaFuncionarios;

    @FXML
    private TableColumn<Funcionario, String> colMatricula;

    @FXML
    private TableColumn<Funcionario, String> colNome;

    @FXML
    private TableColumn<Funcionario, String> colCargo;

    @FXML
    private TableColumn<Funcionario, BigDecimal> colSalario;

    @FXML
    private Button btnExcluir;
}