package ricardo.funcionariop1.controller;

import ricardo.funcionariop1.model.Funcionario;
import ricardo.funcionariop1.model.Endereco;
import ricardo.funcionariop1.service.FuncionarioService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatoriosController {

    @FXML
    private ComboBox<String> cbTipoRelatorio;

    @FXML
    private VBox areaFiltros;

    @FXML
    private Button btnGerarRelatorio;

    @FXML
    private Label lblResultado;

    @FXML
    private TextArea txtResultado;

    private final FuncionarioService funcionarioService =
            new FuncionarioService();

    private final NumberFormat formatoMoeda =
            NumberFormat.getCurrencyInstance(
                    Locale.forLanguageTag("pt-BR")
            );

    private TextField txtCargo;
    private TextField txtSalarioMinimo;
    private TextField txtSalarioMaximo;

    @FXML
    public void initialize() {
        cbTipoRelatorio.setItems(
                FXCollections.observableArrayList(
                        "Funcionários por cargo",
                        "Funcionários por faixa salarial",
                        "Média salarial por cargo",
                        "Funcionários por cidade/estado"
                )
        );

        cbTipoRelatorio.valueProperty().addListener(
                (observable, anterior, selecionado) ->
                        configurarFiltros(selecionado)
        );

        btnGerarRelatorio.setOnAction(
                event -> gerarRelatorio()
        );

        txtResultado.setEditable(false);
        lblResultado.setText("Selecione um relatório.");
    }

    private void configurarFiltros(String tipo) {
        areaFiltros.getChildren().clear();

        txtCargo = null;
        txtSalarioMinimo = null;
        txtSalarioMaximo = null;

        if (tipo == null) {
            return;
        }

        switch (tipo) {
            case "Funcionários por cargo" -> {
                txtCargo = criarCampo("Ex.: Analista de TI");
                adicionarFiltro("Cargo:", txtCargo);
            }

            case "Funcionários por faixa salarial" -> {
                txtSalarioMinimo = criarCampo("Ex.: 2000,00");
                txtSalarioMaximo = criarCampo("Ex.: 5000,00");

                adicionarFiltro("Salário mínimo:", txtSalarioMinimo);
                adicionarFiltro("Salário máximo:", txtSalarioMaximo);
            }

            case "Média salarial por cargo",
                 "Funcionários por cidade/estado" -> {
                Label informacao = new Label(
                        "Este relatório não exige filtros."
                );
                areaFiltros.getChildren().add(informacao);
            }
        }

        txtResultado.clear();
        lblResultado.setText("Pronto para gerar o relatório.");
    }

    private TextField criarCampo(String placeholder) {
        TextField campo = new TextField();
        campo.setPromptText(placeholder);
        campo.setMaxWidth(250);
        return campo;
    }

    private void adicionarFiltro(String titulo, TextField campo) {
        HBox linha = new HBox(10);
        Label label = new Label(titulo);
        label.setMinWidth(130);

        linha.getChildren().addAll(label, campo);
        areaFiltros.getChildren().add(linha);
    }

    private void gerarRelatorio() {
        String tipo = cbTipoRelatorio.getValue();

        if (tipo == null) {
            mostrarErro("Selecione um tipo de relatório.");
            return;
        }

        try {
            String resultado = switch (tipo) {
                case "Funcionários por cargo" ->
                        gerarPorCargo();

                case "Funcionários por faixa salarial" ->
                        gerarPorFaixaSalarial();

                case "Média salarial por cargo" ->
                        gerarMediaPorCargo();

                case "Funcionários por cidade/estado" ->
                        gerarPorLocalizacao();

                default -> throw new IllegalArgumentException(
                        "Tipo de relatório inválido."
                );
            };

            txtResultado.setText(resultado);
            lblResultado.setText("Relatório gerado com sucesso.");

        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        }
    }

    private String gerarPorCargo() {
        String cargo = txtCargo.getText().trim();

        if (cargo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Informe o cargo desejado."
            );
        }

        var funcionarios = funcionarioService.listarTodos()
                .stream()
                .filter(f -> f.getCargo() != null)
                .filter(f -> f.getCargo().equalsIgnoreCase(cargo))
                .sorted(Comparator.comparing(Funcionario::getNome))
                .toList();

        if (funcionarios.isEmpty()) {
            return "Nenhum funcionário encontrado para o cargo: "
                    + cargo;
        }

        return "RELATÓRIO DE FUNCIONÁRIOS POR CARGO\n\n"
                + "Cargo: " + cargo + "\n"
                + "Total: " + funcionarios.size() + "\n\n"
                + formatarFuncionarios(funcionarios);
    }

    private String gerarPorFaixaSalarial() {
        BigDecimal minimo = converterSalario(
                txtSalarioMinimo.getText()
        );

        BigDecimal maximo = converterSalario(
                txtSalarioMaximo.getText()
        );

        if (minimo.signum() < 0 || maximo.signum() < 0) {
            throw new IllegalArgumentException(
                    "Os salários não podem ser negativos."
            );
        }

        if (minimo.compareTo(maximo) > 0) {
            throw new IllegalArgumentException(
                    "O salário mínimo não pode superar o máximo."
            );
        }

        var funcionarios = funcionarioService.listarTodos()
                .stream()
                .filter(f -> f.getSalario() != null)
                .filter(f -> f.getSalario().compareTo(minimo) >= 0)
                .filter(f -> f.getSalario().compareTo(maximo) <= 0)
                .sorted(Comparator.comparing(Funcionario::getSalario))
                .toList();

        if (funcionarios.isEmpty()) {
            return "Nenhum funcionário encontrado nessa faixa salarial.";
        }

        return "RELATÓRIO POR FAIXA SALARIAL\n\n"
                + "Salário mínimo: " + formatoMoeda.format(minimo) + "\n"
                + "Salário máximo: " + formatoMoeda.format(maximo) + "\n"
                + "Total: " + funcionarios.size() + "\n\n"
                + formatarFuncionarios(funcionarios);
    }

    private String gerarMediaPorCargo() {
        Map<String, Double> medias = funcionarioService.listarTodos()
                .stream()
                .filter(f -> f.getCargo() != null)
                .filter(f -> !f.getCargo().isBlank())
                .filter(f -> f.getSalario() != null)
                .collect(Collectors.groupingBy(
                        Funcionario::getCargo,
                        Collectors.averagingDouble(
                                f -> f.getSalario().doubleValue()
                        )
                ));

        if (medias.isEmpty()) {
            return "Não há dados suficientes para calcular as médias.";
        }

        StringBuilder resultado = new StringBuilder(
                "MÉDIA SALARIAL POR CARGO\n\n"
        );

        medias.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(
                        String.CASE_INSENSITIVE_ORDER
                ))
                .forEach(entrada -> resultado
                        .append(entrada.getKey())
                        .append(": ")
                        .append(formatoMoeda.format(entrada.getValue()))
                        .append("\n"));

        return resultado.toString();
    }

    private String gerarPorLocalizacao() {
        Map<String, java.util.List<Funcionario>> grupos =
                funcionarioService.listarTodos()
                        .stream()
                        .filter(f -> f.getEndereco() != null)
                        .collect(Collectors.groupingBy(
                                f -> {
                                    Endereco e = f.getEndereco();

                                    String cidade = e.getCidade() == null
                                            ? "Não informada"
                                            : e.getCidade();

                                    String estado = e.getEstado() == null
                                            ? "Não informado"
                                            : e.getEstado();

                                    return cidade + "/" + estado;
                                }
                        ));

        if (grupos.isEmpty()) {
            return "Nenhum funcionário com endereço cadastrado.";
        }

        StringBuilder resultado = new StringBuilder(
                "FUNCIONÁRIOS POR CIDADE/ESTADO\n\n"
        );

        grupos.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(
                        String.CASE_INSENSITIVE_ORDER
                ))
                .forEach(entrada -> {
                    resultado.append(entrada.getKey())
                            .append(" (")
                            .append(entrada.getValue().size())
                            .append(" funcionário(s))\n");

                    entrada.getValue().stream()
                            .sorted(Comparator.comparing(
                                    Funcionario::getNome,
                                    Comparator.nullsLast(
                                            String.CASE_INSENSITIVE_ORDER
                                    )
                            ))
                            .forEach(f -> resultado
                                    .append("  • ")
                                    .append(f.getNome())
                                    .append(" | Matrícula: ")
                                    .append(f.getMatricula())
                                    .append("\n"));

                    resultado.append("\n");
                });

        return resultado.toString();
    }

    private String formatarFuncionarios(
            java.util.List<Funcionario> funcionarios
    ) {
        StringBuilder resultado = new StringBuilder();

        for (Funcionario f : funcionarios) {
            resultado.append("Matrícula: ")
                    .append(f.getMatricula())
                    .append("\nNome: ")
                    .append(f.getNome())
                    .append("\nCargo: ")
                    .append(f.getCargo())
                    .append("\nSalário: ")
                    .append(formatoMoeda.format(f.getSalario()))
                    .append("\n\n");
        }

        return resultado.toString();
    }

    private BigDecimal converterSalario(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "Preencha os dois valores salariais."
            );
        }

        try {
            String normalizado = valor.trim();

            if (normalizado.contains(",")) {
                normalizado = normalizado
                        .replace(".", "")
                        .replace(",", ".");
            }

            return new BigDecimal(normalizado);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Informe salários válidos, como 2500,00."
            );
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro no relatório");
        alerta.setHeaderText("Não foi possível gerar o relatório");
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
