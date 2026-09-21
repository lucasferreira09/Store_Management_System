package com.example.StoreManagement.dtos.dtoResponse;

import org.springframework.data.domain.Sort;

public record PaginationResponse(
        Integer page,
        Integer size,
        String sortField,
        Sort.Direction direction,
        boolean isLast
) {}
