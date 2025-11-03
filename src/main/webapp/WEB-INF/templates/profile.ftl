<#macro page_header>
    <#include "header.ftl">
</#macro>

<#macro page_content>
    <div class="container">
        <div class="row justify-content-center my-5">
            <div class="col-lg-6 col-md-8">
                <div class="card shadow-sm border">
                    <div class="card-body p-4">
                        <div class="text-center mb-4">
                            <h2 class="h3 fw-bold mb-2">Настройки профиля</h2>
                            <p class="text-muted medium">
                                Обновите свои учетные данные
                            </p>
                        </div>

                        <#if successMessage?has_content>
                            <div class="alert alert-light border border-success bg-light rounded-3" role="alert">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-check-circle text-success me-3 fs-5"></i>
                                    <div class="text-dark">${successMessage}</div>
                                </div>
                            </div>
                        </#if>

                        <#if error?has_content>
                            <div class="alert alert-light border border-danger bg-light rounded-3" role="alert">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-exclamation-circle text-danger me-3 fs-5"></i>
                                    <div class="text-dark">${error}</div>
                                </div>
                            </div>
                        </#if>

                        <form action="${ctx}/profile/email" method="post" class="mb-4 pb-4 border-bottom">
                            <div class="mb-3">
                                <label for="currentEmail" class="form-label">Текущий Email</label>
                                <input type="email" class="form-control form-control-sm" id="currentEmail" value="${currentEmail!''}" disabled>
                            </div>
                            <div class="mb-3">
                                <label for="newEmail" class="form-label">Новый Email</label>
                                <input type="email" class="form-control form-control-sm" id="newEmail" name="newEmail" required>
                                <div class="invalid-feedback">
                                    Этот email уже используется.
                                </div>
                            </div>
                            <button type="submit" class="btn btn-cta w-100">Обновить Email</button>
                        </form>

                        <form action="${ctx}/profile/password" method="post">
                            <div class="mb-3">
                                <label for="currentPassword" class="form-label">Текущий пароль</label>
                                <input type="password" class="form-control form-control-sm" id="currentPassword" name="currentPassword" required>
                            </div>
                            <div class="mb-3">
                                <label for="newPassword" class="form-label">Новый пароль</label>
                                <input type="password" class="form-control form-control-sm" id="newPassword" name="newPassword" required minlength="8">
                            </div>
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Подтвердите новый пароль</label>
                                <input type="password" class="form-control form-control-sm" id="confirmPassword" name="confirmPassword" required minlength="8">
                                <div class="invalid-feedback">
                                    Новый пароль и его подтверждение не совпадают.
                                </div>
                            </div>
                            <button type="submit" class="btn btn-cta w-100">Обновить Пароль</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro page_footer>
    <#include "footer.ftl">
</#macro>

<#macro page_extra_script>
    <script src="${ctx}/assets/js/email-validator.js"></script>
    <script src="${ctx}/assets/js/profile.js"></script>
    <script>
        initEmailValidation('#newEmail');
    </script>
</#macro>

<#include "base.ftl">