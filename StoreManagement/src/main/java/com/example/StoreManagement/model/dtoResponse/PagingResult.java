package com.example.StoreManagement.model.dtoResponse;

import java.util.Collection;

public record PagingResult(
        Collection content,
        Integer totalPages,
        long totalElements,
        Integer size,
        Integer page,
        boolean empty,
        boolean last
) {}
