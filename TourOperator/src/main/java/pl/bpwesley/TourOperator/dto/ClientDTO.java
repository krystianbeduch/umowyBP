package pl.bpwesley.TourOperator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.bpwesley.TourOperator.entity.PickupLocation;

@Getter
@Setter
@AllArgsConstructor
public class ClientDTO {
    private Long clientNumber;
    private String name;
    private String alias;
    private String street;
    private String number;
    private String postCode;
    private String city;
    private PickupLocation pickupLocation;
}
