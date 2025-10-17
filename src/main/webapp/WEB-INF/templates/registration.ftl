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
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </#if>

                        <ul class="nav nav-pills nav-fill mb-4 custom-pills" id="registerTab" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active" id="passenger-tab" data-bs-toggle="pill" data-bs-target="#passenger-form" type="button" role="tab" aria-controls="passenger-form" aria-selected="true">Пассажир</button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="company-tab" data-bs-toggle="pill" data-bs-target="#company-form" type="button" role="tab" aria-controls="company-form" aria-selected="false">Компания</button>
                            </li>
                        </ul>

                        <div class="tab-content" id="registerTabContent">

                            <div class="tab-pane fade show active" id="passenger-form" role="tabpanel" aria-labelledby="passenger-tab">
                                <form action="${ctx}/register" method="post">
                                    <input type="hidden" name="role" value="passenger">
                                    <div class="mb-3">
                                        <label for="passengerEmail" class="form-label">Email</label>
                                        <input type="email" class="form-control form-control-sm" id="passengerEmail" name="email" required>
                                    </div>
                                    <div class="mb-4">
                                        <label for="passengerPassword" class="form-label">Пароль</label>
                                        <input type="password" class="form-control form-control-sm" id="passengerPassword" name="password" required>
                                    </div>
                                    <button type="submit" class="btn btn-cta btn-primary w-100">Зарегистрироваться</button>
                                </form>
                            </div>

                            <div class="tab-pane fade" id="company-form" role="tabpanel" aria-labelledby="company-tab">
                                <form action="${ctx}/register" method="post">
                                    <input type="hidden" name="role" value="company">
                                    <div class="mb-3">
                                        <label for="companyName" class="form-label">Название компании</label>
                                        <input type="text" class="form-control form-control-sm" id="companyName" name="companyName" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="inn" class="form-label">ИНН</label>
                                        <input type="text" class="form-control form-control-sm" id="inn" name="inn" required pattern="[0-9]{10,12}" title="10 или 12 цифр">
                                    </div>
                                    <div class="mb-3">
                                        <label for="companyEmail" class="form-label">Рабочий Email</label>
                                        <input type="email" class="form-control form-control-sm" id="companyEmail" name="email" required>
                                    </div>
                                    <div class="mb-4">
                                        <label for="companyPassword" class="form-label">Пароль</label>
                                        <input type="password" class="form-control form-control-sm" id="companyPassword" name="password" required>
                                    </div>

                                    <!-- TODO: add section for uploading file -->

                                    <button type="submit" class="btn btn-cta btn-primary w-100">Отправить заявку</button>
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