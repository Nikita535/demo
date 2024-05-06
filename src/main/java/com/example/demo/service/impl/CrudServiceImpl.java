package com.example.demo.service.impl;

import com.example.demo.mapper.EntityMapper;
import com.example.demo.repository.EntityRepository;
import com.example.demo.service.CrudService;

import org.springframework.transaction.annotation.Transactional;

public abstract class CrudServiceImpl<E, CreateDto, PatchDto, Dto, Id, R extends EntityRepository<E, Id>> extends GetServiceImpl<E, Dto, Id, R> implements CrudService<CreateDto, PatchDto, Dto, Id> {

    protected final EntityMapper<E, Dto, CreateDto, PatchDto> entityMapper;

    protected CrudServiceImpl(R repository, EntityMapper<E, Dto, CreateDto, PatchDto> mapper) {
        super(repository, mapper);
        this.entityMapper = mapper;
    }

    @Transactional
    @Override
    public Dto create(CreateDto createDto) {
        E entity = entityMapper.toEntityFromCreateDto(createDto);
        entity = repository.save(entity);
        return mapper.domainToDto(entity);
    }

    @Override
    public Dto update(PatchDto patchDto, Id id) {
        E currentEntity = getEntityById(id);
        E entity = entityMapper.toEntityFromCreateDtoPatch(currentEntity, patchDto);
        entity = repository.save(entity);
        return mapper.domainToDto(entity);
    }

    @Override
    public void delete(Id id) {
        repository.deleteById(id);
    }
}
