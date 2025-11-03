<#include "base_company.ftl">

<#macro page_company_extra_head>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="${ctx}/assets/css/company_dashboard.css">
</#macro>

<#macro page_company_content>
    <div class="stats-card">
        <div class="stats-card-header">
            <h5>Общая статистика по компании</h5>
        </div>
        <div id="company-stats-container">
            <#if companyStats??>
                <#list companyStats as stat>
                    <div class="stat-item">
                        <div class="stat-category-name">${stat.categoryName}</div>
                        <div class="progress-wrapper">
                            <div class="stat-values">
                                <span class="positive">${stat.positiveCount}</span>
                                <span class="negative">${stat.negativeCount}</span>
                            </div>
                            <div class="progress" style="height: 10px;">
                                <div class="progress-bar bg-success" role="progressbar" style="width: ${stat.positivePercent}%"></div>
                                <div class="progress-bar bg-danger" role="progressbar" style="width: ${stat.negativePercent}%"></div>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <p class="text-muted m-0">Статистика по компании отсутствует.</p>
            </#if>
        </div>
    </div>

    <div class="stats-card">
        <div class="stats-card-header">
            <h5>Статистика по транспортному средству</h5>
            <select id="vehicle-select" class="form-select">
                <option value="">Выберите номер ТС</option>
                <#if companyVehicleNumbers??>
                    <#list companyVehicleNumbers as number>
                        <option value="${number}">${number}</option>
                    </#list>
                </#if>
            </select>
        </div>

        <div id="vehicle-stats-container">
            <p class="text-muted text-center" id="vehicle-stats-placeholder">Выберите ТС для просмотра статистики.</p>
        </div>
    </div>
</#macro>

<#macro page_extra_script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/i18n/ru.js"></script>
    <script src="${ctx}/assets/js/dashboard-company.js"></script>
</#macro>