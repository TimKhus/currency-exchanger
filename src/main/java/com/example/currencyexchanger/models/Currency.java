package com.example.currencyexchanger.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="currency")
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @NotNull
    @Size(max=3)
    @Column(name = "code", unique = true)
    String code;

    @NotNull
    @Column(name = "full_name")
    String fullName;

    @Column(name = "sign")
    String sign;
}
