package pl.bpwesley.TourOperator.reservation.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.reservation.entity.tours.BaseTour;
import pl.bpwesley.TourOperator.reservation.entity.tours.IndividualOneDayTour;
import pl.bpwesley.TourOperator.reservation.entity.tours.PriceVariant;
import pl.bpwesley.TourOperator.reservation.repository.IndividualOneDayTourRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IndividualOneDayTourService {
    private final IndividualOneDayTourRepository tourRepository;

    @Autowired
    public IndividualOneDayTourService(IndividualOneDayTourRepository individualOneDayTourRepository) {
        this.tourRepository = individualOneDayTourRepository;
    }

    public List<IndividualOneDayTour> getAllTours() {
        return tourRepository.findAll();
    }

    @Transactional
    public void saveNewTour(IndividualOneDayTour tour) {
        if (tour.getPriceVariants() != null) {
            for (PriceVariant pv : tour.getPriceVariants()) {
                pv.setTour(tour);
            }
        }
        tour.setUpdateDate(LocalDateTime.now());
        tourRepository.save(tour);
    }

    public IndividualOneDayTour getTourById(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wycieczka o ID " + id + " nie została znaleziona"));
    }
}
