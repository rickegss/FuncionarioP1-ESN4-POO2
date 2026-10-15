
package ricardo.funcionariop1.service;

import ricardo.funcionariop1.model.Funcionario;
import ricardo.funcionariop1.repository.FuncionarioRepository;
import ricardo.funcionariop1.utils.FuncionarioValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final List<Funcionario> funcionarios;
    private final FuncionarioValidator validator = new FuncionarioValidator();

    public FuncionarioService() {
        repository = new FuncionarioRepository();

        try {
            funcionarios = repository.carregarTodos();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar os funcionários.", e);
        }
    }

    public void cadastrar(Funcionario funcionario) {
        validator.validar(funcionario);

        boolean matriculaExiste = funcionarios.stream()
                .anyMatch(f -> f.getMatricula()
                        .equals(funcionario.getMatricula()));

        if (matriculaExiste) {
            throw new IllegalArgumentException("Já existe um funcionário com essa matrícula.");
        }

        funcionarios.add(funcionario);
        salvarAlteracoes();
    }

    public Optional<Funcionario> buscarPorMatricula(String matricula) {
        return funcionarios.stream()
                .filter(f -> f.getMatricula().equals(matricula))
                .findFirst();
    }

    public List<Funcionario> listarTodos() {
        return new ArrayList<>(funcionarios);
    }

    public void excluir(String matricula) {
        boolean removido = funcionarios.removeIf(
                f -> f.getMatricula().equals(matricula)
        );

        if (!removido) {
            throw new IllegalArgumentException("Funcionário não encontrado.");
        }

        salvarAlteracoes();
    }

    private void salvarAlteracoes() {
        try {
            repository.salvarTodos(funcionarios);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível salvar os funcionários.", e);
        }
    }
}
