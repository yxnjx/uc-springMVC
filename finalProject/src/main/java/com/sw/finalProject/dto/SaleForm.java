package com.sw.finalProject.dto;

import com.sw.finalProject.entity.Sale;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString

public class SaleForm {
    private Long id;
    private String title;
    private String price;
    private String content;

    public Sale toEntity() {
        return new Sale(id, title, price, content);
    }
}
