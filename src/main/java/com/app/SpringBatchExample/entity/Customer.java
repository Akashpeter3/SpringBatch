package com.app.SpringBatchExample.entity;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER_INFO")
public class Customer {

    @Id
    @Column(name = "CUSTOMER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "CUSTOMER_NAME")
    private String name;
    @Column(name = "CUSTOMER_AGE")
    private int age;
    @Column(name = "CUSTOMER_EMAIL")
    private String email;

}
