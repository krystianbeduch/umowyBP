import { setPolishDatepickerLocale } from "../config/datepicker-locale-pl.js";

$(() => {
    setPolishDatepickerLocale(jQuery);

    const today = new Date();
    const eighteenYearsAgo = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());

    const $birthDay = $("#birthDate-0");

    $birthDay.datepicker({
        changeMonth: true,
        changeYear: true,
        yearRange: "1900:" + eighteenYearsAgo.getFullYear(),
        maxDate: eighteenYearsAgo,
        defaultDate: eighteenYearsAgo,
    });

    $(".birth-date").on("keydown paste", e => e.preventDefault());

    // $(".birth-date").not("#birthDate-0").each(function () {
    //     $(this).datepicker({
    //         changeMonth: true,
    //         changeYear: true,
    //         yearRange: "1900:" + today.getFullYear(),
    //         maxDate: today,
    //         defaultDate: today
    //     });
    // });



    let participantIndex = 1;

    $("#reserverNotParticipating").on("change", function () {
        const additionalParticipantsCount = $(".participant").filter((_, el) => {
            return $(el).data("index") >= 1;
        }).length;


        if ($(this).is(":checked") && additionalParticipantsCount === 0) {
            addParticipant();
        }
        // else if (!$(this).is(":checked") && additionalParticipantsCount !== 0) {
        //     // Usuwamy uczestnika z data-index=1, jeśli jest
        //     removeParticipant(1);
        // }
    });

    $("#add-participant").on("click", () => {
        addParticipant();
    });

    const addParticipant = () => {
        const container = $("#participantsContainer");
        const html = `
        <div class="participant border rounded p-3 mb-4 bg-light position-relative" data-index="${participantIndex}">
            <button type="button" class="btn-close position-absolute top-0 end-0 m-2 remove-participant"></button>
            <h5>Uczestnik ${participantIndex + 1}</h5>
            <div class="row g-3">
                 <div class="col-md-6 form-floating">
                    <input type="text" class="form-control" 
                           id="firstName-${participantIndex}" 
                           name="participants[${participantIndex}].firstName" 
                           placeholder="Imię" required>
                    <label for="firstName-${participantIndex}">Imię *</label>
                </div>
                
                <div class="col-md-6 form-floating">
                    <input type="text" class="form-control" 
                           id="lastName-${participantIndex}" 
                           name="participants[${participantIndex}].lastName" 
                           placeholder="Nazwisko" required>
                    <label for="lastName-${participantIndex}">Nazwisko *</label>
                </div>
        
                <div class="col-md-4 form-floating">
                    <input type="text" class="form-control birth-date" 
                           id="birthDate-${participantIndex}" 
                           name="participants[${participantIndex}].birthDate" 
                           placeholder="Data urodzenia"
                           inputmode="none" required>
                    <label for="birthDate-${participantIndex}">Data urodzenia *</label>
                </div>
                
                <div class="col-md-4 form-floating">
                    <input type="email" class="form-control" 
                           id="email-${participantIndex}" 
                           name="participants[${participantIndex}].email" 
                           placeholder="Email">
                    <label for="email-${participantIndex}">Email</label>
                </div>
                
                <div class="col-md-4">
                    <div class="input-group">
                        <div class="form-floating" style="max-width: 90px;">
                            <select class="form-select phone-prefix" id="phonePrefix-${participantIndex}" name="participants[${participantIndex}].phonePrefix">
                                ${generateCountryOptions()}
                            </select>
                            <label for="phonePrefix-0">Kraj *</label>
                        </div>
                        
                        <input type="text" class="form-control" id="phonePrefixDisplay-${participantIndex}" readonly style="min-width: 60px; max-width: 140px;">
                        
                        <div class="form-floating flex-grow-2">
                            <input type="tel" class="form-control" id="phone-${participantIndex}" name="participants[${participantIndex}].phone" placeholder="Telefon" maxlength="22">
                            <label for="phone-${participantIndex}">Telefon</label>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
        `;
        container.append(html);

        const today = new Date();

        // Datepicker dla nowego uczestnika
        const birthDateParticipant = $(`#birthDate-${participantIndex}`);
        birthDateParticipant.datepicker({
            changeMonth: true,
            changeYear: true,
            yearRange: "1900:" + today.getFullYear(),
            maxDate: today,
            defaultDate: today
        });
        birthDateParticipant.on("keydown paste", e => e.preventDefault());

        const newPrefixSelect = $(`#phonePrefix-${participantIndex}`);
        updatePhonePrefixDisplay(newPrefixSelect);

        participantIndex++;
    };

    // Usuwanie uczestnika
    const removeParticipant = (index) => {
        console.log(index);
        const $participantDiv = $(`.participant[data-index="${index}"]`);
        if ($participantDiv.length) {
            $participantDiv.remove();
            // participantIndex--;
            updateParticipantsIndices();
            console.log(participantIndex);
        }
    };
    $("#participantsContainer").on("click", ".remove-participant", function () {
        const index = $(this).closest(".participant").attr("data-index");
        removeParticipant(index);
    });


    $("#reservationForm").on("submit", function (event) {
        if (!$(this)[0].checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }

        let validPhones = true;

        $(".participant").each(function () {
            const index = $(this).attr("data-index");
            const prefix = $(`#phonePrefix-${index}`).val();
            const phoneInput = $(`#phone-${index}`);
            const phoneVal = phoneInput.val().replace(/-/g, "");

            if (prefix === "+48") {
                if (!/^\d{9}$/.test(phoneVal)) {
                    validPhones = false;
                    phoneInput.addClass("is-invalid").removeClass("is-valid");
                }
                else {
                    phoneInput.removeClass("is-invalid");
                }
            }
            else {
                phoneInput.removeClass("is-invalid");
            }
        });

        if (!validPhones) {
            event.preventDefault();
            event.stopPropagation();
        }

        $(this).addClass("was-validated");
    });

    // Aktualizacja prefixu uczestnikow
    const updatePhonePrefixDisplay = (element) => {
        const prefix = $(element).val();
        const id = $(element).attr("id");
        const index = id.split("-")[1];
        $(`#phonePrefixDisplay-${index}`).val(prefix);
    };

    // Aktualizacja prefixu pozostalych uczestnikow
    $(document).on("change", "select.phone-prefix", function () {
        updatePhonePrefixDisplay(this);
    });

    // Aktualizacja przy starcie formularza
    $("select.phone-prefix").each(function () {
        updatePhonePrefixDisplay(this);
    });

    $(document).on("input", "input[type='tel']", function () {
        const $input = $(this);
        const participantDiv = $input.closest(".participant");
        const index = participantDiv.attr("data-index");

        const prefix = $(`#phonePrefix-${index}`).val();
        if (prefix !== "+48") {
            $input.removeClass("is-invalid");
            // $input.next(".invalid-feedback").remove();
            return;
        }

        let input = $(this).val().replace(/\D/g, "");

        if (input.length > 9) {
            input = input.substring(0, 9);
        }

        // Formatowanie numeru z myślnikami
        let formatted = input;

        if (input.length > 6) {
            formatted = input.substring(0, 3) + "-" + input.substring(3, 6) + "-" + input.substring(6);
        }
        else if (input.length > 3) {
            formatted = input.substring(0, 3) + '-' + input.substring(3);
        }

        $input.val(formatted);
        // console.log(formatted);
        //
        // // Walidacja formatu polskiego numeru
        // if ((/^\d{3}-\d{3}-\d{3}$/.test(formatted) || /^\d{9}$/.test(input)) && input.length === 9) {
        //     console.log("sas");
        //     $input.removeClass("is-invalid");
        //     // $input.next(".invalid-feedback").remove();
        // }
        // else {
        //     // if (!$input.next().hasClass("invalid-feedback")) {
        //     //     $input.after('<div class="invalid-feedback d-block">Numer telefonu musi mieć 9 cyfr w formacie 123-456-789.</div>');
        //     // }
        //     console.log("NIEsas");
        //
        //     $input.addClass("is-invalid").removeClass("is-valid");
        // }
    });

    const generateCountryOptions = () => {
        return countries.map(country =>
            `<option value="${country.phoneCode}">${country.commonName}</option>`
        ).join('');
    };

    const updateParticipantsIndices = () => {
        const participants = $(".participant");
        participants.each(function(index) {
            const $participant = $(this);
            const oldIndex = $participant.data("index");

            if (oldIndex !== index) {
                // Aktualizujemy data-index, nagłówka, id, name, for
                $participant.attr("data-index", index);
                $participant.find("h5").text(`Uczestnik ${index + 1}`);
                $participant.find("input, select, label").each(function() {
                    const $el = $(this);

                    if ($el.attr("id")) {
                        const newId = $el.attr("id").replace(/\d+$/, index);
                        $el.attr("id", newId);
                    }

                    if ($el.attr("for")) {
                        const newFor = $el.attr("for").replace(/\d+$/, index);
                        $el.attr("for", newFor);
                    }

                    if ($el.attr("name")) {
                        const newName = $el.attr("name").replace(/\[\d+]/, `[${index}]`);
                        $el.attr("name", newName);
                    }
                });
            }
        });

        participantIndex = participants.length;
    };
});