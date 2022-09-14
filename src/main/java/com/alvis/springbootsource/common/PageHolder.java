package com.alvis.springbootsource.common;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

@Getter
public class PageHolder {
    public static final String TOTAL_PAGES_HEADER = "Total-Pages";
    private int totalPages;

    public void setPage(Page<?> page) {
        totalPages = page.getTotalPages();
    }

    public HttpHeaders buildPaginationHeaders() {
        var headers = new HttpHeaders();
        headers.add(TOTAL_PAGES_HEADER, String.valueOf(totalPages));
        return headers;
    }
}
