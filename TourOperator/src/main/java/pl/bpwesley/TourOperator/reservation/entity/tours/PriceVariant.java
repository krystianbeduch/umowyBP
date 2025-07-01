package pl.bpwesley.TourOperator.reservation.entity.tours;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "price_variants")
public class PriceVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private Integer ageFrom;
    private Integer ageTo;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private IndividualOneDayTour tour; // TODO: baseTour do konkretnego typu wycieczki
}
