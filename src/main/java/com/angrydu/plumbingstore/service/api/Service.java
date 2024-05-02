package com.angrydu.plumbingstore.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Service<Entity, Id> {
    Entity getById(Id id);

    Page<Entity> getAll(Pageable pageable);

    Entity create(Entity entity);

    void delete(Id id);
}
