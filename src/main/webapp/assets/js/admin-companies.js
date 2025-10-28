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

    let isLoading = false;

    if (state.status) {
        $statusFilters.find('button').removeClass('btn-primary').addClass('btn-outline-secondary');
        $statusFilters.find(`button[data-status="${state.status}"]`)
            .removeClass('btn-outline-secondary').addClass('btn-primary');
    }

    $sortFilters.find('button').removeClass('active');
    $sortFilters.find(`button[data-order="${state.sortOrder}"]`).addClass('active');

    function fetchCompanies() {
        if (isLoading) return;

        isLoading = true;
        TableUtils.showLoader($tableContainer);

        $.ajax({
            url: `${ctx}/api/admin/companies`,
            method: 'GET',
            data: state,
            dataType: 'json',
            timeout: 10000,
            success: function (response) {
                renderTable(response.data);
                TableUtils.renderPagination(response.totalPages, response.currentPage, $paginationContainer);
                TableUtils.updateURL(state);
            },
            error: function (jqXHR, textStatus) {
                let errorMessage = 'Не удалось загрузить данные. Пожалуйста попробуйте позже.';
                if (textStatus === 'timeout') {
                    errorMessage = 'Превышено время ожидания ответа от сервера.';
                }
                if (jqXHR.responseText && jqXHR.responseText.trim() !== '') {
                    errorMessage = JSON.parse(jqXHR.responseText).error;
                }
                $tableContainer.html(`<div class="alert alert-danger">${errorMessage}</div>`);
            },
            complete: function () {
                isLoading = false;
            }
        });
    }

    function renderTable(companies) {
        if (companies.length === 0) {
            TableUtils.showEmptyMessage($tableContainer, 'Компании с выбранными параметрами не найдены.');
            return;
        }

        const tableRows = companies.map(company => {
            let statusBadge;
            switch (company.status) {
                case 'PENDING':
                    statusBadge = '<span class="badge status-pending">Ожидание</span>';
                    break;
                case 'APPROVED':
                    statusBadge = '<span class="badge bg-success">Одобрено</span>';
                    break;
                case 'DENIED':
                    statusBadge = '<span class="badge bg-danger">Отклонено</span>';
                    break;
                default:
                    statusBadge = `<span class="badge bg-secondary">${TableUtils.escapeHtml(company.status)}</span>`;
            }

            const safeCompanyName = TableUtils.escapeHtml(company.companyName);
            const safeInn = TableUtils.escapeHtml(company.inn);
            const companyId = parseInt(company.id);

            let actionButtons = `
               <a href="${ctx}/admin/companies/${companyId}" 
                   class="btn btn-sm btn-outline-primary me-1" 
                   title="Просмотр">
                    <i class="bi bi-eye"></i>
               </a>
            `;

            if (company.status === 'PENDING') {
                actionButtons += `
                    <button class="btn btn-sm btn-outline-success me-1 btn-approve" 
                            data-id="${companyId}" 
                            data-name="${safeCompanyName}" 
                            title="Одобрить">
                        <i class="bi bi-check-circle"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-warning me-1 btn-deny" 
                            data-id="${companyId}" 
                            data-name="${safeCompanyName}" 
                            title="Отклонить">
                        <i class="bi bi-x-circle"></i>
                    </button>
                `;
            }

            actionButtons += `
                <button class="btn btn-sm btn-outline-danger btn-delete" 
                        data-id="${companyId}" 
                        data-name="${safeCompanyName}" 
                        title="Удалить">
                    <i class="bi bi-trash"></i>
                </button>
            `;

            return `
                <tr>
                    <td><a href="${ctx}/admin/companies/${companyId}" 
                           class="fw-semibold text-decoration-none" 
                           title="${safeCompanyName}">${safeCompanyName}</a></td>
                    <td>${safeInn}</td>
                    <td>${statusBadge}</td>
                    <td class="text-nowrap">${actionButtons}</td>
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

    function performAction(companyId, action, companyName) {
        let url, method, successMessage, confirmMessage;

        if (action === 'approve') {
            url = `${ctx}/api/admin/companies/${companyId}/approve`;
            method = 'PUT';
            successMessage = 'Заявка компании одобрена';
            confirmMessage = `Вы уверены, что хотите одобрить заявку компании "${companyName}"?`;
        } else if (action === 'deny') {
            url = `${ctx}/api/admin/companies/${companyId}/deny`;
            method = 'PUT';
            successMessage = 'Заявка компании отклонена';
            confirmMessage = `Вы уверены, что хотите отклонить заявку компании "${companyName}"?`;
        } else if (action === 'delete') {
            url = `${ctx}/api/admin/companies/${companyId}`;
            method = 'DELETE';
            successMessage = 'Компания удалена';
            confirmMessage = `Вы уверены, что хотите удалить компанию "${companyName}"? Это действие необратимо.`;
        } else {
            console.error('Неизвестное действие:', action);
            return;
        }

        TableUtils.showConfirmModal(confirmMessage, function() {
            const $confirmBtn = $('#confirmActionBtn');
            $confirmBtn.prop('disabled', true).text('Обработка...');

            $.ajax({
                url: url,
                method: method,
                timeout: 10000,
                success: function() {
                    $('#confirmActionModal').modal('hide');
                    TableUtils.showNotification(successMessage, 'success');
                    fetchCompanies();
                },
                error: function (jqXHR) {
                    let errorMessage = 'Не удалось выполнить операцию';
                    if (jqXHR.responseText && jqXHR.responseText.trim() !== '') {
                        errorMessage = JSON.parse(jqXHR.responseText).error;
                    }
                    TableUtils.showNotification(errorMessage, 'danger');
                },
                complete: function () {
                    $confirmBtn.prop('disabled', false).text('Подтвердить');
                }
            });
        });
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
        if (page !== state.page && page > 0) {
            state.page = page;
            fetchCompanies();
        }
    });

    $tableContainer.on('click', '.btn-approve', function (e) {
        e.preventDefault();
        const companyId = $(this).data('id');
        const companyName = $(this).data('name');
        performAction(companyId, 'approve', companyName);
    });

    $tableContainer.on('click', '.btn-deny', function(e) {
        e.preventDefault();
        const companyId = $(this).data('id');
        const companyName = $(this).data('name');
        performAction(companyId, 'deny', companyName);
    });

    $tableContainer.on('click', '.btn-delete', function(e) {
        e.preventDefault();
        const companyId = $(this).data('id');
        const companyName = $(this).data('name');
        performAction(companyId, 'delete', companyName);
    });

    fetchCompanies();
});