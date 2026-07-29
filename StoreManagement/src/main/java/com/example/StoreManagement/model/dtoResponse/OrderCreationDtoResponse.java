package com.example.StoreManagement.model.dtoResponse;

import java.util.ArrayList;
import java.util.List;

public record OrderCreationDtoResponse(
        String sessionId,
        String url
) {}
