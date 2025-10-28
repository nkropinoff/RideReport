var TableUtils = (function() {

    function escapeHtml(text) {
        const map = {'&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;'};
        return String(text).replace(/[&<>"']/g, m => map[m]);
    }

    function showNotification(message, type) {
        const alertHtml = `
            <div class="alert alert-${type} alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3" 
                 style="z-index: 9999; min-width: 300px;" role="alert">
                ${escapeHtml(message)}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
        $('body').append(alertHtml);
        setTimeout(() => $('.alert').fadeOut(400, function() { $(this).remove(); }), 4000);
    }

    function showConfirmModal(text, onConfirm) {
        $('#confirmActionText').text(text);
        $('#confirmActionBtn').off('click').on('click', onConfirm);
        const modal = new bootstrap.Modal(document.getElementById('confirmActionModal'));
        modal.show();
    }

    function renderPagination(totalPages, currentPage, $container) {
        if (totalPages <= 1) {
            $container.html('');
            return;
        }

        let html = '<ul class="pagination justify-content-center">';
        const prevDisabled = currentPage === 1 ? 'disabled' : '';
        html += `<li class="page-item ${prevDisabled}"><a class="page-link" href="#" data-page="${currentPage - 1}">Назад</a></li>`;

        const maxVisible = 5;
        let start = Math.max(1, currentPage - Math.floor(maxVisible / 2));
        let end = Math.min(totalPages, start + maxVisible - 1);

        if (start > 1) {
            html += `<li class="page-item"><a class="page-link" href="#" data-page="1">1</a></li>`;
            if (start > 2) html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
        }

        for (let i = start; i <= end; i++) {
            const active = i === currentPage ? 'active' : '';
            html += `<li class="page-item ${active}"><a class="page-link" href="#" data-page="${i}">${i}</a></li>`;
        }

        if (end < totalPages) {
            if (end < totalPages - 1) html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
            html += `<li class="page-item"><a class="page-link" href="#" data-page="${totalPages}">${totalPages}</a></li>`;
        }

        const nextDisabled = currentPage === totalPages ? 'disabled' : '';
        html += `<li class="page-item ${nextDisabled}"><a class="page-link" href="#" data-page="${currentPage + 1}">Вперед</a></li>`;
        html += '</ul>';

        $container.html(html);
    }

    function updateURL(params) {
        const query = new URLSearchParams(params).toString();
        history.pushState(null, '', `${window.location.pathname}?${query}`);
    }

    function showLoader($container) {
        $container.html('<div class="text-center py-5"><div class="spinner-border text-primary"></div></div>');
    }

    function showEmptyMessage($container, message) {
        message = message || 'Записи не найдены';
        $container.html(`<div class="text-center text-muted py-5">${message}</div>`);
    }

    return {
        escapeHtml: escapeHtml,
        showNotification: showNotification,
        showConfirmModal: showConfirmModal,
        renderPagination: renderPagination,
        updateURL: updateURL,
        showLoader: showLoader,
        showEmptyMessage: showEmptyMessage
    };
})();