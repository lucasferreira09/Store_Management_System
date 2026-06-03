package com.example.StoreManagement.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {

    public static Pageable getPageable(PaginationRequest request) {

        return PageRequest.of(
                request.getPageNumber(),
                request.getSize(),
                request.getDirection(),
                request.getSortField()
        );
    }
}
