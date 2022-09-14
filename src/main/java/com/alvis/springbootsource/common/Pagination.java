package com.alvis.springbootsource.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class Pagination {
    @Min(value = 0, message = "Page must be greater than 0")
    private int page = 0;

    @Min(value = 1, message = "Size must be greater than 1")
    @Max(value = 100, message = "Size must be smaller than 100")
    private int size = 10;

    public Pageable toPageable(Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}