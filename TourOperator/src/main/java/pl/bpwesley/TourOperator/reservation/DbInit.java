package pl.bpwesley.TourOperator.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.bpwesley.TourOperator.reservation.entity.Participant;
import pl.bpwesley.TourOperator.reservation.entity.Reservation;
import pl.bpwesley.TourOperator.reservation.entity.Tour;
import pl.bpwesley.TourOperator.reservation.repository.ParticipantRepository;
import pl.bpwesley.TourOperator.reservation.repository.ReservationRepository;
import pl.bpwesley.TourOperator.reservation.repository.TourRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Profile("create-simple-reservation")
@Configuration
public class DbInit implements CommandLineRunner {
    private final ReservationRepository reservationRepository;
    private final TourRepository tourRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public DbInit(ReservationRepository reservationRepository, TourRepository tourRepository, ParticipantRepository participantRepository) {
        this.reservationRepository = reservationRepository;
        this.tourRepository = tourRepository;
        this.participantRepository = participantRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        // Wycieczka
        Tour tour = new Tour();
        tour.setTitle("Wakacje w Grecji");
        tour.setStartDate(LocalDate.now());
        tour.setEndDate(LocalDate.now().plusDays(7));
        tour.setFinalPaymentDate(LocalDate.now().plusDays(3));
        tour.setNumberOfAvailableSeats(50);
        tour.setNumberOfSeatsForMessageLastSeats(5);
        tour.setLocation("Ateny");
        tour.setTransport("Samolot");
        tour.setHotel("Hotel Grecki");
        tour.setCatering("All inclusive");
        tour.setInsurance("Pełne");
        tour.setNotesToTheAgreement("Brak uwag");
        tour.setPrice("3500 PLN");
        tour.setType("Indywidualna");
        tour.setUpdateDate(LocalDateTime.now());
        tourRepository.save(tour);

        // Rezerwacja 1 (1 uczestnik)
        Participant main1 = new Participant();
        main1.setFirstName("Jan");
        main1.setLastName("Kowalski");
        main1.setEmail("jan@example.com");
        main1.setPhone("123456789");
        main1.setBirthDate(LocalDate.of(2002, 12, 4));
        main1.setPrice(3500);
        main1.setConfirmed(false);
        main1.setAdvancePaymentPaid(false);
        main1.setTotalPaid(false);
        main1.setComment("Rezerwacja 2 - 3 osoby");
        main1.setMainParticipant(true);

        Reservation reservation1 = new Reservation();
        reservation1.setTour(tour);
        reservation1.setMainParticipant(main1);

        main1.setReservation(reservation1);
        reservation1.setParticipants(List.of(main1));

        reservationRepository.save(reservation1);

        // Rezerwacja 2 (5 uczestnikow)
        Participant main2 = new Participant();
        main2.setFirstName("Anna");
        main2.setLastName("Nowak");
        main2.setEmail("anna@example.com");
        main2.setPhone("987654321");
        main2.setBirthDate(LocalDate.of(2002, 12, 4));
        main2.setPrice(3500);
        main2.setConfirmed(false);
        main2.setAdvancePaymentPaid(false);
        main2.setTotalPaid(false);
        main2.setComment("Rezerwacja 2 - 3 osoby");
        main2.setMainParticipant(true);

        Participant p2a = new Participant();
        p2a.setFirstName("Marek");
        p2a.setLastName("Nowak");
        p2a.setBirthDate(LocalDate.of(2002, 12, 4));
        p2a.setPrice(3500);
        p2a.setConfirmed(false);
        p2a.setAdvancePaymentPaid(false);
        p2a.setTotalPaid(false);
        p2a.setComment("Rezerwacja 2 - 5 osób");
        p2a.setMainParticipant(false);

        Participant p2b = new Participant();
        p2b.setFirstName("Kasia");
        p2b.setLastName("Nowak");
        p2b.setBirthDate(LocalDate.of(2002, 12, 4));
        p2b.setPrice(3500);
        p2b.setConfirmed(false);
        p2b.setAdvancePaymentPaid(false);
        p2b.setTotalPaid(false);
        p2b.setComment("Rezerwacja 2 - 5 osób");
        p2b.setMainParticipant(false);

        Participant p2c = new Participant();
        p2c.setFirstName("Adam");
        p2c.setLastName("Nowak");
        p2c.setBirthDate(LocalDate.of(2002, 12, 4));
        p2c.setPrice(3500);
        p2c.setConfirmed(false);
        p2c.setAdvancePaymentPaid(false);
        p2c.setTotalPaid(false);
        p2c.setComment("Rezerwacja 2 - 5 osób");
        p2c.setMainParticipant(false);

        Participant p2d = new Participant();
        p2d.setFirstName("Zdzisław");
        p2d.setLastName("Nowak");
        p2d.setBirthDate(LocalDate.of(1950, 12, 4));
        p2d.setPrice(3500);
        p2d.setConfirmed(false);
        p2d.setAdvancePaymentPaid(false);
        p2d.setTotalPaid(false);
        p2d.setComment("Rezerwacja 2 - 5 osób");
        p2d.setMainParticipant(false);

        Participant p2e = new Participant();
        p2e.setFirstName("Emilia");
        p2e.setLastName("Nowak");
        p2e.setBirthDate(LocalDate.of(2018, 12, 4));
        p2e.setPrice(3500);
        p2e.setConfirmed(false);
        p2e.setAdvancePaymentPaid(false);
        p2e.setTotalPaid(false);
        p2e.setComment("Rezerwacja 2 - 5 osób");
        p2e.setMainParticipant(false);

        Reservation reservation2 = new Reservation();
        reservation2.setTour(tour);
        reservation2.setMainParticipant(main2);

        main2.setReservation(reservation2);
        p2a.setReservation(reservation2);
        p2b.setReservation(reservation2);
        p2c.setReservation(reservation2);
        p2d.setReservation(reservation2);
        p2e.setReservation(reservation2);

        reservation2.setParticipants(Arrays.asList(main2, p2a, p2b, p2c, p2d, p2e));

        reservationRepository.save(reservation2);
    }
}
