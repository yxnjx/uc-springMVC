package com.sw.finalProject.dto;

import com.sw.finalProject.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter @Getter

public class MemberForm {
    private Long id;

    private String name;

    private String email;

    private String password;

    public Member toEntity() {
        return new Member(id, name, email, password);
    }
}
