let variantIndex = 0;
$(document).ready(() => {

    $("#add-variant-button").click(() => {
        addPriceVariant();
    });

    const variantContainer = $("#variant-container");

    // Delegowanie zdarzenia usuwania, ponieważ elementy są dodawane dynamicznie
    variantContainer.on("click", ".remove-variant", function () {
        $(this).closest('.variant-block').remove();
    });

    // Delegowanie zdarzenia obliczania daty na podstawie wieku
    variantContainer.on("input", ".age-from, .age-to", function () {
        const $block = $(this).closest('.variant-block');
        updateBirthDates($block);
    });

    const calculateBirthDate = (age) => {
        const today = new Date();
        return new Date(today.getFullYear() - age, today.getMonth(), today.getDate());
    };

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
    }

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
    }

    $("form").on("submit", function (e) {
        let isValid = true;

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

        if (!isValid) {
            e.preventDefault();
        }
    });
});