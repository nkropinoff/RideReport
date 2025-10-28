<#include "../common-modals.ftl">

<#macro page_extra_head>
    <link rel="stylesheet" href="${ctx}/assets/css/company-route-form.css">
</#macro>

<#macro page_header>
    <#include "../header.ftl">
</#macro>

<#macro page_content>
    <div class="container py-4">
        <div class="row">
            <div class="col-12">
                <div class="mb-3">
                    <a href="${ctx}/company/dashboard" class="btn btn-soft-outline">
                        <i class="bi bi-arrow-left me-2"></i>Вернуться в панель управления
                    </a>
                </div>

                <div id="formAlert" class="alert alert-warning alert-dismissible fade" role="alert" style="display: none;">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    <span id="formAlertText"></span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>

                <div class="details-card p-4 rounded">
                    <div class="border-bottom pb-3 mb-4">
                        <h4 class="fw-semibold mb-2">Редактирование маршрута</h4>
                        <p class="text-muted mb-0 small">Выберите город, тип транспорта и номер маршрута</p>
                    </div>

                    <div class="row g-3 mb-4">
                        <div class="col-md-4">
                            <label for="citySelect" class="form-label">Город <span class="text-danger">*</span></label>
                            <select class="form-select" id="citySelect" required>
                                <option value="" selected disabled>Выберите город</option>
                                <#list cities as city>
                                    <option value="${city.id}">${city.name}</option>
                                </#list>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label for="transportSelect" class="form-label">Тип транспорта <span class="text-danger">*</span></label>
                            <select class="form-select" id="transportSelect" required>
                                <option value="" selected disabled>Выберите тип</option>
                                <#list transportModes as mode>
                                    <option value="${mode.id}">${mode.name}</option>
                                </#list>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label for="routeSelect" class="form-label">Номер маршрута <span class="text-danger">*</span></label>
                            <select class="form-select" id="routeSelect" required disabled>
                                <option value="" selected disabled>Сначала выберите город и тип</option>
                            </select>
                        </div>
                    </div>

                    <div id="editSection" style="display: none;">
                        <form id="routeForm" method="POST" class="needs-validation" novalidate>
                            <input type="hidden" id="routeId" name="routeId">

                            <div class="vehicle-section mt-4 pt-4">
                                <h5 class="fw-semibold mb-3">Транспортные средства</h5>

                                <div class="row g-3 mb-3">
                                    <div class="col-md-9">
                                        <label for="vehicleNumber" class="form-label">Номер ТС</label>
                                        <input type="text" class="form-control" id="vehicleNumber"
                                               placeholder="Например: А123ВС777" maxlength="20">
                                        <div id="vehicleError" class="error-message"></div>
                                    </div>
                                    <div class="col-md-3">
                                        <label for="addVehicleBtn" class="form-label">&nbsp;</label>
                                        <button type="button" id="addVehicleBtn" class="btn btn-cta w-100">
                                            <i class="bi bi-plus-circle me-1"></i>Добавить
                                        </button>
                                    </div>
                                </div>

                                <div id="vehiclesList" class="vehicles-list"></div>

                                <div class="alert alert-light border-0 bg-light" id="noVehiclesMsg" style="display: none;">
                                    <i class="bi bi-info-circle me-2"></i>
                                    Добавьте хотя бы одно транспортное средство
                                </div>
                            </div>

                            <input type="hidden" id="vehiclesData" name="vehicles" value="">

                            <div class="mt-4 pt-3 d-flex gap-2">
                                <button type="submit" id="submitBtn" class="btn btn-cta px-4">
                                    <i class="bi bi-check-circle me-2"></i>Сохранить изменения
                                </button>
                                <button type="button" id="deleteBtn" class="btn btn-danger px-4">
                                    <i class="bi bi-trash me-2"></i>Удалить маршрут
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <@confirm_modal />
</#macro>

<#macro page_footer>
    <#include "../footer.ftl">
</#macro>

<#include "../base.ftl">c

<script src="${ctx}/assets/js/route-edit.js"></script>
