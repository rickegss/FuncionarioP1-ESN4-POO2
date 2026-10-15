
package ricardo.funcionariop1.controller;

import ricardo.funcionariop1.model.Funcionario;
import ricardo.funcionariop1.service.FuncionarioService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ListagemController {

    @FXML private TextField txtBuscaMatricula;
    @FXML private Button btnBuscar;

    @FXML private TableView<Funcionario> tabelaFuncionarios;

    @FXML private TableColumn<Funcionario, String> colMatricula;
    @FXML private TableColumn<Funcionario, String> colNome;
    @FXML private TableColumn<Funcionario, String> colCargo;
    @FXML private TableColumn<Funcionario, BigDecimal> colSalario;

    @FXML private Button btnExcluir;

    private final FuncionarioService funcionarioService =
            new FuncionarioService();

    @FXML
    public void initialize() {
        colMatricula.setCellValueFactory(
                new PropertyValueFactory<>("matricula")
        );

        colNome.setCellValueFactory(
                new PropertyValueFactory<>("nome")
        );

        colCargo.setCellValueFactory(
                new PropertyValueFactory<>("cargo")
        );

        colSalario.setCellValueFactory(
                new PropertyValueFactory<>("salario")
        );

        colSalario.setCellFactory(coluna ->
                new javafx.scene.control.TableCell<>() {
                    @Override
                    protected void updateItem(
                            BigDecimal salario, boolean vazio) {
                        super.updateItem(salario, vazio);

                        if (vazio || salario == null) {
                            setText(null);
                        } else {
                            setText(NumberFormat.getCurrencyInstance(
                                    Locale.forLanguageTag("pt-BR")
                            ).format(salario));
                        }
                    }
                }
        );

        btnBuscar.setOnAction(event -> buscar());
        btnExcluir.setOnAction(event -> excluirSelecionado());

        atualizarTabela(funcionarioService.listarTodos());
    }

    private void atualizarTabela(List<Funcionario> funcionarios) {
        tabelaFuncionarios.setItems(
                FXCollections.observableArrayList(funcionarios)
        );
    }

    @FXML
    private void buscar() {
        String matricula = txtBuscaMatricula.getText().trim();

        if (matricula.isEmpty()) {
            atualizarTabela(funcionarioService.listarTodos());
            return;
        }

        Optional<Funcionario> resultado =
                funcionarioService.buscarPorMatricula(matricula);

        if (resultado.isPresent()) {
            atualizarTabela(List.of(resultado.get()));
        } else {
            atualizarTabela(List.of());

            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Busca",
                    "Nenhum funcionário encontrado para essa matrícula."
            );
        }
    }

    @FXML
    private void excluirSelecionado() {
        Funcionario selecionado =
                tabelaFuncionarios.getSelectionModel()
                        .getSelectedItem();

        if (selecionado == null) {
            exibirAlerta(
                    Alert.AlertType.WARNING,
                    "Nenhuma seleção",
                    "Selecione um funcionário na tabela para excluir."
            );
            return;
        }

        Alert confirmacao = new Alert(
                Alert.AlertType.CONFIRMATION
        );
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Excluir funcionário?");
        confirmacao.setContentText(
                "Deseja excluir " + selecionado.getNome()
                        + " (matrícula "
                        + selecionado.getMatricula() + ")?"
        );

        Optional<javafx.scene.control.ButtonType> resposta =
                confirmacao.showAndWait();

        if (resposta.isPresent()
                && resposta.get()
                == javafx.scene.control.ButtonType.OK) {
            try {
                funcionarioService.excluir(
                        selecionado.getMatricula()
                );

                atualizarTabela(
                        funcionarioService.listarTodos()
                );

                txtBuscaMatricula.clear();

                exibirAlerta(
                        Alert.AlertType.INFORMATION,
                        "Sucesso",
                        "Funcionário excluído com sucesso."
                );

            } catch (RuntimeException e) {
                exibirAlerta(
                        Alert.AlertType.ERROR,
                        "Erro",
                        "Não foi possível excluir o funcionário."
                );
            }
        }
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
