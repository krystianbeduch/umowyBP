package pl.bpwesley.TourOperator.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Embeddable
public class PickupLocation {
    private String pickupLocation = "*Adres*";
    private String pickupStreet;
    private String pickupNumber;
    private String pickupPostCode;
    private String pickupCity;
}
