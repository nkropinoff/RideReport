document.addEventListener('DOMContentLoaded', function () {
    const errorAlert = document.querySelector('.alert.border-danger');
    if (errorAlert) {
        const formInputs = document.querySelectorAll('.card-body form input');

        const hideAlert = () => {
            $(errorAlert).fadeOut(300);
        };

        formInputs.forEach(input => {
            input.addEventListener('focus', hideAlert, { once: true });
        });
    }
});
