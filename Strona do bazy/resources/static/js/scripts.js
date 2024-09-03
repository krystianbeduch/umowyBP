document.addEventListener("DOMContentLoaded", function () {
   const checkbox = document.getElementById("sameAsAddress");
   const pickupLocationGroup = document.querySelector(".pickup-location-group");

   // Funkcja do ukrywania/pokazywania sekcji "miejsce-odbioru"
   function togglePickupLocation() {
      if(checkbox.checked) {
         pickupLocationGroup.style.display = "none";
      }
      else {
         pickupLocationGroup.style.display = "block";
      }
   }

   // Wlaczenie obslugi na zmiane checkboxa
   checkbox.addEventListener("change", togglePickupLocation);

   togglePickupLocation();
});