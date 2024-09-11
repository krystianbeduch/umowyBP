import { handlePickupLocationToggle } from './modules/pickup-location.js';
import { validateForm } from "./modules/form-validation.js";

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

      fetch(`/api/client/${clientNumber}`)
          .then(response => response.json())
          .then(client => {
             if (client) {
                populateForm(client);
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
      document.getElementById('edit-alias').value = client?.alias || '';
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

   // Obsluga ukrywania/pokazywania sekcji "miejsce-odbioru" i ustawiania obowiazkowosci pol
   checkbox.addEventListener("change", () =>
       handlePickupLocationToggle("edit", pickupLocation, pickupLocationGroup, checkbox));

   // Walidacja wprowadzonych danych
   validateForm(form, checkbox);

});