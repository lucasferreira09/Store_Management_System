package com.example.StoreManagement.model.dtoResponse;

import org.springframework.data.domain.Sort;

public record PaginationResponse(
        Integer page,
        Integer size,
        String sortField,
        Sort.Direction direction,
        boolean isLast
) {}
