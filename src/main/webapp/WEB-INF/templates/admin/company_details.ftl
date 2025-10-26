<#macro page_extra_head>
    <link rel="stylesheet" href="${ctx}/assets/css/company-details.css">
</#macro>

<#macro page_header>
    <#include "../header.ftl">
</#macro>

<#macro page_content>
    <div class="container py-4">
        <div class="row">
            <div class="col-12">
                <div class="mb-3">
                    <a href="${ctx}/admin/companies" class="btn btn-soft-outline">
                        <i class="bi bi-arrow-left me-2"></i>Назад к списку компаний
                    </a>
                </div>

                <div class="details-card p-4 rounded">
                    <div class="border-bottom pb-3 mb-4">
                        <div class="d-flex justify-content-between align-items-start flex-wrap gap-3">
                            <div style="min-width: 0; flex: 1;">
                                <h2 class="fw-semibold mb-2 company-title">${company.companyName}</h2>
                                <p class="text-muted mb-0">Детальная информация о компании</p>
                            </div>
                            <div class="flex-shrink-0">
                                <#if company.status == 'PENDING'>
                                    <span class="badge status-pending fs-6">Ожидание</span>
                                <#elseif company.status == 'APPROVED'>
                                    <span class="badge bg-success fs-6">Одобрено</span>
                                <#elseif company.status == 'DENIED'>
                                    <span class="badge bg-danger fs-6">Отклонено</span>
                                </#if>
                            </div>
                        </div>
                    </div>

                    <div class="row g-4 mb-4">
                        <div class="col-md-6">
                            <div class="info-block">
                                <label class="info-label">Название компании</label>
                                <p class="info-value">${company.companyName}</p>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="info-block">
                                <label class="info-label">ИНН</label>
                                <p class="info-value">${company.inn}</p>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="info-block">
                                <label class="info-label">Email</label>
                                <p class="info-value">${company.email}</p>
                            </div>
                        </div>
                    </div>

                    <#if company.documents?has_content>
                        <div class="documents-section mt-4">
                            <h4 class="fw-semibold mb-3">Документы компании</h4>
                            <div class="documents-list">
                                <#list company.documents as document>
                                    <div class="document-item">
                                        <div class="document-icon">
                                            <#if document.fileType?contains('image')>
                                                <i class="bi bi-file-image"></i>
                                            <#elseif document.fileType?contains('pdf')>
                                                <i class="bi bi-file-pdf"></i>
                                            <#else>
                                                <i class="bi bi-file-earmark"></i>
                                            </#if>
                                        </div>
                                        <div class="document-info">
                                            <p class="document-name" title="${document.originalFilename}">${document.originalFilename}</p>
                                            <p class="document-size">${document.fileSizeFormatted}</p>
                                        </div>
                                        <div class="document-actions">
                                            <a href="${ctx}${document.downloadUrl}"
                                               class="btn btn-sm btn-cta">
                                                <i class="bi bi-download me-1"></i>Скачать
                                            </a>
                                        </div>
                                    </div>
                                </#list>
                            </div>
                        </div>
                    <#else>
                        <div class="alert alert-light border mt-4" role="alert">
                            <i class="bi bi-info-circle me-2"></i>
                            К данной компании не прикреплены документы.
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro page_footer>
    <#include "../footer.ftl">
</#macro>

<#include "../base.ftl">
