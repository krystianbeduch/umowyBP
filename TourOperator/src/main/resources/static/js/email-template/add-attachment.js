document.addEventListener("DOMContentLoaded", () => {
    const attachmentList = document.getElementById("attachment-list");
    const newAttachmentInput = document.getElementById("new-attachment");
    const addAttachmentButton = document.getElementById("add-attachment-button");

    // Zdarzenie przycisku dodawnania zalacznika
    addAttachmentButton.addEventListener("click", () => {
        const files = newAttachmentInput.files;
        // console.log(file);
        // Sprawdzamy czy wybrano pliki
        if (files.length > 0 ) {
            for (let i = 0; i < files.length; i++) {
                addAttachmentToList(files[i]);

            }
            // console.log(file);
            newAttachmentInput.value = "";
        }
        else {
            alert("Proszę wybrać plik do dodania")
        }
    });

    const addAttachmentToList = (file) => {
        const listItem = document.createElement("li");
        const link = document.createElement("a");
        link.href = URL.createObjectURL(file);
        link.target = "_blank";
        link.textContent = file.name;

        const inputFile = document.createElement("input");
        inputFile.type = "file";
        inputFile.name = "attachments"
        inputFile.files = file;
        inputFile.accept = 'accept="application/pdf"';

        // inputFile.style.display = "none";

        const deleteButton = document.createElement("button");
        deleteButton.type = "button";
        deleteButton.textContent = "Usuń załącznik";
        deleteButton.addEventListener("click", () => {
           attachmentList.remove(listItem);
        });

        listItem.appendChild(link);
        listItem.appendChild(inputFile);
        listItem.appendChild(deleteButton);
        attachmentList.appendChild(listItem);
    };
});