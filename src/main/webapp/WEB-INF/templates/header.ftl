<header class="sticky-top">
    <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
        <div class="container">
            <a class="navbar-brand fw-semibold d-flex align-items-center gap-2" href="${ctx}/">
                <img src="${ctx}/assets/img/logo-ridereport.png" alt="Logo" height="32" />
                RideReport
            </a>

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#topNav">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="topNav">
                <ul class="navbar-nav ms-auto align-items-lg-center gap-2">

                    <!-- TODO: specific buttons for authorized users -->

                    <li class="nav-item">
                        <a class="btn btn-raise px-3" href="${ctx}/#how">Как это работает</a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-raise px-3" href="${ctx}/#for-companies">Для компаний</a>
                    </li>
                    <li class="nav-item ms-lg-2">
                        <a class="btn btn-soft-outline px-3" href="${ctx}/login">Войти</a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-cta px-3" href="${ctx}/register">Регистрация</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>
