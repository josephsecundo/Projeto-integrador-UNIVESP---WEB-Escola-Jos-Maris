package com.example.escola.repository;

import com.example.escola.model.Alunos;
import com.example.escola.model.Emprestimo;
import com.example.escola.model.Livros;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {
    boolean existsByLivroId(Integer livroId);

    <T> java.util.Optional<T> findByLivroAndAluno(Livros livro, Alunos aluno);

    boolean existsByLivroAndAlunoAndStatus(Livros livro, Alunos aluno, String pendente);

    <T> java.util.Optional<T> findByLivroAndAlunoAndStatus(Livros livro, Alunos aluno, String pendente);
}