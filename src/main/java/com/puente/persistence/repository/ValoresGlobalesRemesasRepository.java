package com.puente.persistence.repository;

import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface ValoresGlobalesRemesasRepository extends ListCrudRepository<ValoresGlobalesRemesasEntity, String> {
    Optional<ValoresGlobalesRemesasEntity> findByCodigoAndItem(String code, String item);

    List<ValoresGlobalesRemesasEntity> findByItem(String item);


}
