package com.tlp.challenge.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String surname;

    @NaturalId
    private String fiscalCode;

    private String address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Device> devices;

    public Customer(Long id, String name, String surname, String fiscalCode, String address, List<Device> devices) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fiscalCode = fiscalCode;
        this.address = address;
        this.devices=devices;
    }

    private Customer() {
    }
}