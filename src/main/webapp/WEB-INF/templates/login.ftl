<#macro page_content>
    <div class="container">
        <div class="row justify-content-center my-5">
            <div class="col-lg-5 col-md-8">
                <div class="card shadow-sm border">
                    <div class="card-body p-4">
                        <div class="text-center mb-4">
                            <h2 class="h3 fw-bold mb-2">Вход в аккаунт</h2>
                            <p class="text-muted medium">
                                Добро пожаловать! Войдите, чтобы продолжить.
                            </p>
                        </div>

                        <#if error?has_content>
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </#if>

                        <form action="${ctx}/login" method="post">
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control form-control-sm" id="email" name="email" required>
                            </div>
                            <div class="mb-4">
                                <label for="password" class="form-label">Пароль</label>
                                <input type="password" class="form-control form-control-sm" id="password" name="password" required>
                            </div>
                            <button type="submit" class="btn btn-cta btn-primary w-100">Войти</button>
                        </form>

                        <div class="text-center mt-4">
                            <small class="text-muted">Нет аккаунта? <a href="${ctx}/register">Зарегистрироваться</a></small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro page_footer>
    <#include "footer.ftl">
</#macro>

<#include "base.ftl">
