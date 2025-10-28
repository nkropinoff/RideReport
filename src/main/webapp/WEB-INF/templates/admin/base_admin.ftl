<#include "../base.ftl">

<#macro page_header>
    <#include "../header.ftl">
</#macro>

<#macro page_content>
    <div class="container py-4">
        <div class="row gx-4">
            <aside class="col-lg-2">
                <nav id="adminSidebar" class="p-3 rounded">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link<#if pageId?? && pageId == 'overview'> active</#if>" href="${ctx}/admin/dashboard">
                                <i class="bi bi-speedometer2 me-2"></i> Обзор
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link<#if pageId?? && pageId == 'companies'> active</#if>" href="${ctx}/admin/companies">
                                <i class="bi bi-building me-2"></i> Компании
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link<#if pageId?? && pageId == 'reviews'> active</#if>" href="${ctx}/admin/reviews">
                                <i class="bi bi-chat-left-text me-2"></i> Отзывы
                            </a>
                        </li>
                    </ul>
                </nav>
            </aside>

            <main class="col-lg-10">
                <div id="adminContentCard" class="p-4 rounded">

                    <div class="border-bottom pb-2 mb-3">
                        <h3 class="fw-semibold mb-0">${sectionTitle}</h3>
                    </div>

                    <div class="admin-content-area">
                        <@page_admin_content />
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
    <link rel="stylesheet" href="${ctx}/assets/css/admin.css">
    <#if .namespace.page_companies_head??>
        <@page_companies_head />
    </#if>
</#macro>