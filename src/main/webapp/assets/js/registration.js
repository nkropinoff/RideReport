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

    const fileInput = document.getElementById('companyDocuments');
    const fileListContainer = document.getElementById('fileList');
    const maxFiles = 4;
    const maxFileSize = 10 * 1024 * 1024;

    if (fileInput) {
        fileInput.addEventListener('change', function () {
            fileListContainer.innerHTML = '';

            if (this.files.length > maxFiles) {
                alert(`Вы можете загрузить не более ${maxFiles} файлов.`);
                this.value = '';
                return;
            }

            for (let i = 0; i < this.files.length; i++) {
                const file = this.files[i];

                if (file.size > maxFileSize) {
                    alert(`Файл "${file.filename}" слишком большой (максимальный размер 10МБ).`);
                    this.value = '';
                    fileListContainer.innerHTML = '';
                    return;
                }

                const fileItem = document.createElement('div');
                fileItem.className = 'file-list-item';

                const fileNameSpan = document.createElement('span');
                fileNameSpan.textContent = file.name;

                const fileSizeSpan = document.createElement('span');
                fileSizeSpan.className = 'file-size';
                fileSizeSpan.textContent = `${(file.size / 1024 / 1024).toFixed(2)} МБ`;

                fileItem.appendChild(fileNameSpan);
                fileItem.appendChild(fileSizeSpan);

                fileListContainer.appendChild(fileItem);
            }
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

           error: function(jqXHR) {
               let errorMessage = 'Не удалось выполнить операцию';
               if (jqXHR.responseText && jqXHR.responseText.trim() !== '') {
                   errorMessage = JSON.parse(jqXHR.responseText).error;
               }
               console.error(errorMessage);
               submitButton.prop('disabled', false);
           }
       })
    });
});