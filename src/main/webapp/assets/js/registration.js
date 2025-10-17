document.addEventListener('DOMContentLoaded', function () {
    const params = new URLSearchParams(window.location.search);
    const role = params.get("role");

    if (role === 'company') {
        const companyTab = document.getElementById('company-tab');
        if (companyTab) companyTab.click();
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