package com.example.demo.mapper;

import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

public interface EntityMapper<E,Dto,CreateDto,PatchDto> extends BaseMapper<E,Dto> {
    @Named("fromCreateDto")
    E toEntityFromCreateDto(CreateDto dto);

    @Named("fromPatchDto")
    E toEntityFromCreateDtoPatch(@MappingTarget E entity, PatchDto dto);
}