package com.mc.infrastructure.persistence.id;

import com.mc.domain.core.service.DomainEntityFactory;
import com.mc.domain.core.service.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Domain Entity Factory Implementation - Infrastructure Layer
 * Implements DomainEntityFactory using IdGenerator.
 */
@Component
@RequiredArgsConstructor
public class DomainEntityFactoryImpl implements DomainEntityFactory {

    private final IdGenerator idGenerator;

    @Override
    public UUID generateId() {
        return idGenerator.generate();
    }

    @Override
    public UUID generateTimeOrderedId() {
        return idGenerator.generateTimeOrdered();
    }
}
