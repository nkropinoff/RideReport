<#macro page_extra_head>
    <link rel="stylesheet" href="${ctx}/assets/css/registration.css">
</#macro>

<#macro page_content>
    <div class="container">
        <div class="row justify-content-center my-5">
            <div class="col-lg-5 col-md-8">
                <div class="card shadow-sm border">
                    <div class="card-body p-4">
                        <div class="text-center mb-3">
                            <h2 class="h3 fw-bold mb-2">Создание аккаунта</h2>
                            <p class="text-muted medium">
                                Присоединяйтесь и улучшайте общественный транспорт в своем городе
                            </p>
                        </div>

                        <#if error?has_content>
                            <div class="alert alert-light border border-danger bg-light rounded-3" role="alert">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-exclamation-circle text-danger me-3 fs-5"></i>
                                    <div class="text-dark">${error}</div>
                                </div>
                            </div>
                        </#if>

                        <ul class="nav nav-pills nav-fill mb-4 custom-pills" id="registerTab" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link <#if !(role?? && role == 'company')>active</#if>" id="passenger-tab" data-bs-toggle="pill" data-bs-target="#passenger-form" type="button" role="tab" aria-controls="passenger-form" aria-selected="${(!(role?? && role == 'company'))?c}">
                                    Пассажир
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link <#if role?? && role == 'company'>active</#if>" id="company-tab" data-bs-toggle="pill" data-bs-target="#company-form" type="button" role="tab" aria-controls="company-form" aria-selected="${(role?? && role == 'company')?c}">
                                    Компания
                                </button>
                            </li>
                        </ul>

                        <div class="tab-content" id="registerTabContent">

                            <div class="tab-pane fade <#if !(role?? && role == 'company')>show active</#if>" id="passenger-form" role="tabpanel" aria-labelledby="passenger-tab">
                                <form action="${ctx}/register" method="post">
                                    <input type="hidden" name="role" value="passenger">
                                    <div class="mb-3">
                                        <label for="passengerEmail" class="form-label">Email</label>
                                        <input type="email" class="form-control form-control-sm email-check" id="passengerEmail" name="email" value="<#if role?? && role == 'passenger'>${email!''}</#if>" required>
                                        <div class="invalid-feedback">
                                            Этот email уже используется.
                                        </div>
                                    </div>
                                    <div class="mb-4">
                                        <label for="passengerPassword" class="form-label">Пароль</label>
                                        <input type="password" class="form-control form-control-sm" id="passengerPassword" name="password" required>
                                    </div>
                                    <button type="submit" class="btn btn-cta btn-primary w-100">Зарегистрироваться</button>
                                </form>
                            </div>

                            <div class="tab-pane fade <#if role?? && role == 'company'>show active</#if>" id="company-form" role="tabpanel" aria-labelledby="company-tab">
                                <form action="${ctx}/register" method="post" enctype="multipart/form-data">
                                    <input type="hidden" name="role" value="company">
                                    <div class="mb-3">
                                        <label for="companyName" class="form-label">Название компании</label>
                                        <input type="text" class="form-control form-control-sm" id="companyName" name="companyName" value="${companyName!''}" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="inn" class="form-label">ИНН</label>
                                        <input type="text" class="form-control form-control-sm" id="inn" name="inn" value="${inn!''}" required pattern="^(\d{10}|\d{12})$" title="ИНН должен состоять из 10 или 12 цифр">
                                    </div>
                                    <div class="mb-3">
                                        <label for="companyEmail" class="form-label">Рабочий Email</label>
                                        <input type="email" class="form-control form-control-sm email-check" id="companyEmail" name="email" value="<#if role?? && role == 'company'>${email!''}</#if>" required>
                                        <div class="invalid-feedback">
                                            Этот email уже используется.
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <label for="companyPassword" class="form-label">Пароль</label>
                                        <input type="password" class="form-control form-control-sm" id="companyPassword" name="password" required>
                                    </div>

                                    <div class="mb-4">
                                        <div class="mb-2">
                                            <div class="tooltip-wrapper">
                                                <span class="form-label mb-0">Подтверждающие документы</span>
                                                <span class="info-icon">i</span>
                                                <div class="tooltip-content">
                                                    <ul class="list-unstyled mb-0">
                                                        <li>&bull; Свидетельство ОГРН/ОГРНИП</li>
                                                        <li>&bull; Свидетельство ИНН</li>
                                                        <li>&bull; Паспорт представителя</li>
                                                        <li>&bull; Доверенность на право действовать от имени организации</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <input class="form-control form-control-sm" type="file" id="companyDocuments" name="documents" multiple>
                                        <div id="fileList" class="mt-2"></div>
                                        <small class="form-text text-muted">
                                            Загрузите до 4 документов, каждый не более 10 МБ.
                                        </small>

                                    </div>

                                    <button type="submit" class="btn btn-cta btn-primary w-100">Отправить заявку на регистрацию</button>
                                </form>
                            </div>
                        </div>

                        <div class="text-center mt-4">
                            <small class="text-muted">Уже есть аккаунт? <a href="${ctx}/login">Войти</a></small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
    </div>
</#macro>

<#macro page_extra_script>
    <script src="${ctx}/assets/js/registration.js"></script>
</#macro>

<#macro page_footer>
    <#include "footer.ftl">
</#macro>

<#include "base.ftl">