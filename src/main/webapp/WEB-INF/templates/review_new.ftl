<#macro page_header>
    <#include "header.ftl">
</#macro>

<#macro page_content>
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-12 col-md-10 col-lg-8 col-xl-6">

                <div class="review-form-wrapper">
                    <div id="formAlert" class="alert alert-warning alert-dismissible fade" role="alert" style="display: none;">
                        <i class="bi bi-exclamation-triangle me-2"></i>
                        <span id="formAlertText"></span>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>

                    <form id="reviewForm" method="POST" action="${ctx}/reviews" enctype="multipart/form-data">
                        <div class="review-card">
                            <div class="text-center border-bottom pb-3 mb-4">
                                <h4 class="fw-semibold mb-2">Создание отзыва</h4>
                                <p class="text-muted mb-0 small">Выберите город, тип транспорта, маршрут и номер транспортного средства</p>
                            </div>

                            <div class="row g-3 mb-4">
                                <div class="col-6">
                                    <label for="citySelect" class="form-label">
                                        Город <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" name="cityId" id="citySelect" required>
                                        <option value="" selected disabled>Выберите город</option>
                                        <#list cities as city>
                                            <option value="${city.id}">${city.name}</option>
                                        </#list>
                                    </select>
                                </div>

                                <div class="col-6">
                                    <label for="transportSelect" class="form-label">
                                        Тип <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" name="transportModeId" id="transportSelect" required>
                                        <option value="" selected disabled>Выберите тип</option>
                                        <#list transportModes as mode>
                                            <option value="${mode.id}">${mode.name}</option>
                                        </#list>
                                    </select>
                                </div>

                                <div class="col-6">
                                    <label for="routeSelect" class="form-label">
                                        Маршрут <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" name="routeId" id="routeSelect" required disabled>
                                        <option value="">Выберите маршрут</option>
                                    </select>
                                </div>

                                <div class="col-6">
                                    <label for="vehicleSelect" class="form-label">
                                        Номер ТС <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" name="vehicleNumber" id="vehicleSelect" required disabled>
                                        <option value="">Выберите ТС</option>
                                    </select>
                                </div>
                            </div>

                            <div class="border-top pt-2 mb-4">
                                <p class="text-muted small text-center mb-3">Выберите время поездки</p>
                                <input type="datetime-local"
                                       class="form-control"
                                       name="rideTime"
                                       id="rideTime"
                                       required>
                            </div>

                            <div class="border-top pt-2 mb-4">
                                <h5 class="fw-semibold mb-3 text-center">Оцените Вашу поездку</h5>

                                <div class="feedback-ratings">
                                    <#list feedbackCategories as category>
                                        <div class="rating-category mb-3">
                                            <label class="form-label d-block mb-2">${category.name}</label>

                                            <div class="btn-group w-100" role="group" data-category-id="${category.id}">
                                                <input type="radio"
                                                       class="btn-check"
                                                       name="category_${category.id}"
                                                       id="negative_${category.id}"
                                                       value="${category.negativeTagId}"
                                                       autocomplete="off">
                                                <label class="btn btn-outline-danger" for="negative_${category.id}">
                                                    <i class="bi bi-hand-thumbs-down"></i> ${category.negativeTagName}
                                                </label>

                                                <input type="radio"
                                                       class="btn-check"
                                                       name="category_${category.id}"
                                                       id="neutral_${category.id}"
                                                       value=""
                                                       checked
                                                       autocomplete="off">
                                                <label class="btn btn-outline-secondary" for="neutral_${category.id}">
                                                    Не оценивать
                                                </label>

                                                <input type="radio"
                                                       class="btn-check"
                                                       name="category_${category.id}"
                                                       id="positive_${category.id}"
                                                       value="${category.positiveTagId}"
                                                       autocomplete="off">
                                                <label class="btn btn-outline-success" for="positive_${category.id}">
                                                    <i class="bi bi-hand-thumbs-up"></i> ${category.positiveTagName}
                                                </label>
                                            </div>
                                        </div>
                                    </#list>
                                </div>
                            </div>

                            <div class="border-top pt-2 mb-4">
                                <p class="text-muted small text-center mb-3">Опишите подробнее ваши впечатления (необязательно)</p>

                                <div class="review-text-wrapper">
                                    <textarea
                                            class="form-control review-textarea"
                                            name="reviewText"
                                            id="reviewText"
                                            placeholder="Расскажите подробнее"
                                            maxlength="2000"></textarea>
                                </div>
                            </div>
                            <div class="border-top pt-2 mb-4">
                                <p class="text-muted small text-center mb-3">При необходимости прикрепите фото</p>
                                <div class="photo-upload-wrapper">
                                    <input
                                            type="file"
                                            class="form-control photo-input"
                                            name="photo"
                                            id="photoUpload"
                                            accept="image/jpeg,image/png,image/jpg">
                                </div>
                            </div>

                            <input type="hidden" name="selectedTags" id="selectedTags">

                            <div class="d-grid">
                                <button type="submit" class="btn btn-cta btn-lg">
                                    <i class="bi bi-send me-2"></i>Отправить отзыв
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro page_extra_head>
    <link rel="stylesheet" href="${ctx}/assets/css/review-form.css">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css" rel="stylesheet" />
</#macro>

<#macro page_extra_script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/i18n/ru.js"></script>
    <script src="${ctx}/assets/js/review-form.js"></script>
</#macro>

<#macro page_footer>
    <#include "footer.ftl">
</#macro>

<#include "base.ftl">
