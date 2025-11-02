<#include "../common-modals.ftl">

<#macro page_extra_head>
    <link rel="stylesheet" href="${ctx}/assets/css/company-review-details.css">
</#macro>

<#macro page_header>
    <#include "../header.ftl">
</#macro>

<#macro page_content>
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-12 col-md-11 col-lg-10 col-xl-9">
                <div class="mb-3">
                    <a href="${ctx}/company/reviews" class="btn btn-soft-outline">
                        <i class="bi bi-arrow-left me-2"></i>Вернуться к списку отзывов
                    </a>
                </div>

                <div class="review-details-wrapper">
                    <div class="review-details-card">
                        <div class="text-center border-bottom pb-3 mb-4">
                            <h4 class="fw-semibold mb-2">Подробности отзыва</h4>
                            <p class="text-muted mb-0 small">
                                Отзыв оставлен пассажиром <strong>${review.passengerEmail}</strong>
                                <span class="mx-2">•</span>
                                Дата поездки: <strong>${review.rideTime}</strong>
                            </p>
                        </div>

                        <div class="route-info-section mb-4">
                            <h5 class="section-title mb-3">Информация о поездке</h5>
                            <div class="row g-3">
                                <div class="col-md-3 col-6">
                                    <div class="info-item">
                                        <span class="info-label">Город</span>
                                        <span class="info-value">${review.city}</span>
                                    </div>
                                </div>
                                <div class="col-md-3 col-6">
                                    <div class="info-item">
                                        <span class="info-label">Тип транспорта</span>
                                        <span class="info-value">${review.transportMode}</span>
                                    </div>
                                </div>
                                <div class="col-md-3 col-6">
                                    <div class="info-item">
                                        <span class="info-label">Маршрут</span>
                                        <span class="info-value">${review.route}</span>
                                    </div>
                                </div>
                                <div class="col-md-3 col-6">
                                    <div class="info-item">
                                        <span class="info-label">Номер ТС</span>
                                        <span class="info-value">${review.vehicleNumber}</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="review-feedback-section mb-4">
                            <h5 class="section-title mb-3">Оценки</h5>
                            <div class="ratings-container">
                                <#list review.ratings as ratingItem>
                                    <div class="rating-badge">
                                        <span class="rating-category">${ratingItem.feedbackCategory}:</span>
                                        <span class="rating-tag ${ratingItem.tagType}">
                                            ${ratingItem.feedbackTag}
                                        </span>
                                    </div>
                                </#list>
                            </div>
                        </div>

                        <div class="review-additional-info-section">
                            <div class="row">
                                <div class="col-md-8 mb-4 mb-md-0">
                                    <h5 class="section-title mb-3">Текст отзыва</h5>
                                    <div class="review-text-container">
                                        <div class="review-text">${review.text!"Пассажир не оставил текстового отзыва."}</div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <h5 class="section-title mb-3">Фото отзыва</h5>
                                    <div class="photo-container">
                                        <#if review.photoUrl?? && review.photoUrl?has_content>
                                            <div class="thumbnail-wrapper">
                                                <img
                                                        src="${review.photoUrl}"
                                                        alt="Фото отзыва"
                                                        class="review-thumbnail"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#imageModal"
                                                >
                                            </div>
                                        <#else>
                                            <div class="empty-photo">
                                                <i class="bi bi-image" style="font-size: 2rem; color: #adb5bd;"></i>
                                                <p class="mb-0 mt-2">Пассажир не прикрепил фото</p>
                                            </div>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <#if review.photoUrl?? && review.photoUrl?has_content>
        <div class="modal fade" id="imageModal" tabindex="-1" aria-labelledby="imageModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content photo-modal">
                    <div class="modal-header">
                        <h5 class="modal-title" id="imageModalLabel">Фото отзыва</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
                    </div>
                    <div class="modal-body">
                        <img src="${review.photoUrl}"
                             alt="Фото отзыва в полном размере"
                             class="modal-image">
                    </div>
                </div>
            </div>
        </div>
    </#if>
</#macro>

<#macro page_footer>
    <#include "../footer.ftl">
</#macro>

<#include "../base.ftl">
