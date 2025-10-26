<#include "../base.ftl">

<#macro page_header>
    <#include "../header.ftl">
</#macro>

<#macro page_content>
    <div class="container py-4">
        <div class="row gx-4">
            <aside class="col-lg-2">
                <nav id="companySidebar" class="p-3 rounded">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link<#if pageId?? && pageId == 'statistics'> active</#if>" href="${ctx}/company/dashboard">
                                <i class="bi bi-bar-chart-fill me-2"></i> Статистика
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link<#if pageId?? && pageId == 'routes'> active</#if>" href="${ctx}/company/routes">
                                <i class="bi bi-signpost-2 me-2"></i> Маршруты
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link<#if pageId?? && pageId == 'reviews'> active</#if>" href="${ctx}/company/reviews">
                                <i class="bi bi-chat-left-text me-2"></i> Отзывы
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link<#if pageId?? && pageId == 'create-route'> active</#if>" href="${ctx}/company/routes/create">
                                <i class="bi bi-plus-circle me-2"></i> Создать маршрут
                            </a>
                        </li>
                    </ul>
                </nav>
            </aside>

            <main class="col-lg-10">
                <div id="companyContentCard" class="p-4 rounded">
                    <div class="border-bottom pb-2 mb-3">
                        <h3 class="fw-semibold mb-0">${sectionTitle}</h3>
                    </div>

                    <div class="company-content-area">
                        <@page_company_content />
                    </div>
                </div>
            </main>
        </div>
    </div>
</#macro>

<#macro page_footer>
    <#include "../footer.ftl">
</#macro>

<#macro page_extra_head>
    <link rel="stylesheet" href="${ctx}/assets/css/company.css">
    <#if .namespace.page_company_extra_head??>
        <@page_company_extra_head />
    </#if>
</#macro>
