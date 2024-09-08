document.addEventListener("DOMContentLoaded", function() {
   const checkbox = document.getElementById("pickup-location-same-as-address");
   const pickupLocationGroup = document.querySelector(".pickup-location-group");
   const pickupLocation = document.getElementById("add-pickup-location");
   const form = document.getElementById("client-form");

   // Funkcja do ukrywania/pokazywania sekcji "miejsce-odbioru"
   function togglePickupLocation() {
      // Wyszukaj wszystkie inputu miejsca odbioru; inputy ktorych id zaczyna sie od 'add-pickup-'
      const pickupFields = document.querySelectorAll("input[id^='add-pickup-']");
      if(checkbox.checked) {
         // Ukryj sekcje "Miejsce odbioru" i ustaw w polu domyślną wartość
         pickupLocationGroup.style.display = "none";
         pickupLocation.value = "*Adres*";

         // Usun obowiazkowosc pol
         pickupFields.forEach(field => {
            field.removeAttribute("required");
            field.value = "";
         });
      } // if
      else {
         pickupLocationGroup.style.display = "block";
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

   // Wlaczenie obslugi na zmiane checkboxa
   checkbox.addEventListener("change", togglePickupLocation);


   // Funkcja do ustawiania pustych pol na null
   form.addEventListener("submit", function(event)  {
      let isValid = true;

      // Wyczysc poprzednie bledy
      document.querySelectorAll('.error-message').forEach(el => el.textContent = '');

      // Pobierz wszystkie inputy
      const inputs = form.querySelectorAll('input[type="text"], input[type="number"]');
      inputs.forEach(input => {
         const value = input.value.trim();
         const errorMessageElement = document.getElementById(`error-${input.id}`);

         // Jesli wartosc nieobowiazkowych pol jest pusta, ustaw null (nie dziala w bazie)
         if (value === "") {
            input.value = null;
         }

         // Walidacja ogolna dla pol numeru i kodu pocztowego
         if (input.name === "number" && !/^[A-Za-z0-9\/]+$/.test(value)) {
            isValid = false;
            errorMessageElement.textContent = "Numer jest niepoprawny";
         }
         else if (input.name === "postCode" && !/^\d{2}-\d{3}$/.test(value)) {
            isValid = false;
            errorMessageElement.textContent = "Kod pocztowy musi mieć format ##-###";
         }

         // Walidacja specyficzna dla "Miejsca odbioru" gdy jest on podawany osobno
         if (!checkbox.checked) {
            // Numer nie musi byc podany - dlatego w regexie jest na koncu *
            if (input.name === "pickupLocation.pickupNumber" && !/^[A-Za-z0-9\/]*$/.test(value)) {
               isValid = false;
               errorMessageElement.textContent = "Numer jest niepoprawny";
            }
            else if (input.name === "pickupLocation.pickupPostCode" && !/^\d{2}-\d{3}$/.test(value)) {
               isValid = false;
               errorMessageElement.textContent = "Kod pocztowy musi mieć format ##-###";
            }
         }
      }); // inputs.forEach

      // Zatrzymanie wyslanie formularza jesli dane nie sa poprawne
      if (!isValid) {
         event.preventDefault();
      }
   });  // form.addEventListener

   togglePickupLocation();
});