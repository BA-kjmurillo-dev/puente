package com.puente.persistence.repository;

import com.puente.persistence.entity.SeguridadCanalEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface MessageCodesRepository extends ListCrudRepository<SeguridadCanalEntity, String> {

}
