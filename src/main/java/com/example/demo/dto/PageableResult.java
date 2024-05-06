package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Ответ на запрос с пагинацией")

public class PageableResult<T> extends Result<T> {

    @Schema(description = "Смещение")

    private int offset;

    @Schema(description = "Количество")

    private int limit;

    @Schema( description = "Общее количество")

    private long total;

    public PageableResult(T data, int offset, int limit, long total) {
        super(data);
        this.offset = offset;
        this.limit = limit;
        this.total = total;
    }

    public static <T> PageableResult<T> success(final T data, int offset, int limit, long total) {
        return new PageableResult(data, offset, limit, total);
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLimit() {
        return this.limit;
    }

    public long getTotal() {
        return this.total;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageableResult)) {
            return false;
        } else {
            PageableResult<?> other = (PageableResult)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getOffset() != other.getOffset()) {
                return false;
            } else if (this.getLimit() != other.getLimit()) {
                return false;
            } else {
                return this.getTotal() == other.getTotal();
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PageableResult;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getOffset();
        result = result * 59 + this.getLimit();
        long $total = this.getTotal();
        result = result * 59 + (int)($total >>> 32 ^ $total);
        return result;
    }

    public String toString() {
        int var10000 = this.getOffset();
        return "PageableResult(offset=" + var10000 + ", limit=" + this.getLimit() + ", total=" + this.getTotal() + ")";
    }

    public PageableResult() {
    }

    public PageableResult(final int offset, final int limit, final long total) {
        this.offset = offset;
        this.limit = limit;
        this.total = total;
    }
}
