import { setPolishDatepickerLocale } from "../config/datepicker-locale-pl.js";

$(() => {
    // // Ustawienia jezykowe dla kalendarzy
    // $.datepicker.regional["pl"] = {
    //     closeText: "Zamknij",
    //     prevText: "Poprzedni",
    //     nextText: "Następny",
    //     currentText: "Dziś",
    //     monthNames: ["Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"],
    //     monthNamesShort: ["Sty", "Lut", "Mar", "Kwi", "Maj", "Cze", "Lip", "Sie", "Wrz", "Paź", "Lis", "Gru"],
    //     dayNames: ["Niedziela", "Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota"],
    //     dayNamesShort: ["Nie", "Pon", "Wto", "Śro", "Czw", "Pią", "Sob"],
    //     dayNamesMin: ["Nd", "Pn", "Wt", "Śr", "Cz", "Pt", "So"],
    //     weekHeader: "Tydz",
    //     dateFormat: "dd.mm.yy",
    //     firstDay: 1,
    //     isRTL: false, // Czy kalendarzy ma byc od prawej do lewej
    //     showMonthAfterYear: false,
    //     yearSuffix: ""
    // };
    // $.datepicker.setDefaults($.datepicker.regional["pl"]);
    setPolishDatepickerLocale(jQuery);


    const startDate = $("#startDate");
    const endDate = $("#endDate");
    const finalPaymentDate = $("#finalPaymentDate");
    const noAdvanceCheckbox = $("#noAdvancePayment");
    const advanceDaysInput = $("#numberOfDaysOfAdvancePaymentAfterBooking");
    const numberOfAvailableSeats = $("#numberOfAvailableSeats");
    const numberOfSeatsForMessageLastSeats = $("#numberOfSeatsForMessageLastSeats");
    const variantContainer = $("#variant-container");
    let variantIndex = 0;

    const parseDate = (str) => {
        const [day, month, year] = str.split(".");
        return new Date(Number(year), Number(month) - 1, Number(day));
    };

    const calculateBirthDate = (age) => {
        const today = new Date();
        return new Date(today.getFullYear() - age, today.getMonth(), today.getDate());
    };

    const validateSeats = () => {
        let available = parseInt(numberOfAvailableSeats.val()) || 0;
        let lastSeats = parseInt(numberOfSeatsForMessageLastSeats.val()) || 0;

        if (lastSeats > available) {
            numberOfSeatsForMessageLastSeats.val(available);
        }
    };

    const updateBirthDates = ($block) => {
        const $ageFromInput = $block.find(".age-from");
        const $ageToInput = $block.find(".age-to");

        const ageFrom = parseInt($ageFromInput.val());
        const ageTo = parseInt($ageToInput.val());

        $ageFromInput.removeClass("is-invalid");
        $ageToInput.removeClass("is-invalid")

        // Walidacja: przynajmniej jedno pole musi być wypełnione
        const isAgeFromValid = !Number.isNaN(ageFrom);
        const isAgeToValid = !Number.isNaN(ageTo);

        if (!isAgeFromValid && !isAgeToValid) {
            $ageFromInput.addClass("is-invalid");
            $ageToInput.addClass("is-invalid");
            $block.find(".birth-date-from, .birth-date-to").val("");
            return;
        }

        // Walidacja: ageFrom nie może być większy niż ageTo
        if (isAgeFromValid && isAgeToValid && ageFrom > ageTo) {
            $ageFromInput.addClass("is-invalid");
            $ageToInput.addClass("is-invalid");
            $block.find(".birth-date-from, .birth-date-to").val("");
            return;
        }

        const fromDate = isAgeToValid ? calculateBirthDate(ageTo) : null;
        const toDate = isAgeFromValid ? calculateBirthDate(ageFrom) : null;

        $block.find(".birth-date-from").val(fromDate ? fromDate.toLocaleDateString() : "");
        $block.find(".birth-date-to").val(toDate ? toDate.toLocaleDateString() : "");
    };

    // Inicjalizacja datepicker
    const today = new Date();
    const minStartDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());

    startDate.datepicker({
        minDate: minStartDate,
        dateFormat: "dd.mm.yy",
        onClose: function (selectedDate) {
            if (selectedDate) {
                const parsed = parseDate(selectedDate);
                const nextDay = new Date(parsed);
                nextDay.setDate(parsed.getDate() + 1);
                endDate.datepicker("option", "minDate", nextDay);
                finalPaymentDate.datepicker("option", "maxDate", parsed);
            }
        }
    });

    endDate.datepicker({
        minDate: minStartDate,
        dateFormat: "dd.mm.yy",
        onClose: function (selectedDate) {
            const startVal = startDate.val();
            if (startVal && selectedDate) {
                const start = parseDate(startVal);
                const end = parseDate(selectedDate);
                if (end <= start) {
                    alert("Data końca musi być późniejsza niż data początku.");
                    $(this).val("");
                }
            }
        }
    });

    finalPaymentDate.datepicker({
        minDate: minStartDate,
        dateFormat: "dd.mm.yy",
        onClose: function (selectedDate) {
            const startVal = startDate.val();
            if (startVal && selectedDate) {
                const start = parseDate(startVal);
                const payment = parseDate(selectedDate);
                if (payment > start) {
                    alert("Termin płatności całości nie może być późniejszy niż data początku wyjazdu.");
                    $(this).val("");
                }
            }
        }
    });

    // Blokowanie pola liczba dni zaliczki
    noAdvanceCheckbox.on("change", function () {
        if ($(this).is(":checked")) {
            advanceDaysInput.prop("disabled", true).val("");
        }
        else {
            advanceDaysInput.prop("disabled", false).val("0");
        }
    });

    if (noAdvanceCheckbox.is(":checked")) {
        advanceDaysInput.prop("disabled", true).val("");
    }

    numberOfAvailableSeats.on("input", validateSeats);
    numberOfSeatsForMessageLastSeats.on("input", validateSeats);


    // Obsługa wariantów cenowych
    const addPriceVariant = () => {
        const html = `
        <div class="variant-block border p-3 mb-3 rounded" data-index="${variantIndex}">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <strong>Wariant</strong>
                <button type="button" class="btn btn-danger btn-sm remove-variant">Usuń</button>
            </div>

            <div class="mb-3">
                <label class="form-label">Nazwa wariantu</label>
                <input type="text" name="priceVariants[${variantIndex}].name" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Cena wariantu</label>
                <input type="number" name="priceVariants[${variantIndex}].price" class="form-control" required>
            </div>

            <div class="row mb-3">
                <div class="col">
                    <label class="form-label">Wiek od</label>
                    <input type="number" name="priceVariants[${variantIndex}].ageFrom" class="form-control age-from" min="0">
                    <div class="invalid-feedback">Wypełnij przynajmniej jedno pole i upewnij się, że <strong>wiek od</strong> ≤ <strong>wiek do</strong>.</div>
                </div>
                <div class="col">
                    <label class="form-label">Wiek do</label>
                    <input type="number" name="priceVariants[${variantIndex}].ageTo" class="form-control age-to" min="0">
                    <div class="invalid-feedback">Wypełnij przynajmniej jedno pole i upewnij się, że <strong>wiek od</strong> ≤ <strong>wiek do</strong>.</div>
                </div>
            </div>

            <div class="row mb-2">
                <div class="col">
                    <label class="form-label">Data urodzenia do</label>
                    <input type="text" class="form-control birth-date-to" readonly>
                </div>
                <div class="col">
                    <label class="form-label">Data urodzenia od</label>
                    <input type="text" class="form-control birth-date-from" readonly>
                </div>
            </div>
        </div>
    `;

        variantContainer.append(html);
        variantIndex++;
    };

    $("#add-variant-button").on("click",addPriceVariant);

    variantContainer.on("click", ".remove-variant", function () {
        $(this).closest('.variant-block').remove();
    });

    variantContainer.on("input", ".age-from, .age-to", function () {
        const $block = $(this).closest('.variant-block');
        updateBirthDates($block);
    });

    $("form").on("submit", function (e) {
        let isValid = true;

        $(".is-invalid").removeClass("is-invalid");

        $(".variant-block").each(function () {
            const $block = $(this);
            const ageFrom = parseInt($block.find(".age-from").val());
            const ageTo = parseInt($block.find(".age-to").val());

            const isAgeFromValid = !Number.isNaN(ageFrom);
            const isAgeToValid = !Number.isNaN(ageTo);

            if (!isAgeFromValid && !isAgeToValid) {
                $block.find(".age-from, .age-to").addClass("is-invalid");
                isValid = false;
            }
            else if (isAgeFromValid && isAgeToValid && ageFrom > ageTo) {
                $block.find(".age-from, .age-to").addClass("is-invalid");
                isValid = false;
            }
        });

        // const startVal = startDate.val();
        // const endVal = endDate.val();
        // const finalPayVal = finalPaymentDate.val();
        //
        // if (!startVal) {
        //     startDate.addClass("is-invalid");
        //     // messages.push("Pole Data początku jest wymagane.");
        //     isValid = false;
        // }
        // else {
        //     const start = parseDate(startVal);
        //     // if (start < minStartDate) {
        //     //     messages.push("Data początku nie może być wcześniejsza niż dzisiejsza data.");
        //     //     isValid = false;
        //     // }
        //     if (start < today) {
        //         startDate.addClass("is-invalid");
        //         isValid = false;
        //     }
        //
        //     if (endVal) {
        //         const end = parseDate(endVal);
        //         if (end <= start) {
        //             endDate.addClass("is-invalid");
        //             isValid = false;
        //         }
        //     }
        //
        //     // if (finalPayVal) {
        //     //     const finalPay = parseDate(finalPayVal);
        //     //     if (finalPay > start) {
        //     //
        //     //         messages.push("Termin płatności całości nie może być późniejszy niż data początku wyjazdu.");
        //     //         isValid = false;
        //     //     }
        //     //     if (finalPay < minStartDate) {
        //     //         messages.push("Termin płatności całości nie może być wcześniejszy niż dzisiejsza data.");
        //     //         isValid = false;
        //     //     }
        //     // }
        //     if (finalPayVal) {
        //         const finalPay = parseDate(finalPayVal);
        //         if (finalPay > start || finalPay < today) {
        //             finalPaymentDate.addClass("is-invalid");
        //             isValid = false;
        //         }
        //     }
        // }

        if (!isValid) {
            e.preventDefault();
        }
    });
});