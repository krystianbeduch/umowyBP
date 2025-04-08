package pl.bpwesley.TourOperator.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bpwesley.TourOperator.reservation.service.TourService;

@Controller
public class TourController {
    private final TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

//    public
}
