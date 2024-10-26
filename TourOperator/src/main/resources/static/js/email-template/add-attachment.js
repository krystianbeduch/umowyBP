document.addEventListener("DOMContentLoaded", () => {
    const attachmentList = document.getElementById("attachment-list");
    const newAttachmentInput = document.getElementById("new-attachment");
    const addAttachmentButton = document.getElementById("add-attachment-button");

    addAttachmentButton.addEventListener("click", () => {
        newAttachmentInput.click();
    });

    newAttachmentInput.addEventListener("change", () => {
        Array.from(newAttachmentInput.files).forEach(file => {
            addAttachmentToList(file);
        });
    });


    const addAttachmentToList = (file) => {
        const listItem = document.createElement("li");

        // Tworzymy link do załącznika
        const link = document.createElement("a");
        link.href = URL.createObjectURL(file);
        link.target = "_blank";
        link.textContent = file.name;

        // Tworzymy ukryty input, który przekaże załącznik do Springa
        ///????????????????
        // const inputFile = document.createElement("input");
        // inputFile.type = "hidden";
        // inputFile.name = "attachments";
        // inputFile.value = file.name; // Użyjemy nazwy pliku jako placeholdera
        ////////////


        const deleteButton = document.createElement("button");
        deleteButton.type = "button";
        deleteButton.textContent = "Usuń załącznik";
        deleteButton.classList.add("delete-attachment-button");
        deleteButton.addEventListener("click", () => {
            listItem.remove();
        });

        // class="delete-attachment-button"

        listItem.appendChild(link);
        // listItem.appendChild(inputFile);
        listItem.appendChild(deleteButton);
        attachmentList.appendChild(listItem);
    };
    // // Zdarzenie przycisku dodawnania zalacznika
    // addAttachmentButton.addEventListener("click", () => {
    //     const files = newAttachmentInput.files;
    //     // console.log(file);
    //     // Sprawdzamy czy wybrano pliki
    //     if (files.length > 0 ) {
    //         for (let i = 0; i < files.length; i++) {
    //             addAttachmentToList(files[i]);
    //
    //         }
    //         // console.log(file);
    //         newAttachmentInput.value = "";
    //     }
    //     else {
    //         alert("Proszę wybrać plik do dodania")
    //     }
    // });
    //
    // // const addAttachmentToList = (file) => {
    // //     const listItem = document.createElement("li");
    // //     const link = document.createElement("a");
    // //     link.href = URL.createObjectURL(file);
    // //     link.target = "_blank";
    // //     link.textContent = file.name;
    // //
    // //     const inputFile = document.createElement("input");
    // //     inputFile.type = "hidden";
    // //     inputFile.name = "attachments"
    // //     // inputFile.files = [file];
    // //     inputFile.value = file.name;
    // //     // inputFile.accept = 'accept="application/pdf"';
    // //
    // //     // inputFile.style.display = "none";
    // //
    // //     const deleteButton = document.createElement("button");
    // //     deleteButton.type = "button";
    // //     deleteButton.textContent = "Usuń załącznik";
    // //     deleteButton.addEventListener("click", () => {
    // //        attachmentList.remove(listItem);
    // //     });
    // //
    // //     listItem.appendChild(link);
    // //     listItem.appendChild(inputFile);
    // //     listItem.appendChild(deleteButton);
    // //     attachmentList.appendChild(listItem);
    // // };
});