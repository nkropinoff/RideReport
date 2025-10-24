<#include "base_admin.ftl">

<#macro page_admin_content>
    <div class="filters-section">
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
            <div id="status-filters" class="btn-group">
                <button type="button" class="btn btn-sm btn-primary" data-status="">Все</button>
                <button type="button" class="btn btn-sm btn-outline-secondary" data-status="PENDING">Ожидают</button>
                <button type="button" class="btn btn-sm btn-outline-secondary" data-status="APPROVED">Одобрены</button>
                <button type="button" class="btn btn-sm btn-outline-secondary" data-status="DENIED">Отклонены</button>
            </div>

            <div id="sort-filters" class="btn-group">
                <button type="button" class="btn btn-sm btn-light active" data-order="desc">Сначала новые</button>
                <button type="button" class="btn btn-sm btn-light" data-order="asc">Сначала старые</button>
            </div>
        </div>
    </div>

    <div id="table-container">
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Загрузка...</span>
            </div>
        </div>
    </div>

    <div class="modal fade" id="confirmActionModal" tabindex="-1" aria-labelledby="confirmActionLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmActionLabel">Подтверждение действия</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p id="confirmActionText"></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                    <button type="button" class="btn btn-primary" id="confirmActionBtn">Подтвердить</button>
                </div>
            </div>
        </div>
    </div>

    <div id="pagination-container" class="mt-4"></div>
</#macro>

<#macro page_extra_script>
    <script src="${ctx}/assets/js/admin-companies.js"></script>
</#macro>

<#macro page_companies_head>
    <link rel="stylesheet" href="${ctx}/assets/css/admin-companies.css">
</#macro>