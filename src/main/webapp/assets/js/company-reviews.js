$(document).ready(function () {

    let state = {
        page: 1,
        sortOrder: 'desc'
    }

    const $tableContainer = $('#table-container');
    const $paginationContainer = $('#pagination-container');
    const $sortFilters = $('#sort-filters');

    isLoading = false;

    $sortFilters.find('button').removeClass('active');
    $sortFilters.find(`button[data-order="${state.sortOrder}"]`).addClass('active');

    $sortFilters.on('click', 'button', function () {
        state.sortOrder = $(this).data('order');
        state.page = 1;

        $sortFilters.find('button').removeClass('active');
        $(this).addClass('active');

        fetchReviews();
    })

    function fetchReviews() {
        if (isLoading) return;

        isLoading = true;
        TableUtils.showLoader($tableContainer);

        $.ajax({
            url: `${ctx}/api/company/reviews`,
            method: 'GET',
            data: state,
            dataType: 'json',
            timeout: 10000,
            success: function (response) {
                renderTable(response.data);
                TableUtils.renderPagination(response.totalPages, response.currentPage, $paginationContainer);
            },
            error: function (jqXHR, textStatus) {
                let errorMessage = 'Не удалось загрузить данные, попробуйте позже';
                if (textStatus === 'timeout') {
                    errorMessage = 'Превышено время ожидания ответа от сервера.';
                }
                if (jqXHR.responseText && jqXHR.responseText.trim() !== '') {
                    errorMessage = JSON.parse(jqXHR.responseText).error;
                }
                $tableContainer.html(`<div class="alert alert-danger">${errorMessage}</div>`);
            },
            complete: function () {
                isLoading = false;
            }
        })
    }

    function renderTable(reviews) {
        if (reviews.length === 0) {
            TableUtils.showEmptyMessage($tableContainer, 'Отзывов пока нет');
            return;
        }

        const tableRows = reviews.map(review => {

            const safeRideTime = TableUtils.escapeHtml(review.rideTime);
            const safeCity = TableUtils.escapeHtml(review.city);
            const safeTransportMode = TableUtils.escapeHtml(review.transportMode);
            const safeRoute = TableUtils.escapeHtml(review.route);
            const safeVehicleNumber = TableUtils.escapeHtml(review.vehicleNumber);
            const safePassengerEmail = TableUtils.escapeHtml(review.passengerEmail);

            return `<tr data-review-id="${review.reviewId}">
                       <td title="Время поездки">${safeRideTime}</td>
                       <td>${safeCity}</td>
                       <td>${safeTransportMode}</td>
                       <td>${safeRoute}</td>
                       <td>${safeVehicleNumber}</td>
                       <td>${safePassengerEmail}</td>
                   </tr>`
        }).join('');

        const tableHtml = `
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>Время поездки</th>
                            <th>Город</th>
                            <th>Транспорт</th>
                            <th>Маршрут</th>
                            <th>Номер ТС</th>
                            <th>Пассажир</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${tableRows}
                    </tbody>
                </table>
            </div>
        `

        $tableContainer.html(tableHtml);
    }

    $tableContainer.on('click', 'tr[data-review-id]', function () {
        const reviewId = $(this).data('review-id');
        window.location.href = `${ctx}/company/reviews/${reviewId}`;
    });

    $paginationContainer.on('click', 'a.page-link', function(e) {
        e.preventDefault();
        const page = $(this).data('page');
        if (page !== state.page && page > 0) {
            state.page = page;
            fetchReviews();
        }
    });

    fetchReviews();
});