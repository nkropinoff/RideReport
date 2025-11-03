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