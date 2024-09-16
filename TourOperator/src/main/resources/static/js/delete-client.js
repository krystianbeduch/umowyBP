document.addEventListener("DOMContentLoaded", function() {
    const radioById = document.getElementById("delete-by-id");
    const radioByName = document.getElementById("delete-by-name");
    const clientNumberInput = document.getElementById("delete-client-id");
    const clientNameInput = document.getElementById("delete-name");
    const form = document.getElementById("client-form");
    const errorMessageElement = document.getElementById("error-message");

    // Funkcja blokujaca/odblokowujaca odpowiendie pola
    function toggleDeleteInputs() {
        if (radioById.checked) {
            clientNumberInput.disabled = false;
            clientNameInput.disabled = true;
            clientNameInput.value = "";
        }
        else if (radioByName.checked) {
            clientNumberInput.disabled = true;
            clientNameInput.disabled = false;
            clientNumberInput.value = "";
        }
    }
    radioByName.addEventListener("change", toggleDeleteInputs);
    radioById.addEventListener("change", toggleDeleteInputs);
    toggleDeleteInputs();

    // Funkcja do automatycznego uzupelniania nazwy na podstawie numeru
    clientNumberInput.addEventListener("input", function() {
        if (radioById.checked && clientNumberInput.value) {

            // Wyslij zapytanie do API o dane klienta na podstawie jego numeru
            fetch(`/api/admin/client/${clientNumberInput.value}`)
                .then(response => {
                    // Jesli odpowiedz jest prawidlowa (status OK), zwroc dane w formacie JSON
                    if(response.ok) {
                        // Jesli odpowiedz zawiera dane, zwroc sparsowany JSON, jesli nie zwroc pusty obiekt
                        return response.text().then(text => text ? JSON.parse(text): {})
                    }
                })
                .then(client => {
                    // Jesli klient istnieje i zawiera dane, ustaw nazwe klienta w odpowiednim polu
                    if (client && Object.keys(client).length !== 0) {
                        clientNameInput.value = client.name;
                    }
                    else {
                        clientNameInput.value = "";
                    }
                })
                // Obsluga bledow w przypadku problemow z zapytaniem
                .catch(error => console.error('Error:', error));
        } // if
    }); // clientNumberInput.addEventListener

    // Funkcje do automatycznego uzupelniania numeru klienta na podstawie jego nazwy
    clientNameInput.addEventListener("input", function() {
        if (radioByName.checked && clientNameInput.value) {

            // Pobierz wartsc z pola, usun nadmiarowe spacje i koduj do bezpiecznego formatu URL
            const name = encodeURIComponent(clientNameInput.value.trim());

            // Wyslij zapytanie do API o dane klienta na podstawie jego nazwy
            fetch(`/api/admin/client/name/${name}`)
                .then(response => {
                    // Jesli odpowiedz jest prawidlowa (status OK), zwroc dane w formacie JSON
                    if(response.ok) {
                        // Jesli odpowiedz zawiera dane, zwroc sparsowany JSON, jesli nie zwroc pusty obiekt
                        return response.text().then(text => text ? JSON.parse(text) : {})
                    }
                })
                .then(client => {
                    // Jesli klient istnieje i zawiera dane, ustaw nazwe klienta w odpowiednim polu
                    if (client && Object.keys(client).length !== 0) {
                        clientNumberInput.value = client.clientNumber;
                    }
                    else {
                        clientNumberInput.value = "";
                    }
                })
                // Obsluga bledow w przypadku problemow z zapytaniem
                .catch(error => console.error('Error:', error));
        }
    });



    // przekierowanie formularza
    form.addEventListener("submit", function(event) {
       if (clientNumberInput.value) {
           form.action= `/admin/form/delete/${clientNumberInput.value}`
       }
       else {
           event.preventDefault();
           alert("Nie podano numeru");
       }
    });

    // Wyslij zapytanie API aby pobrac maksymalny numer klienta w bazie
    fetch('/api/admin/client/max-client-number')
        .then(response => response.json())
        .then(maxClientNumber => {
            if (maxClientNumber > 0) {
                // Ustaw maksymalna wartosc pola
                clientNumberInput.setAttribute('max', maxClientNumber);

                // Dodaj nasluchiwanie zdarzenia 'input' aby nie pozwolic na wpisanie wartosci powyzej maxa
                clientNumberInput.addEventListener("input", function() {
                    const value = parseInt(clientNumberInput.value, 10);
                    if (value > maxClientNumber) {
                        clientNumberInput.value = maxClientNumber;
                        console.error(errorMessageElement);
                        errorMessageElement.textContent = `Numer klienta nie może przekraczać ${maxClientNumber}`;
                    }
                })
            }
            else {
                // Jesli brak klientow w bazie, wylacz pole
                clientNumberInput.setAttribute('disabled', true);
                alert("Brak klientów w bazie")
            }
        })
        // Obsluga bledu podczas pobierania maksymalnego numeru klienta
        .catch(error => console.error('Błąd podczas pobierania maksymalnego numeru klienta'));
});