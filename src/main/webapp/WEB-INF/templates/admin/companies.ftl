<#include "../common-table.ftl">
<#include "base_admin.ftl">

<#macro page_admin_content>
    <@table_layout />
</#macro>

<#macro table_filters>
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <div id="status-filters" class="btn-group filters">
            <button type="button" class="btn btn-sm btn-primary" data-status="">Все</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" data-status="PENDING">Ожидают</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" data-status="APPROVED">Одобрены</button>
            <button type="button" class="btn btn-sm btn-outline-secondary" data-status="DENIED">Отклонены</button>
        </div>

        <div id="sort-filters" class="btn-group filters">
            <button type="button" class="btn btn-sm btn-light active" data-order="desc">Сначала новые</button>
            <button type="button" class="btn btn-sm btn-light" data-order="asc">Сначала старые</button>
        </div>
    </div>
</#macro>

<#macro page_extra_script>
    <script src="${ctx}/assets/js/common-table.js"></script>
    <script src="${ctx}/assets/js/admin-companies.js"></script>
</#macro>

<#macro page_companies_head>
    <link rel="stylesheet" href="${ctx}/assets/css/admin-companies.css">
</#macro>
