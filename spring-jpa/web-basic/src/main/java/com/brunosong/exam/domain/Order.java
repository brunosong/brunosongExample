package com.brunosong.exam.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    private Member member;

    private List<OrderItem> orderItems = new ArrayList<>();
}
