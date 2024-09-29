package pl.bpwesley.TourOperator.groupsAdminPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.bpwesley.TourOperator.groupsAdminPanel.entity.Client;
import pl.bpwesley.TourOperator.groupsAdminPanel.entity.PickupLocation;
import pl.bpwesley.TourOperator.groupsAdminPanel.repository.ClientRepository;

import java.util.List;

@Profile("startup") // do aktywacji proflu: spring.profiles.active=startup
@Configuration
public class DbInit implements CommandLineRunner {

    private final ClientRepository clientRepository;

    @Autowired
    public DbInit(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        clientRepository.saveAll(List.of(
                new Client("Zakład Opieki Zdrowotnej Szpital im. Jana Pawła II we Włoszczowie", "", "ul. Żeromskiego", "28", "29-100", "Włoszczowa",
                        new PickupLocation()),
                new Client("Zespół Szkolno-Przedszkolny nr 5", null, "ul. Św.Barbary", "32", "42-200", "Częstochowa",
                        new PickupLocation()),
                new Client("Szkoła Podstawowa nr 50", null , "ul. Starzyńskiego", "10", "42-224", "Częstochowa",
                        new PickupLocation()),
                new Client("Szkoła Podstawowa z Oddziałami Integracyjnymi nr 53", null, "ul. Orkana", "95/109", "42-229", "Częstochowa",
                        new PickupLocation()),
                new Client("Zespół Szkoł Technicznych im. Jana Pawła II", null, "al. Jana Pawła II", "126/130", "42-220", "Częstochowa",
                        new PickupLocation()),
                new Client("Miejskie Przedszkole nr 21", null, "ul. Przemysłowa", "6", "42-202", "Częstochowa",
                        new PickupLocation("parking za Społem", "ul.Senatorska", null, "42-202", "Częstochowa")),
                new Client("Zespół Szkolno-Przedkszolny w Aleksandrii", null, "ul. Gościnna", "130", "42-274", "Aleksandria Pierwsza",
                        new PickupLocation()),
                new Client("Związek Zawodowy Pracowników Inspekcji Sanitarnej w Częstochowie", "Sanepid", "ul. Jasnogórska", "15A", "42-200", "Częstochowa",
                        new PickupLocation())
        ));


    }
}
