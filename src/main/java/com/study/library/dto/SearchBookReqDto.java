package com.study.library.dto;

import lombok.Data;

@Data
public class SearchBookReqDto {
    private int bookTypeId;
    private int categoryId;
    private int searchTypeId;
    private String searchText;
}
