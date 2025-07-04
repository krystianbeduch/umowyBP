package pl.bpwesley.TourOperator.reservation.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.reservation.entity.tours.BaseTour;
import pl.bpwesley.TourOperator.reservation.entity.tours.IndividualOneDayTour;
import pl.bpwesley.TourOperator.reservation.entity.tours.PriceVariant;
import pl.bpwesley.TourOperator.reservation.exception.TourNotFoundException;
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

    public IndividualOneDayTour getTourById(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wycieczka o ID " + id + " nie została znaleziona"));
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

    @Transactional
    public void updateTour(IndividualOneDayTour tour) {
        IndividualOneDayTour existingTour = tourRepository
                .findById(tour.getTourId())
                .orElseThrow(() -> new TourNotFoundException("Wycieczka o ID " + tour.getTourId() + " nie istnieje"));

        existingTour.setCatalogId(tour.getCatalogId());
        existingTour.setTitle(tour.getTitle());
        existingTour.setStartDate(tour.getStartDate());
        existingTour.setEndDate(tour.getEndDate());
        existingTour.setFinalPaymentDate(tour.getFinalPaymentDate());
        existingTour.setNumberOfDaysOfAdvancePaymentAfterBooking(tour.getNumberOfDaysOfAdvancePaymentAfterBooking());
        existingTour.setNumberOfAvailableSeats(tour.getNumberOfAvailableSeats());
        existingTour.setNumberOfSeatsForMessageLastSeats(tour.getNumberOfSeatsForMessageLastSeats());
        existingTour.setLocation(tour.getLocation());
        existingTour.setTransport(tour.getTransport());
        existingTour.setCatering(tour.getCatering());
        existingTour.setInsurance(tour.getInsurance());
        existingTour.setNotes(tour.getNotes());
        existingTour.setPrice(tour.getPrice());
        existingTour.setUpdateDate(LocalDateTime.now());
        existingTour.setReservations(tour.getReservations());

        existingTour.getPriceVariants().clear();

        if (tour.getPriceVariants() != null) {
            for (PriceVariant pv : tour.getPriceVariants()) {
                pv.setTour(existingTour); // powiazanie FK
                existingTour.getPriceVariants().add(pv);
            }
        }

        tourRepository.save(existingTour);
    }

    @Transactional
    public void deleteTourById(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new TourNotFoundException("Wycieczka o ID: " + id + " nie istnieje");
        }
        tourRepository.deleteById(id);
    }
}
