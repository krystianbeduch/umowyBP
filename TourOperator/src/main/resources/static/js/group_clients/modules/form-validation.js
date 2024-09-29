export function validateForm(form, checkbox) {
    form.addEventListener("submit", function (event) {
        let isValid = true;

        // Wyczysc poprzednie bledy
        document.querySelectorAll('.error-message').forEach(el => el.textContent = '');

        // Pobierz wszystkie inputy
        const inputs = form.querySelectorAll('input[type="text"], input[type="number"]');
        inputs.forEach(input => {
            const value = input.value.trim();
            const errorMessageElement = document.getElementById(`error-${input.id}`);

            // Jesli wartosc nieobowiazkowych pol jest pusta, ustaw null (nie dziala w bazie)
            // if (value === "") {
            //    input.value = null;
            // }

            // Walidacja ogolna dla pol numeru i kodu pocztowego
            if (input.name === "number" && !/^[A-Za-z0-9\/]+$/.test(value)) {
                isValid = false;
                errorMessageElement.textContent = "Numer jest niepoprawny";
            } else if (input.name === "postCode" && !/^\d{2}-\d{3}$/.test(value)) {
                isValid = false;
                errorMessageElement.textContent = "Kod pocztowy musi mieć format ##-###";
            }

            // Walidacja specyficzna dla "Miejsca odbioru" gdy jest on podawany osobno
            if (!checkbox.checked) {
                // Numer nie musi byc podany - dlatego w regexie jest na koncu *
                if (input.name === "pickupLocation.pickupNumber" && !/^[A-Za-z0-9\/]*$/.test(value)) {
                    isValid = false;
                    errorMessageElement.textContent = "Numer jest niepoprawny";
                } else if (input.name === "pickupLocation.pickupPostCode" && !/^\d{2}-\d{3}$/.test(value)) {
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
}