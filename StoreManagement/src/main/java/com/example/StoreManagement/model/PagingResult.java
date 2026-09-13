package com.example.StoreManagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collection;

@Data
@NoArgsConstructor
public class PagingResult<T> {

    private Collection<T> content;
    private Integer totalPages;
    private long totalElements;
    private Integer size;
    private Integer page;
    private boolean empty;
    private boolean last;

    public PagingResult(
            Collection<T> content,
            Integer totalPages,
            long totalElements,
            Integer size,
            Integer page,
            boolean empty,
            boolean last
    ) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.page = page;
        this.empty = empty;
        this.last = last;
    }
}
