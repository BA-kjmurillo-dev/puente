package com.puente.persistence.repository;

import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ValoresGlobalesRemesasRepository extends ListCrudRepository<ValoresGlobalesRemesasEntity, String> {
    ValoresGlobalesRemesasEntity findByCodigoAndItem(String code, String item);

    List<ValoresGlobalesRemesasEntity> findByItem(String item);
}
