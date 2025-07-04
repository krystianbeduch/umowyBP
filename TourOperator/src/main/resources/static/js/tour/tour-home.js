$(document).ready(() => {
    $("#toggle-tour-type").click(() => {
        $("#tour-type-options").fadeToggle();
    });

    $("#tour-type-select").change(function() {
        const selectedUrl = $(this).val();
        if (selectedUrl) {
            window.location.href = selectedUrl;
        }
    });

    $(".delete-tour-btn").click(function()  {
        const button = $(this);
        const tourId = button.data("id");

        if (!confirm("Czy na pewno chcesz usunąć tę wycieczkę")) {
            return;
        }

        $.ajax({
            url: `/tour/individual-one-day/delete/${tourId}`,
            method: "DELETE",
            success: (response) => {
                console.log(response.message);
                button.closest("tr").remove();
                $("#success-message-text").text(response.message);
                $("#success-message-box").show();
                $("#error-message-box").hide();
            },
            error: (xhr) => {
                const response = xhr.responseJSON;
                const errorMsg = response?.error || "Wystąpił błąd podczas usuwania.";

                // Pokaż wiadomość błędu
                $("#error-message-text").text(errorMsg);
                $("#error-message-box").show();
                $("#success-message-box").hide();
            }
        }).always(() => {
            autoHideMessages();
        });
    });

    const autoHideMessages = (delay = 5000) => {
        setTimeout(() => {
            $("#success-message-box").fadeOut();
            $("#error-message-box").fadeOut();
        }, delay);
    };
});