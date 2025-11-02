<#include "../common-table.ftl">
<#include "base_company.ftl">

<#macro page_company_content>
    <@table_layout />
</#macro>

<#macro table_filters>
    <div id="sort-filters" class="btn-group filters">
        <button type="button" class="btn btn-sm btn-light active" data-order="desc">Сначала новые</button>
        <button type="button" class="btn btn-sm btn-light" data-order="asc">Сначала старые</button>
    </div>
</#macro>

<#macro page_extra_script>
    <script src="${ctx}/assets/js/common-table.js"></script>
    <script src="${ctx}/assets/js/company-reviews.js"></script>
</#macro>
