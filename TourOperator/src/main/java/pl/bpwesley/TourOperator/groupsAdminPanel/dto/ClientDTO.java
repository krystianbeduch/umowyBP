package pl.bpwesley.TourOperator.groupsAdminPanel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bpwesley.TourOperator.groupsAdminPanel.entity.PickupLocation;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
