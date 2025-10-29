<#macro page_header>
    <#include "header.ftl">
</#macro>

<#macro page_content>
    <section class="hero">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-xl-8 col-lg-9">
                    <div class="hero-card p-4 p-md-5 text-center">
                        <h1 class="display-5 fw-bold mb-3">Сделайте транспорт лучше</h1>
                        <p class="lead mb-4">Увидели проблему в автобусе или трамвае? Оставьте отзыв — он напрямую попадёт к компании и повлияет на реальные изменения.</p>
                        <div class="d-flex gap-3 justify-content-center">
                            <a href="${ctx}/reviews/new" class="btn btn-cta btn-lg px-4">Оставить отзыв</a>
                            <a href="#how" class="btn btn-raise btn-lg px-4">Как это работает</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="how" class="py-5">
        <div class="container">
            <h2 class="text-center fw-semibold mb-4">Как это работает</h2>
            <p class="text-center text-muted mb-5">Всего три шага: опишите ситуацию — отправьте — помогите сделать лучше.</p>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="p-4 feature-card h-100 text-center">
                        <div class="icon-circle mb-3 mx-auto">
                            <i class="bi bi-phone"></i>
                        </div>
                        <h5 class="fw-semibold mb-2">Опишите проблему</h5>
                        <p class="text-muted mb-0">Выберите город, маршрут и номер ТС. Добавьте оценку и комментарий, при необходимости приложите фото.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="p-4 feature-card h-100 text-center">
                        <div class="icon-circle mb-3 mx-auto">
                            <i class="bi bi-send-check"></i>
                        </div>
                        <h5 class="fw-semibold mb-2">Отправьте отзыв</h5>
                        <p class="text-muted mb-0">Мы доставим его представителю компании, который отвечает за этот маршрут и транспорт.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="p-4 feature-card h-100 text-center">
                        <div class="icon-circle mb-3 mx-auto">
                            <i class="bi bi-graph-up-arrow"></i>
                        </div>
                        <h5 class="fw-semibold mb-2">Влияйте на качество</h5>
                        <p class="text-muted mb-0">Компании анализируют статистику и принимают меры. Ваш голос помогает улучшать сервис.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="for-companies" class="company-section py-5">
        <div class="mask"></div>
        <div class="container content">
            <div class="row align-items-center gy-4">
                <div class="col-lg-8">
                    <h2 class="fw-semibold mb-3">Для транспортных компаний</h2>
                    <p class="lead mb-4">Подключите панель и начните получать обратную связь от пассажиров: управляемые маршруты, фильтруемые отзывы, наглядная статистика по показателям и ТС.</p>
                    <a href="${ctx}/register?role=company" class="btn btn-cta px-4">Зарегистрировать компанию</a>
                </div>
                <div class="col-lg-4">
                    <div class="hero-card p-4">
                        <ul class="mb-0">
                            <li class="mb-2">Модерация заявки администратором</li>
                            <li class="mb-2">Управление маршрутами и ТС</li>
                            <li class="mb-2">Фильтры по дате/маршруту/ТС</li>
                            <li class="mb-2">Детализация отзывов с фото</li>
                            <li class="mb-0">Аналитика и показатели качества</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro page_extra_head>
    <link rel="stylesheet" href="${ctx}/assets/css/index.css">
</#macro>

<#macro page_footer>
    <#include "footer.ftl">
</#macro>

<#include "base.ftl">