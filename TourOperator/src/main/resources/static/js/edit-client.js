import { handlePickupLocationToggle } from './modules/pickup-location.js';
import { validateForm } from "./modules/form-validation.js";
import { slideDown, slideUp } from "./modules/animations.js";

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

   // Uzupelnienie formularza na podstawie wybranego klienta z tabeli
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

      fetch(`/api/admin/client/${clientNumber}`)
          .then(response => response.json())
          .then(client => {
             if (client) {
                populateForm(client);
                if (client.pickupLocation.pickupLocation === "*Adres*") {
                   checkbox.checked = true;
                   // pickupLocationGroup.style.display = "none";
                   slideUp(pickupLocationGroup);
                   // pickupLocationGroup.classList.add("fade-out");
                   // pickupLocationGroup.classList.remove("fade-in");
                }
                else {
                   checkbox.checked = false;
                   // pickupLocationGroup.style.display = "block";
                   slideDown(pickupLocationGroup);
                   // pickupLocationGroup.classList.add("fade-in");
                   // pickupLocationGroup.classList.remove("fade-out");
                }
             }
             else {
                alert("Klient nie znaleziony");
             }
          })
          .catch(error => console.error("Błąd: ", error));
   } // selectClient()

   function populateForm(client) {
      const fields = {
         'edit-client-id' : client.clientNumber,
         'edit-name' : client.name,
         'edit-alias' : client?.alias || '',
         'edit-street': client.street,
         'edit-number': client.number,
         'edit-post-code': client.postCode,
         'edit-city': client.city,
         'edit-pickup-location': client.pickupLocation?.pickupLocation || '',
         'edit-pickup-street': client.pickupLocation?.pickupStreet || '',
         'edit-pickup-number': client.pickupLocation?.pickupNumber || '',
         'edit-pickup-post-code': client.pickupLocation?.pickupPostCode || '',
         'edit-pickup-city': client.pickupLocation?.pickupCity || ''
      };

      Object.keys(fields).forEach(id => {
         const element = document.getElementById(id);
         if (element) {
            element.value = fields[id];
         }
      });
   } // populateForm()

   // Obsluga ukrywania/pokazywania sekcji "miejsce-odbioru" i ustawiania obowiazkowosci pol
   checkbox.addEventListener("change", () =>
       handlePickupLocationToggle("edit", pickupLocation, pickupLocationGroup, checkbox));

   // Walidacja wprowadzonych danych
   validateForm(form, checkbox);

});