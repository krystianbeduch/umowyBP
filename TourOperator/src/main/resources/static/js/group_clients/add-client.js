import { handlePickupLocationToggle } from './modules/pickup-location.js';
import { validateForm } from "./modules/form-validation.js";

document.addEventListener("DOMContentLoaded", function() {
   const checkbox = document.getElementById("pickup-location-same-as-address");
   const pickupLocationGroup = document.querySelector(".pickup-location-group");
   const pickupLocation = document.getElementById("add-pickup-location");
   const form = document.getElementById("client-form");

   // Obsluga ukrywania/pokazywania sekcji "miejsce-odbioru" i ustawiania obowiazkowosci pol
   checkbox.addEventListener("change", () =>
       handlePickupLocationToggle("add", pickupLocation, pickupLocationGroup, checkbox));

   // Walidacja wprowadzonych danych
   validateForm(form, checkbox);

   handlePickupLocationToggle("add", pickupLocation, pickupLocationGroup, checkbox);
});