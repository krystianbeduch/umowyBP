package pl.bpwesley.TourOperator.reservation.entity.tours;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bpwesley.TourOperator.reservation.entity.tours.BaseTour;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tours_in_1day")
@Inheritance(strategy = InheritanceType.JOINED)
public class IndividualOneDayTour extends BaseTour {
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceVariant> priceVariants = new ArrayList<>();
}
