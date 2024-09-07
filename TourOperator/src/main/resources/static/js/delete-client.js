document.addEventListener("DOMContentLoaded", function() {
    const radioById = document.getElementById("delete-by-id");
    const radioByName = document.getElementById("delete-by-name");
    const clientNumberInput = document.getElementById("delete-client-id");
    const clientNameInput = document.getElementById("delete-name")

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
        fetch(`/api/client/${clientNumberInput.value}`)
            .then(response => {
                if(response.ok) {
                    return response.text().then(text => text ? JSON.parse(text): {})
                }
            })
            .then(client => {
                if (client && Object.keys(client).length !== 0) {
                    clientNameInput.value = client.name;
                }
                else {
                    clientNameInput.value = "";
                }
            })
            .catch(error => console.error('Error:', error));
        }
    });

    // Funkcje do automatycznego uzupelniania numeru na podstawie nazwy
    clientNameInput.addEventListener("input", function() {
        if (radioByName.checked && clientNameInput.value) {
            const name = encodeURIComponent(clientNameInput.value.trim());
            fetch(`/api/client/name/${name}`)
                .then(response => response.json())
                .then(client => {
                    if (client) {
                        clientNumberInput.value = client.clientNumber;
                    }
                    else {
                        clientNumberInput.value = "";
                    }
                })
                .catch(error => console.error('Error:', error));
        }
    });

    const form = document.getElementById("client-form");

    // przekierowanie formularza
    form.addEventListener("submit", function(event) {
       if (clientNumberInput.value) {
           form.action= `/form/delete/${clientNumberInput.value}`
       }
       else {
           event.preventDefault();
           alert("Nie podano numeru");
       }
    });

    // Wyslij zapytanie API aby pobrac maksymalny numer klienta w bazie
    fetch('/api/client/max-client-number')
        .then(response => response.json())
        .then(maxClientNumber => {
            if (maxClientNumber > 0) {
                // ustaw maksymalna wartosc pola
                clientNumberInput.setAttribute('max', maxClientNumber);
            }
            else {
                // jesli brak klientow w bazie, wylacz pole
                clientNumberInput.setAttribute('disabled', true);
                alert("Brak klientów w bazie")
            }
        })
        .catch(error => console.error('Błąd podczas pobierania maksymalnego numeru klienta'));





});