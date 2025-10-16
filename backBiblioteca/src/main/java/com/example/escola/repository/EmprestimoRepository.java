package com.example.escola.repository;

import com.example.escola.dto.LivrosMaisEmprestadosDTO;
import com.example.escola.model.Alunos;
import com.example.escola.model.Emprestimo;
import com.example.escola.model.Livros;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {

    @Query("SELECT new com.example.escola.dto.LivrosMaisEmprestadosDTO(l.titulolivro, COUNT(e)) " +
           "FROM Emprestimo e JOIN e.livro l " +
           "GROUP BY l.id, l.titulolivro " +
           "ORDER BY COUNT(e) DESC")
    List<LivrosMaisEmprestadosDTO> findTopLivrosMaisEmprestados(Pageable pageable);

    Optional<Emprestimo> findByLivroAndAlunoAndStatus(Livros livro, Alunos aluno, String status);

    boolean existsByLivroAndAlunoAndStatus(Livros livro, Alunos aluno, String pendente);
}