package com.sw.finalProject.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@ToString
@Entity

public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String price;

    @Column
    private String content;

    @Column
    private String filename;

    public Sale(Long id, String title, String price, String content) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.content = content;
    }
}
