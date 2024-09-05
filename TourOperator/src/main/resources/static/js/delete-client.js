document.addEventListener("DOMContentLoaded", function() {
    const radioById = document.getElementById("delete-by-id");
    const radioByName = document.getElementById("delete-by-name");
    const clientIdInput = document.getElementById("delete-client-id");
    const clientNameInput = document.getElementById("delete-name")

    // Funkcja blokujaca/odblokowujaca odpowiendie pola
    function toggleDeleteInputs() {
        if (radioById.checked) {
            clientIdInput.disabled = false;
            clientNameInput.disabled = true;
            clientNameInput.value = "";
        }
        else if (radioByName.checked) {
            clientIdInput.disabled = true;
            clientNameInput.disabled = false;
            clientIdInput.value = "";
        }

    }
    radioByName.addEventListener("change", toggleDeleteInputs);
    radioById.addEventListener("change", toggleDeleteInputs);

    toggleDeleteInputs();

    
    // Funkcja do automatycznego uzupelniania nazwy na podstawie numeru
    clientIdInput.addEventListener("input", function() {
        if (radioById.checked && clientIdInput.value) {
        fetch(`/api/client/${clientIdInput.value}`)
            .then(response => response.json())
            .then(client => {
                if (client) {
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
                        clientIdInput.value = client.clientNumber;
                    }
                    else {
                        clientIdInput.value = "";
                    }
                })
                .catch(error => console.error('Error:', error));
        }
    });

});