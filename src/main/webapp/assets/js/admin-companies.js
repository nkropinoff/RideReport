$(document).ready(function () {

    const urlParams = new URLSearchParams(window.location.search);

    let state = {
        page: parseInt(urlParams.get('page')) || 1,
        status: urlParams.get('status') || '',
        sortOrder: urlParams.get('sortOrder') || 'desc'
    };

    const $tableContainer = $('#table-container');
    const $paginationContainer = $('#pagination-container');
    const $statusFilters = $('#status-filters');
    const $sortFilters = $('#sort-filters');

    let fetchTimeout = null;
    let isLoading = false;

    if (state.status) {
        $statusFilters.find('button').removeClass('btn-primary').addClass('btn-outline-secondary');
        $statusFilters.find(`button[data-status="${state.status}"]`).removeClass('btn-outline-secondary').addClass('btn-primary');
    }

    $sortFilters.find('button').removeClass('active');
    $sortFilters.find(`button[data-order="${state.sortOrder}"]`).addClass('active');

    function escapeHtml(text) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return String(text).replace(/[&<>"']/g, m => map[m]);
    }

    function fetchCompanies() {
        clearTimeout(fetchTimeout);

        if (isLoading) {
            return;
        }

        fetchTimeout = setTimeout(function () {
            isLoading = true;
            $tableContainer.html('<div class="text-center py-5"><div class="spinner-border text-primary"></div></div>');

            $.ajax({
                url: `${ctx}/api/admin/companies`,
                method: 'GET',
                data: state,
                dataType: 'json',
                timeout: 10000,
                success: function (response) {
                    renderTable(response.data);
                    renderPagination(response.totalPages, response.currentPage);
                    updateURL();
                },
                error: function (jqXHR, textStatus) {

                    let errorMessage = 'Не удалось загрузить данные. Пожалуйста попробуйте позже.';

                    if (textStatus !== 'timeout') {
                        errorMessage = 'Превышено время ожидания ответа от сервера.';
                    }

                    $tableContainer.html('<div class="alert alert-danger">${errorMessage}</div>');
                },
                complete: function () {
                    isLoading = false;
                }
            });
        }, 150);
    }


    function renderTable(companies) {
        if (companies.length === 0) {
            $tableContainer.html('<div class="text-center text-muted py-5">Компании с выбранными параметрами не найдены.</div>');
            return;
        }

        const tableRows = companies.map(company => {
                let statusBadge;
                switch (company.status) {
                    case 'PENDING': statusBadge = '<span class="badge bg-warning text-dark">Ожидание</span>'; break;
                    case 'APPROVED': statusBadge = '<span class="badge bg-success">Одобрено</span>'; break;
                    case 'DENIED': statusBadge = '<span class="badge bg-danger">Отклонено</span>'; break;
                    default: statusBadge = `<span class="badge bg-secondary">${escapeHtml(company.status)}</span>`;
                }

                const safeCompanyName = escapeHtml(company.companyName);
                const safeInn = escapeHtml(company.inn);
                const companyId = parseInt(company.id);

                return `
                    <tr>
                    <td><a href="${ctx}/admin/company/${companyId}" class="fw-semibold text-decoration-none" title="${safeCompanyName}">${safeCompanyName}</a></td>
                    <td>${safeInn}</td>
                    <td>${statusBadge}</td>
                    <td><a href="${ctx}/admin/company/${companyId}" class="btn btn-sm btn-outline-primary"><i class="bi bi-eye"></i></a></td>
                    </tr>
                `;
        }).join('');

        const tableHtml = `
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>Название компании</th>
                            <th>ИНН</th>
                            <th>Статус</th>
                            <th style="width: 1%;">Действие</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${tableRows}
                    </tbody>
                </table>
            </div>
        `;

        $tableContainer.html(tableHtml);
    }

    function renderPagination(totalPages, currentPage) {
        if (totalPages <= 1) {
            $paginationContainer.html('');
            return;
        }

        let paginationHtml = '<ul class="pagination justify-content-center">';

        const prevDisabled = currentPage === 1 ? 'disabled' : '';
        paginationHtml += `<li class="page-item ${prevDisabled}"><a class="page-link" href="#" data-page="${currentPage - 1}">Назад</a></li>`;

        const maxVisible = 5;
        let startPage = Math.max(1, currentPage - Math.floor(maxVisible / 2));
        let endPage = Math.min(totalPages, startPage + maxVisible - 1);

        if (endPage - startPage + 1 < maxVisible) {
            startPage = Math.max(1, endPage - maxVisible + 1);
        }

        if (startPage > 1) {
            paginationHtml += `<li class="page-item"><a class="page-link" href="#" data-page="1">1</a></li>`;
            if (startPage > 2) {
                paginationHtml += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
            }
        }

        for (let i = startPage; i <= endPage; i++) {
            const activeClass = i === currentPage ? 'active' : '';
            paginationHtml += `<li class="page-item ${activeClass}"><a class="page-link" href="#" data-page="${i}">${i}</a></li>`;
        }

        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                paginationHtml += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
            }
            paginationHtml += `<li class="page-item"><a class="page-link" href="#" data-page="${totalPages}">${totalPages}</a></li>`;
        }

        const nextDisabled = currentPage === totalPages ? 'disabled' : '';
        paginationHtml += `<li class="page-item ${nextDisabled}"><a class="page-link" href="#" data-page="${currentPage + 1}">Вперед</a></li>`;

        paginationHtml += '</ul>';
        $paginationContainer.html(paginationHtml);
    }

    $statusFilters.on('click', 'button', function() {
        const $this = $(this);
        state.status = $this.data('status');
        state.page = 1;

        $statusFilters.find('button').removeClass('btn-primary').addClass('btn-outline-secondary');
        $this.removeClass('btn-outline-secondary').addClass('btn-primary');

        fetchCompanies();
    });

    $sortFilters.on('click', 'button', function() {
        const $this = $(this);
        state.sortOrder = $this.data('order');
        state.page = 1;

        $sortFilters.find('button').removeClass('active');
        $this.addClass('active');

        fetchCompanies();
    });

    $paginationContainer.on('click', 'a.page-link', function(e) {
        e.preventDefault();
        const page = $(this).data('page');
        if (page !== state.page) {
            state.page = page;
            fetchCompanies();
        }
    });

    function updateURL() {
        const params = new URLSearchParams(state).toString();
        const newUrl = `${window.location.pathname}?${params}`;
        history.pushState(null, '', newUrl);
    }

    fetchCompanies();
});