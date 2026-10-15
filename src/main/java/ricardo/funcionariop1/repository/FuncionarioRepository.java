
package ricardo.funcionariop1.repository;

import ricardo.funcionariop1.model.Endereco;
import ricardo.funcionariop1.model.Funcionario;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {

    private final Path caminhoArquivo;

    public FuncionarioRepository() {
        this.caminhoArquivo = Path.of("funcionarios.csv");
    }

    public List<Funcionario> carregarTodos() throws IOException {
        List<Funcionario> funcionarios = new ArrayList<>();

        if (Files.notExists(caminhoArquivo)) {
            return funcionarios;
        }

        List<String> linhas = Files.readAllLines(caminhoArquivo, StandardCharsets.UTF_8);

        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i);

            if (linha.isBlank()) {
                continue;
            }

            if (i == 0 && linha.startsWith("matricula;")) {
                continue;
            }

            List<String> campos = separarCampos(linha);

            if (campos.size() != 14) {
                throw new IOException(
                    "Linha " + (i + 1) + " do CSV possui "
                            + campos.size() + " campos; eram esperados 14."
                );
            }

            Endereco endereco = new Endereco(
                    campos.get(7),
                    campos.get(8),
                    campos.get(9),
                    campos.get(10),
                    campos.get(11),
                    campos.get(12),
                    campos.get(13)
            );

            Funcionario funcionario = new Funcionario(
                    campos.get(0),
                    campos.get(1),
                    campos.get(2),
                    LocalDate.parse(campos.get(3)),
                    campos.get(4),
                    new BigDecimal(campos.get(5)),
                    LocalDate.parse(campos.get(6)),
                    endereco
            );

            funcionarios.add(funcionario);
        }

        return funcionarios;
    }

    public void salvarTodos(List<Funcionario> funcionarios)
            throws IOException {

        List<String> linhas = new ArrayList<>();

        linhas.add(
                "matricula;nome;CPF;dataNascimento;cargo;salario;"
                        + "dataContratacao;logradouro;numero;complemento;"
                        + "bairro;cidade;estado;CEP"
        );

        for (Funcionario f : funcionarios) {
            Endereco e = f.getEndereco();

            List<String> campos = List.of(
                    f.getMatricula(),
                    f.getNome(),
                    f.getCpf(),
                    f.getDataNascimento().toString(),
                    f.getCargo(),
                    f.getSalario().toPlainString(),
                    f.getDataContratacao().toString(),
                    e.getLogradouro(),
                    e.getNumero(),
                    e.getComplemento(),
                    e.getBairro(),
                    e.getCidade(),
                    e.getEstado(),
                    e.getCep()
            );

            linhas.add(
                    campos.stream()
                            .map(this::escaparCampo)
                            .reduce((a, b) -> a + ";" + b)
                            .orElse("")
            );
        }

        Files.write(
                caminhoArquivo,
                linhas,
                StandardCharsets.UTF_8
        );
    }

    private String escaparCampo(String campo) {
        if (campo == null) {
            return "";
        }

        if (campo.contains(";") || campo.contains("\"")) {
            return "\"" + campo.replace("\"", "\"\"") + "\"";
        }

        return campo;
    }

    private List<String> separarCampos(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char caractere = linha.charAt(i);

            if (caractere == '"') {
                if (entreAspas
                        && i + 1 < linha.length()
                        && linha.charAt(i + 1) == '"') {
                    campo.append('"');
                    i++;
                } else {
                    entreAspas = !entreAspas;
                }
            } else if (caractere == ';' && !entreAspas) {
                campos.add(campo.toString());
                campo.setLength(0);
            } else {
                campo.append(caractere);
            }
        }

        campos.add(campo.toString());

        return campos;
    }
}
