<#macro page_content>
    <div class="container">
        <div class="row justify-content-center my-5">
            <div class="col-lg-6 text-center">
                <h1 class="display-1 fw-bold text-primary">${statusCode!500}</h1>
                <h2 class="mb-3">${errorTitle!"Произошла ошибка"}</h2>
                <p class="text-muted mb-4">${errorDescription!"Извините, произошла непредвиденная ошибка."}</p>
                <a href="${ctx}/" class="btn btn-primary btn-lg">
                    <i class="bi bi-house-door me-2"></i>На главную
                </a>
            </div>
        </div>
    </div>
</#macro>

<#macro page_footer>
    <#include "footer.ftl">
</#macro>

<#include "base.ftl">
