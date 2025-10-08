package com.example.power_rent.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedResponse<T> {
    private List<T> content;
    private int size;
    private int page;
    private long totalElements;
    private int totalPages;
}
