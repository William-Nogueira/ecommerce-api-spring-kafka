package dev.williamnogueira.ecommerce.domain.address;

import dev.williamnogueira.ecommerce.domain.common.BaseEntity;
import dev.williamnogueira.ecommerce.domain.customer.CustomerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "Address")
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AddressEntity extends BaseEntity {

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AddressTypeEnum type;

    @Column
    private String additionalInfo;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private CustomerEntity customer;
}
