// Funkcja do płynnego zwijania sekcji
export function slideUp(element) {
    element.style.transition = 'height 1s ease, opacity 0.5s ease';
    element.style.height = element.scrollHeight + 'px';  // Ustawienie wysokosci poczatkowej
    requestAnimationFrame(() => {
        element.style.height = '0';
        element.style.opacity = '0';
    });
}

// Funkcja do płynnego rozszerzania sekcji
export function slideDown(element) {
    element.style.opacity = '0'; // Ukryj element
    element.style.display = 'block'; // Ustaw element jako widoczny, ale wysokosc poczatkowa 0
    const height = element.scrollHeight + 'px'; // Obliczanie pelnej wysokości
    element.style.height = '0'; // Ustawienie wysokosci na 0 przed animacją
    element.style.transition = 'height 1s ease, opacity 0.5s ease';
    requestAnimationFrame(() => {
        element.style.height = height; // Dynamiczne ustawienie pelnej wysokosci
        element.style.opacity = '1';
    });
}