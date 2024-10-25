document.addEventListener("DOMContentLoaded", function() {
    const currentUrl = window.location.pathname; // Pobierz akutlalny URL

    const links = document.querySelectorAll('#sidebar-menu a');
    links.forEach(link => {

       if (link.getAttribute('href') === currentUrl) {
           link.classList.add('active');
       }
    });
})