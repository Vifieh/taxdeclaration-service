package com.example.taxdeclarationservice.model;

import com.example.taxdeclarationservice.constant.TaxType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DeclaredTax extends CustomEntity{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    private TaxType typeOfTax;
    private String description;
    private double amount;
    private double amountToPay;
    private boolean paid = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "declaredTax", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Payment> paymentList;
}
