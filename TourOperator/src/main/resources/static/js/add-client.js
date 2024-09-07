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
   const postCodeInput = document.getElementById("add-post-code");
   const numberInput = document.getElementById("add-number");
   const pickupNumber = document.getElementById("add-pickup_number");
   // Funkcja do ustawiania pustych pol na null
   form.addEventListener("submit", function(event)  {
      let isValid = true;

      // Wyczysz poprzednie bledy
      document.querySelectorAll('.error-message').forEach(el => el.textContent = '');

      // if (!validatePostCode(postCodeInput.value)) {
      //    alert("Kod pocztowy musi mieć format ##-###");
      //    isValid = false;
      // }
      // if (!validateNumber(numberInput.value)) {
      //    alert("Numer jest niepoprawny");
      //    isValid = false;
      // }

      const inputs = form.querySelectorAll('input[type="text"], input[type="number"]');
      // alert("siem");
      inputs.forEach(input => {
         const value = input.value.trim();
         // alert(input.id);
         const errorMessageElement = document.getElementById(`error-${input.id}`);

         if (value === "") {
            input.value = null;
         }
         else {
            // alert(input.name);
            console.log("name " + input.name);
            console.log(!validatePostCode(value));

            if (input.name === "postCode" && !/^\d{2}-\d{3}$/.test(value)) {
               isValid = false;
               errorMessageElement.textContent = "Kod pocztowy musi mieć format ##-###";
               // alert("Kod")
            }
            else if (input.name === "number" && !/^[A-Za-z0-9\/]+$/.test(value)) {
               isValid = false;
               errorMessageElement.textContent = "Numer jest niepoprawny";
               // alert("Numer");
            }
            else if (input.name === "pickupLocation.pickupNumber" && !/^[A-Za-z0-9\/]*$/.test(value)) {
               isValid = false;
               errorMessageElement.textContent = "Numer jest niepoprawny";
            }
         }
      });

      // Zatrzymanie wyslanie formularza jest dane nie sa poprawne
      if (!isValid) {
         event.preventDefault();
      }
   });

   function validatePostCode(postCode) {
      const postCodePattern = /^\d{2}-\d{3}$/; // ##-###
      return postCodePattern.test(postCode);
   }
   function validateNumber(number) {
      const numberPattern = /^[A-Za-z0-9\/]+$/
      return numberPattern.test(number);
   }


   togglePickupLocation();
});