 let slideIndex = 0;
        showSlides(slideIndex);

        function prevSlide() {
            showSlides(slideIndex -= 1);
        }

        function nextSlide() {
            showSlides(slideIndex += 1);
        }

        function showSlides(n) {
            const slides = document.querySelectorAll('.slide');
            if (n >= slides.length) {
                slideIndex = 0;
            }
            if (n < 0) {
                slideIndex = slides.length - 1;
            }
            slides.forEach(slide => slide.style.display = 'none');
            slides[slideIndex].style.display = 'block';
        }