package com.example.demo.config.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Параметры поиска с фильтром")
public class QuerySearchParams extends PageableParams {

    @Schema(description = "Быстрый фильтр")
    private String query;

}