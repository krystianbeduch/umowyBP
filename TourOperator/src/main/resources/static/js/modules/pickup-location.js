import { slideUp, slideDown} from "./animations.js";

export function handlePickupLocationToggle(prefix, pickupLocation, pickupLocationGroup, checkbox) {
    // Wyszukaj wszystkie inputu miejsca odbioru; inputy ktorych id zaczyna sie od 'add-pickup-'
    const pickupFields = document.querySelectorAll(
        `input[id^='${prefix}-pickup-']`);

    if(checkbox.checked) {
        // pickupLocationGroup.classList.add("fade-out");
        // pickupLocationGroup.classList.remove("fade-in");
        slideUp(pickupLocationGroup);

        // Ukryj sekcje "Miejsce odbioru"
        // pickupLocationGroup.style.display = "none";

        // Usun obowiazkowosc pol
        pickupFields.forEach(field => {
            field.removeAttribute("required");
            field.value = "";
        });

        // Ustaw w polu domyslna wartosc dla bazy
        pickupLocation.value = "*Adres*";
    } // if
    else {
        // pickupLocationGroup.classList.add("fade-in");
        // pickupLocationGroup.classList.remove("fade-out");
        slideDown(pickupLocationGroup);
        // pickupLocationGroup.style.display = "block";
        pickupLocation.value = "";

        // Jesli "Miejsce odbioru" jest podawane, niektore pola zostaja ustawione na obowiazkowe
        pickupFields.forEach(field => {
            if (field.id.includes("number")) {
                return; // Pole number ma pozostac nieobowiazkowe
            }
            field.setAttribute("required", "true");
        });

    } // else
} // togglePickupLocation()