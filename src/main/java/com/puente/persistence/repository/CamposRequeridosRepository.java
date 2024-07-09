package com.puente.persistence.repository;

import com.puente.persistence.entity.CamposRequeridosEntity;
import com.puente.persistence.entity.CamposRequeridosId;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CamposRequeridosRepository extends ListCrudRepository<CamposRequeridosEntity, CamposRequeridosId> {
    List<CamposRequeridosEntity> findByServicio(String servicio);
}
