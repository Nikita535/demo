package com.example.demo.config.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableFilter {

    private String query;
    private Integer limit = 20;
    private Integer offset = 0;
    private List<Sort> sorts;
    private String sortColumn;
    private Direction direction = Direction.ASC;

    @JsonIgnore
    public Pageable getPageRequest() {
        if (StringUtils.hasLength(getSortColumn())) {
            if (getSorts() == null) {
                setSorts(new ArrayList<>());
            }
            getSorts().add(Sort.builder().field(getSortColumn()).direction(getDirection()).build());
        }

        if (getSorts() != null && !getSorts().isEmpty()) {
            return PageRequest.of(
                    getOffset() / getLimit(),
                    getLimit(),
                    org.springframework.data.domain.Sort.by(
                            getSorts()
                                    .stream()
                                    .map(sort ->
                                            new org.springframework.data.domain.Sort.Order(
                                                    org.springframework.data.domain.Sort.Direction.fromString(sort.getDirection().name()),
                                                    sort.getField()
                                            )
                                    )
                                    .collect(Collectors.toList())
                    )
            );
        }

        return PageRequest.of(getOffset() / getLimit(), getLimit());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Sort {

        private String field;
        private Direction direction = Direction.ASC;

    }

    public enum Direction {
        ASC,
        DESC
    }

}
