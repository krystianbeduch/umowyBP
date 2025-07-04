package pl.bpwesley.TourOperator.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.bpwesley.TourOperator.reservation.dto.CountryDto;
import pl.bpwesley.TourOperator.reservation.dto.PrefixCountryDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://restcountries.com/v3.1/all?fields=name,idd";

    public List<PrefixCountryDto> getCountries() {
        CountryDto[] countries = restTemplate.getForObject(API_URL, CountryDto[].class);

        if (countries == null) {
            return List.of();
        }

        return Arrays.stream(countries)
                .map(this::mapToPrefixCountryDto)
                .sorted((c1, c2) -> {
                    if (c1.getCommonName().equalsIgnoreCase("Poland")) {
                        return -1;
                    }
                    if (c2.getCommonName().equalsIgnoreCase("Poland")) {
                        return 1;
                    }
                    return c1.getCommonName().compareToIgnoreCase(c2.getCommonName());
                })
                .collect(Collectors.toList());
    }

    private PrefixCountryDto mapToPrefixCountryDto(CountryDto countryDto) {
        String phoneCode = "";
        if (countryDto.getIdd() != null) {
            String root = countryDto.getIdd().getRoot() != null ? countryDto.getIdd().getRoot() : "";
            String suffix = (countryDto.getIdd().getSuffixes() != null && !countryDto.getIdd().getSuffixes().isEmpty())
                    ? countryDto.getIdd().getSuffixes().getFirst()
                    : "";
            phoneCode = root + suffix;
        }
        String commonName = countryDto.getName() != null ? countryDto.getName().getCommon() : "";

        return new PrefixCountryDto(commonName, phoneCode);
    }
}
