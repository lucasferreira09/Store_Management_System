package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.StockMovementHistoryMapper;
import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PaginationUtils;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoResponse.StockMovementHistoryDtoResponse;
import com.example.StoreManagement.model.entity.StockMovementHistory;
import com.example.StoreManagement.repository.StockMovementHistoryRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockMovementHistoryService {
    private final StockMovementHistoryRespository stockMovementHistoryRespository;
    private final StockMovementHistoryMapper stockMovementHistoryMapper;


    public PagingResult<StockMovementHistoryDtoResponse> findAll(PaginationRequest request) {
       Pageable pageable = PaginationUtils.getPageable(request);

       Page<StockMovementHistory> stockMovementHistoryPage = stockMovementHistoryRespository.findAll(pageable);
       List<StockMovementHistoryDtoResponse> stockMovementHistoryDtoList = stockMovementHistoryPage
               .stream()
               .map(stockMovementHistoryMapper::entityToDtoResponse)
               .toList();

        return new PagingResult<>(
                stockMovementHistoryDtoList,
                stockMovementHistoryPage.getTotalPages(),
                stockMovementHistoryPage.getTotalElements(),
                stockMovementHistoryPage.getSize(),
                stockMovementHistoryPage.getNumber(),
                stockMovementHistoryPage.isEmpty(),
                stockMovementHistoryPage.isLast()
        );
    }
}
