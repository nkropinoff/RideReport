<#ftl output_format="HTML" auto_esc=true>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <title>${pageTitle!"RideReport"}</title>

    <link rel="apple-touch-icon" sizes="180x180" href="${ctx}/assets/img/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="${ctx}/assets/img/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="${ctx}/assets/img/favicon-16x16.png">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${ctx}/assets/css/main.css">

    <#if .namespace.page_extra_head??>
        <@page_extra_head />
    </#if>

</head>

<body class="d-flex flex-column min-vh-100 bg-body">

    <#if .namespace.page_header??>
        <@page_header />
    </#if>

    <main class="flex-grow-1">
        <@page_content />
    </main>

    <#if .namespace.page_footer??>
        <@page_footer />
    </#if>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

    <#if .namespace.page_extra_script??>
        <@page_extra_script />
    </#if>
</body>
</html>
