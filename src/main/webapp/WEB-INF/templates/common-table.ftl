<#include "common-modals.ftl">

<#macro table_layout>
    <div class="filters-section">
        <@table_filters />
    </div>

    <div id="table-container">
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Загрузка...</span>
            </div>
        </div>
    </div>

    <@confirm_modal />

    <div id="pagination-container" class="mt-4"></div>
</#macro>