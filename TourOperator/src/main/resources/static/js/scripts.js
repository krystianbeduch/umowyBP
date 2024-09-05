document.addEventListener("DOMContentLoaded", function() {
   const checkbox = document.getElementById("pickup-location-same-as-address");
   const pickupLocationGroup = document.querySelector(".pickup-location-group");
   const pickupLocation = document.getElementById("add-pickup_location");

   // Funkcja do ukrywania/pokazywania sekcji "miejsce-odbioru"
   function togglePickupLocation() {
      if(checkbox.checked) {
         pickupLocationGroup.style.display = "none";
         pickupLocation.value = "*Adres*";
      }
      else {
         pickupLocationGroup.style.display = "block";
         pickupLocation.value = "";
      }
   }

   // Wlaczenie obslugi na zmiane checkboxa
   checkbox.addEventListener("change", togglePickupLocation);

//////.//////////////
   const form = document.getElementById("client-form");
   // Funkcja do ustawiania pustych pol na null
   function handleFormSubmission(event) {
      const inputs = form.querySelectorAll('input[type="text"], input[type="number"]');
      inputs.forEach(input => {
         if (input.value.trim() === "") {
           // input.value = null;
            console.log(`Pole '${input.name}' jest puste.`);
         }
      });
      console.log('Form submitted');
   }

   // Wlaczenie obslugi wyslania formularza
   form.addEventListener("submit", handleFormSubmission);
//////////////////////////

   togglePickupLocation();
});