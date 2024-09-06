package pl.bpwesley.TourOperator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientNumber;
    private String name;
    private String alias;
    private String street;
    private String number;
    private String postCode;
    private String city;

    @Embedded // wbudowane pola klasy PickupLocation
    private PickupLocation pickupLocation;

    public Client(String name, String alias, String street, String number, String postCode, String city, PickupLocation pickupLocation) {
        this.name = name;
        this.alias = alias;
        this.street = street;
        this.number = number;
        this.postCode = postCode;
        this.city = city;
        this.pickupLocation = pickupLocation;
    }
}
