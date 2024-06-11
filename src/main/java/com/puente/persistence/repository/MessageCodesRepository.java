package com.puente.persistence.repository;

import com.puente.persistence.entity.MessageCodesEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface MessageCodesRepository extends ListCrudRepository<MessageCodesEntity, String> {

}
