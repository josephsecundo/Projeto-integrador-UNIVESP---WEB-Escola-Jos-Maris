package com.example.escola.escola_web_site.repository;

import com.example.escola.escola_web_site.model.RecadosModel;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecadosRepository extends CrudRepository <RecadosModel, Integer> {
    List<RecadosModel> findByAtivoTrue();
    List<RecadosModel> findByAtivoFalse();

    List<RecadosModel> findByDataValidadeBeforeAndAtivoTrue(LocalDate dataValidade);

}
