
package ricardo.funcionariop1.utils;

import ricardo.funcionariop1.model.Endereco;
import ricardo.funcionariop1.model.Funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class FuncionarioValidator {

    public void validar(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário não informado.");
        }

        validarMatricula(funcionario.getMatricula());
        validarNome(funcionario.getNome());
        validarCPF(funcionario.getCpf());
        validarNascimento(funcionario.getDataNascimento());
        validarCargo(funcionario.getCargo());
        validarSalario(funcionario.getSalario());
        validarDataContratacao(funcionario.getDataContratacao());
        validarEndereco(funcionario.getEndereco());
    }

    private void validarMatricula(String matricula) {
        if (matricula == null
                || !matricula.matches("\\d{6}")) {
            throw new IllegalArgumentException("A matrícula deve conter exatamente 6 dígitos.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres.");
        }
    }

    private void validarCPF(String CPF) {
        if (CPF == null) {
            throw new IllegalArgumentException("CPF obrigatório.");
        }

        String cpf = CPF.replaceAll("\\D", "");

        if (!cpf.matches("\\d{11}")
                || cpf.chars().distinct().count() == 1) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }

        int primeiroDigito = (soma * 10) % 11;
        if (primeiroDigito == 10) {
            primeiroDigito = 0;
        }

        if (primeiroDigito != cpf.charAt(9) - '0') {
            throw new IllegalArgumentException("CPF inválido.");
        }

        soma = 0;

        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }

        int segundoDigito = (soma * 10) % 11;
        if (segundoDigito == 10) {
            segundoDigito = 0;
        }

        if (segundoDigito != cpf.charAt(10) - '0') {
            throw new IllegalArgumentException("CPF inválido.");
        }
    }

    private void validarNascimento(LocalDate nascimento) {
        if (nascimento == null
                || nascimento.isAfter(LocalDate.now())
                || Period.between(nascimento, LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("O funcionário deve ter pelo menos 18 anos.");
        }
    }

    private void validarCargo(String cargo) {
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("O cargo é obrigatório.");
        }
    }

    private void validarSalario(BigDecimal salario) {
        if (salario == null
                || salario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O salário deve ser maior que zero.");
        }
    }

    private void validarDataContratacao(LocalDate data) {
        if (data == null || data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de contratação é inválida.");
        }
    }

    private void validarEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("O endereço é obrigatório.");
        }

        if (endereco.getCep() == null
                || !endereco.getCep().matches("\\d{5}-?\\d{3}")) {
            throw new IllegalArgumentException("CEP inválido. Use 00000-000 ou 00000000.");
        }
    }
}
