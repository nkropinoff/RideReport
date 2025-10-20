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

$(document).ready(function () {
    $('.email-check').on('blur', function () {
       const emailInput = $(this);
       const emailValue = emailInput.val();

       const form = emailInput.closest('form');
       const submitButton = form.find('button[type="submit"]');

       if (emailValue.trim() === '') {
           submitButton.prop('disabled', false);
           return;
       }

       $.ajax({
           url: ctx + '/api/check-email',
           method: 'GET',
           data: {email: emailValue},
           dataType: 'json',

           success: function(response) {
               if (response.isAvailable) {
                   emailInput.removeClass('is-invalid')
                   submitButton.prop('disabled', false);
               } else {
                   emailInput.addClass('is-invalid');
                   submitButton.prop('disabled', true);
               }
           },

           error: function() {
               console.error('Failed email check.');
               submitButton.prop('disabled', false);
           }
       })
    });
});