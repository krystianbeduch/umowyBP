$(document).ready(() => {
    $('#toggle-tour-type').click(() => {
        $('#tour-type-options').fadeToggle();
    });

    $('#tour-type-select').change(function() {
        const selectedUrl = $(this).val();
        if (selectedUrl) {
            window.location.href = selectedUrl;
        }
    });
});