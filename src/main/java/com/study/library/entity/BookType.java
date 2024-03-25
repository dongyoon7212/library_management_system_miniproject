package com.study.library.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookType {
    private int bookTpyeId;
    private String bookTypeName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
