document.addEventListener("DOMContentLoaded", function() {
   const checkbox = document.getElementById("pickup-location-same-as-address");
   const pickupLocationGroup = document.querySelector(".pickup-location-group");
   const pickupLocation = document.getElementById("edit-pickup-location");
   const form = document.getElementById("client-form");

   const rows = document.querySelectorAll('#client-overview-table tbody tr');
   rows.forEach(row => {
      row.addEventListener("click", function() {
         selectClient(this);
      })
   });

   function selectClient(row) {
      // Dodaj klase 'unselcted' i usun klase 'selected' ze wszystkich wierszy
      document.querySelectorAll('#client-overview-table tbody tr').forEach(r => {
            r.classList.remove("selected");
            r.classList.add("unselected");
      });

      // Dodaj klase 'selcted' i usun klase 'unselected' z aktualnego wiersza
      row.classList.add("selected");
      row.classList.remove("unselected");

      const clientNumber = row.getAttribute("data-client-number");

      fetch(`/api/client/${clientNumber}`)
          .then(response => response.json())
          .then(client => {
             if (client) {
                populateForm(client);
                console.log(client.pickupLocation.pickupLocation);
                if (client.pickupLocation.pickupLocation === "*Adres*") {
                   checkbox.checked = true;
                   pickupLocationGroup.style.display = "none";
                }
                else {
                   checkbox.checked = false;
                   pickupLocationGroup.style.display = "block";
                }
             }
             else {
                alert("Klient nie znaleziony");
             }
          })
          .catch(error => console.error("Błąd: ", error));
   } // selectClient()

   function populateForm(client) {
      document.getElementById('edit-client-id').value = client.clientNumber;
      document.getElementById('edit-name').value = client.name;
      document.getElementById('edit-alias').value = client.alias;
      document.getElementById('edit-street').value = client.street;
      document.getElementById('edit-number').value = client.number;
      document.getElementById('edit-post-code').value = client.postCode;
      document.getElementById('edit-city').value = client.city;

      document.getElementById('edit-pickup-location').value = client.pickupLocation?.pickupLocation || '';
      document.getElementById('edit-pickup-street').value = client.pickupLocation?.pickupStreet || '';
      document.getElementById('edit-pickup-number').value = client.pickupLocation?.pickupNumber || '';
      document.getElementById('edit-pickup-post-code').value = client.pickupLocation?.pickupPostCode || '';
      document.getElementById('edit-pickup-city').value = client.pickupLocation?.pickupCity || '';
   } // populateForm()

   function handlePickupLocationToggle() {
      // Wyszukaj wszystkie inputu miejsca odbioru; inputy ktorych id zaczyna sie od 'add-pickup-'
      const pickupFields = document.querySelectorAll("input[id^='edit-pickup-']");
      if(checkbox.checked) {
         // Ukryj sekcje "Miejsce odbioru" i ustaw w polu domyślną wartość
         pickupLocationGroup.style.display = "none";

         // Usun obowiazkowosc pol
         pickupFields.forEach(field => {
            field.removeAttribute("required");
            field.value = null;
         });

         // Ustaw wzorzec dla bazy
         pickupLocation.value = "*Adres*";
      } // if
      else {
         pickupLocationGroup.style.display = "block";

         // Wyszyczysc zawartosc wszystkich pol 'Miejsca odbioru'
         pickupFields.forEach(field => {
            field.value = "";
         });

         // Jesli "Miejsce odbioru" jest podawane, niektore pola zostaja ustawione na obowiazkowe
         pickupFields.forEach(field => {
            if (field.id.includes("number")) {
               return; // Pole number ma pozostac nieobowiazkowe
            }
            field.setAttribute("required", "true");
         });

      } // else
   } // togglePickupLocation()

   checkbox.addEventListener("change", handlePickupLocationToggle);

   // Funkcja do ukrywania/pokazywania sekcji "miejsce-odbioru"
   // function togglePickupLocation() {
   //    // Wyszukaj wszystkie inputu miejsca odbioru; inputy ktorych id zaczyna sie od 'add-pickup-'
   //    const pickupFields = document.querySelectorAll("input[id^='add-pickup-']");
   //    if(checkbox.checked) {
   //       // Ukryj sekcje "Miejsce odbioru" i ustaw w polu domyślną wartość
   //       pickupLocationGroup.style.display = "none";
   //       pickupLocation.value = "*Adres*";
   //
   //       // Usun obowiazkowosc pol
   //       pickupFields.forEach(field => {
   //          field.removeAttribute("required");
   //          field.value = "";
   //       });
   //    } // if
   //    else {
   //       pickupLocationGroup.style.display = "block";
   //       pickupLocation.value = "";
   //
   //       // Jesli "Miejsce odbioru" jest podawane, niektore pola zostaja ustawione na obowiazkowe
   //       pickupFields.forEach(field => {
   //          if (field.id.includes("number")) {
   //             return; // Pole number ma pozostac nieobowiazkowe
   //          }
   //          field.setAttribute("required", "true");
   //       });
   //
   //    } // else
   // } // togglePickupLocation()
   //
   // // Wlaczenie obslugi na zmiane checkboxa
   // checkbox.addEventListener("change", togglePickupLocation);
   //
   //
   // // Funkcja do ustawiania pustych pol na null
   // form.addEventListener("submit", function(event)  {
   //    let isValid = true;
   //
   //    // Wyczysc poprzednie bledy
   //    document.querySelectorAll('.error-message').forEach(el => el.textContent = '');
   //
   //    // Pobierz wszystkie inputy
   //    const inputs = form.querySelectorAll('input[type="text"], input[type="number"]');
   //    inputs.forEach(input => {
   //       const value = input.value.trim();
   //       const errorMessageElement = document.getElementById(`error-${input.id}`);
   //
   //       // Jesli wartosc nieobowiazkowych pol jest pusta, ustaw null (nie dziala w bazie)
   //       if (value === "") {
   //          input.value = null;
   //       }
   //
   //       // Walidacja ogolna dla pol numeru i kodu pocztowego
   //       if (input.name === "number" && !/^[A-Za-z0-9\/]+$/.test(value)) {
   //          isValid = false;
   //          errorMessageElement.textContent = "Numer jest niepoprawny";
   //       }
   //       else if (input.name === "postCode" && !/^\d{2}-\d{3}$/.test(value)) {
   //          isValid = false;
   //          errorMessageElement.textContent = "Kod pocztowy musi mieć format ##-###";
   //       }
   //
   //       // Walidacja specyficzna dla "Miejsca odbioru" gdy jest on podawany osobno
   //       if (!checkbox.checked) {
   //          // Numer nie musi byc podany - dlatego w regexie jest na koncu *
   //          if (input.name === "pickupLocation.pickupNumber" && !/^[A-Za-z0-9\/]*$/.test(value)) {
   //             isValid = false;
   //             errorMessageElement.textContent = "Numer jest niepoprawny";
   //          }
   //          else if (input.name === "pickupLocation.pickupPostCode" && !/^\d{2}-\d{3}$/.test(value)) {
   //             isValid = false;
   //             errorMessageElement.textContent = "Kod pocztowy musi mieć format ##-###";
   //          }
   //       }
   //    }); // inputs.forEach
   //
   //    // Zatrzymanie wyslanie formularza jesli dane nie sa poprawne
   //    if (!isValid) {
   //       event.preventDefault();
   //    }
   // });  // form.addEventListener
   //
   // togglePickupLocation();
});