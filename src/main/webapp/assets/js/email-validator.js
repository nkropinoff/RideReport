function initEmailValidation(selector) {
    $(document).ready(function () {
        $(selector).on('blur', function () {
            const emailInput = $(this);
            const emailValue = emailInput.val();

            const form = emailInput.closest('form');
            const submitButton = form.find('button[type="submit"]');

            if (emailValue.trim() === '') {
                emailInput.removeClass('is-invalid');
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
                        emailInput.removeClass('is-invalid');
                        submitButton.prop('disabled', false);
                    } else {
                        emailInput.addClass('is-invalid');
                        submitButton.prop('disabled', true);
                    }
                },

                error: function(jqXHR) {
                    let errorMessage = 'Не удалось выполнить операцию';
                    if (jqXHR.responseText && jqXHR.responseText.trim() !== '') {
                        errorMessage = JSON.parse(jqXHR.responseText).error;
                    }
                    console.error(errorMessage);
                    submitButton.prop('disabled', false);
                }
            });
        });
    });
}
