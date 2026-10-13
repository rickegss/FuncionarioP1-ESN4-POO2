package ricardo.funcionariop1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class CadastroController {

    @FXML
    private TextField txtMatricula;

    @FXML
    private TextField txtCPF;

    @FXML
    private TextField txtCargo;

    @FXML
    private DatePicker dpContratacao;

    @FXML
    private TextField txtNome;

    @FXML
    private DatePicker dpNascimento;

    @FXML
    private TextField txtSalario;

    @FXML
    private TextField txtLogradouro;

    @FXML
    private TextField txtComplemento;

    @FXML
    private TextField txtCidade;

    @FXML
    private TextField txtCEP;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtBairro;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnLimpar;

}
