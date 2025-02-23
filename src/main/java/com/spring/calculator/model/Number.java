package com.spring.calculator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "numbers")
public class Number {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "number_1")
    private int number1;

    @Column(name = "number_2")
    private int number2;

    @Column(name = "operation")
    private String operation;

    @Column(name = "result")
    private int result;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User users;

    public Number(int number1, int number2, String operation, int result) {
        this.number1 = number1;
        this.number2 = number2;
        this.operation = operation;
        this.result = result;
    }

    @Override
    public String toString() {
        return "Number{" +
                "id=" + id +
                ", number1=" + number1 +
                ", number2=" + number2 +
                ", operation='" + operation + '\'' +
                ", result=" + result +
                '}';
    }
}
