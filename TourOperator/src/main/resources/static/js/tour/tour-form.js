$(document).ready(() => {
    // Ustawienia jezykowe dla kalendarzy
    $.datepicker.regional["pl"] = {
        closeText: "Zamknij",
        prevText: "Poprzedni",
        nextText: "Następny",
        currentText: "Dziś",
        monthNames: ["Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"],
        monthNamesShort: ["Sty", "Lut", "Mar", "Kwi", "Maj", "Cze", "Lip", "Sie", "Wrz", "Paź", "Lis", "Gru"],
        dayNames: ["Niedziela", "Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota"],
        dayNamesShort: ["Nie", "Pon", "Wto", "Śro", "Czw", "Pią", "Sob"],
        dayNamesMin: ["Nd", "Pn", "Wt", "Śr", "Cz", "Pt", "So"],
        weekHeader: "Tydz",
        dateFormat: "dd.mm.yy",
        firstDay: 1,
        isRTL: false, // Czy kalendarzy ma byc od prawej do lewej
        showMonthAfterYear: false,
        yearSuffix: ""
    };
    $.datepicker.setDefaults($.datepicker.regional["pl"]);

    const startDate = $("#startDate");
    const endDate = $("#endDate");
    const finalPaymentDate = $("#finalPaymentDate");
    const noAdvanceCheckbox = $("#noAdvancePayment");
    const advanceDaysInput = $("#numberOfDaysOfAdvancePaymentAfterBooking");
    const numberOfAvailableSeats = $("#numberOfAvailableSeats");
    const numberOfSeatsForMessageLastSeats = $("#numberOfSeatsForMessageLastSeats");

    // Blokowanie pola liczba dni zaliczki
    noAdvanceCheckbox.on("change", function () {
        if ($(this).is(":checked")) {
            advanceDaysInput.prop("disabled", true).val("");
        }
        else {
            advanceDaysInput.prop("disabled", false).val("0");
        }
    });

    const parseDate = (str) => {
        const [day, month, year] = str.split(".");
        return new Date(`${year}-${month}-${day}`);
    };

    const today = new Date();
    const minStartDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());

    // Konfiguracja daty początkowej
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

    // Konfiguracja daty końca
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

    // Termin płatności całości
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

    if (noAdvanceCheckbox.is(":checked")) {
        advanceDaysInput.prop("disabled", true).val("");
    }

    numberOfAvailableSeats.on("input", () => {
        validateSeats();
    })

    numberOfSeatsForMessageLastSeats.on("input", () => {
        validateSeats();
    })


    const validateSeats = () => {
        let available = parseInt(numberOfAvailableSeats.val()) || 0;
        let lastSeats = parseInt(numberOfSeatsForMessageLastSeats.val()) || 0;

        if (lastSeats > available) {
            numberOfSeatsForMessageLastSeats.val(available);
        }
    }
});