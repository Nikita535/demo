package com.example.demo.service.impl;

import com.example.demo.config.param.QuerySearchParams;
import com.example.demo.mapper.BaseMapper;
import com.example.demo.repository.EntityRepository;
import com.example.demo.service.GetService;
import io.github.perplexhub.rsql.RSQLJPASupport;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;


public abstract class GetServiceImpl<E,DTO,ID,R extends EntityRepository<E,ID>> implements GetService<DTO,ID> {

    protected final R repository;
    protected final BaseMapper<E,DTO> mapper;

    protected GetServiceImpl(R repository, BaseMapper<E,DTO> mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DTO> get(QuerySearchParams params){
        Page<E> page = getEntities(params);
        return new PageImpl<>(
                mapper.domainsToDtos(page.getContent()),
                page.getPageable(),
                page.getTotalElements()
        );
    }

    protected Page<E> getEntities(QuerySearchParams params){
        return repository.findAll(RSQLJPASupport.rsql(params.getQuery()),params.getPageRequest());
    }

    @Transactional(readOnly = true)
    @Override
    public  DTO getById(ID id){
        E entity = getEntityById(id);
        return mapper.domainToDto(entity);
    }

    protected E getEntityById(ID id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("EntityNotFound"));
    }
}
