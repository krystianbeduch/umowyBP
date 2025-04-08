package pl.bpwesley.TourOperator.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.reservation.entity.Tour;
import pl.bpwesley.TourOperator.reservation.repository.TourRepository;

import java.util.List;

@Service
public class TourService {
    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

//    public Tour getTourById(Long id) {
//        return tourRepository.findAllById(id);
//    }
}
