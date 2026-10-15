
package ricardo.funcionariop1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import ricardo.funcionariop1.model.Endereco;
import ricardo.funcionariop1.model.Funcionario;
import ricardo.funcionariop1.service.FuncionarioService;

import java.math.BigDecimal;

public class CadastroController {

    @FXML private TextField txtMatricula;
    @FXML private TextField txtCPF;
    @FXML private TextField txtCargo;
    @FXML private DatePicker dpContratacao;
    @FXML private TextField txtNome;
    @FXML private DatePicker dpNascimento;
    @FXML private TextField txtSalario;
    @FXML private TextField txtLogradouro;
    @FXML private TextField txtComplemento;
    @FXML private TextField txtCidade;
    @FXML private TextField txtCEP;
    @FXML private TextField txtNumero;
    @FXML private TextField txtBairro;
    @FXML private ComboBox<String> cbEstado;

    private final FuncionarioService funcionarioService =
            new FuncionarioService();

    @FXML
    public void initialize() {
        cbEstado.getItems().setAll(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF",
                "ES", "GO", "MA", "MT", "MS", "MG", "PA",
                "PB", "PR", "PE", "PI", "RJ", "RN", "RS",
                "RO", "RR", "SC", "SP", "SE", "TO"
        );
    }

    @FXML
    private void cadastrar() {
        try {
            Endereco endereco = new Endereco(
                    txtLogradouro.getText().trim(),
                    txtNumero.getText().trim(),
                    txtComplemento.getText().trim(),
                    txtBairro.getText().trim(),
                    txtCidade.getText().trim(),
                    cbEstado.getValue(),
                    txtCEP.getText().trim()
            );

            BigDecimal salario = new BigDecimal(
                    txtSalario.getText()
                            .trim()
                            .replace(".", "")
                            .replace(",", ".")
            );

            Funcionario funcionario = new Funcionario(
                    txtMatricula.getText().trim(),
                    txtNome.getText().trim(),
                    txtCPF.getText().trim(),
                    dpNascimento.getValue(),
                    txtCargo.getText().trim(),
                    salario,
                    dpContratacao.getValue(),
                    endereco
            );

            funcionarioService.cadastrar(funcionario);

            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Sucesso",
                    "Funcionário cadastrado com sucesso!"
            );

            limpar();

        } catch (NumberFormatException e) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Salário inválido",
                    "Informe um salário numérico válido."
            );

        } catch (IllegalArgumentException e) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Dados inválidos",
                    e.getMessage()
            );

        } catch (RuntimeException e) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro",
                    "Não foi possível concluir o cadastro. "
                            + "Verifique os dados e o arquivo CSV."
            );
        }
    }

    @FXML
    private void limpar() {
        txtMatricula.clear();
        txtCPF.clear();
        txtCargo.clear();
        dpContratacao.setValue(null);
        txtNome.clear();
        dpNascimento.setValue(null);
        txtSalario.clear();
        txtLogradouro.clear();
        txtComplemento.clear();
        txtCidade.clear();
        txtCEP.clear();
        txtNumero.clear();
        txtBairro.clear();
        cbEstado.setValue(null);
    }

    private void exibirAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensagem
    ) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
