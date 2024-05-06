package com.example.demo.service;

import com.example.demo.config.param.QuerySearchParams;
import org.springframework.data.domain.Page;

public interface GetService<DTO,ID> {
    Page<DTO> get(QuerySearchParams params);
    DTO getById(ID id);
}
