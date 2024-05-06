package com.example.demo.config.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@Data
@Schema( description = "Параметры поиска")
public class PageableParams {

    @Schema(description = "Размер страницы")
    private Integer limit = 10;

    @Schema( description = "Указатель на страницу")
    private Integer offset = 0;

    @Schema( description = "Направление сортировки")
    private String direction;// = "ASC";

    @Schema( description = "Колонка, по которой произвести сортировку")
    private String sortColumn;

    @JsonIgnore
    public Pageable getPageRequest() {
        if (StringUtils.hasText(getSortColumn())) {
            return PageRequest.of(getOffset() / getLimit(), getLimit(), Sort.Direction.fromString(getDirection()), getSortColumn());
        }
        if (StringUtils.hasText(getDirection())) {
            return PageRequest.of(getOffset() / getLimit(), getLimit(), Sort.Direction.fromString(getDirection()));
        }
        return PageRequest.of(getOffset() / getLimit(), getLimit());
    }
}
