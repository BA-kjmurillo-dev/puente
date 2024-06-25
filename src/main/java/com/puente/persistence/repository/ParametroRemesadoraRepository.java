package com.puente.persistence.repository;

import com.puente.persistence.entity.ParametroRemesadoraEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
public interface ParametroRemesadoraRepository extends ListCrudRepository<ParametroRemesadoraEntity, String> {

    ParametroRemesadoraEntity findByMrecod(String mrecod);
}
