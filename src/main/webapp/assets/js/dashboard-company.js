$(document).ready(function() {
    select2Config = {
        theme: 'bootstrap-5',
        language: 'ru',
        width: '100%',
    }

    $('#vehicle-select').select2($.extend({}, select2Config, {
        placeholder: 'Выберите номер ТС',
        allowClear: false
    }))

    $('#vehicle-select').on('change', function() {
        const vehicleNumber = $(this).val();
        const vehicleStatsContainer = $('#vehicle-stats-container');

        if (!vehicleNumber) {
            vehicleStatsContainer.html('<p class="text-muted text-center" id="vehicle-stats-placeholder">Выберите ТС для просмотра статистики.</p>');
            return;
        }

        $.ajax({
            url: `${ctx}/api/company/statistics/vehicle`,
            method: 'GET',
            data: {number: vehicleNumber},
            dataType: 'json',
            success: function (vehicleStats) {
                vehicleStatsContainer.empty();
                if (vehicleStats && vehicleStats.length > 0) {
                    vehicleStats.forEach(function (stat) {
                        const vehicleStatItem = createStatItem(stat);
                        vehicleStatsContainer.append(vehicleStatItem);
                    });
                }
            },
            error: function () {
                vehicleStatsContainer.html('<p class="text-danger text-center">Ошибка загрузки данных.</p>');
            }
        });
    });

    function createStatItem(stat) {
        return `
            <div class="stat-item">
                <div class="stat-category-name">${stat.categoryName}</div>
                <div class="progress-wrapper">
                    <div class="stat-values">
                        <span class="positive">${stat.positiveCount}</span>
                        <span class="negative">${stat.negativeCount}</span>
                    </div>
                    <div class="progress" style="height: 10px;">
                        <div class="progress-bar bg-success" role="progressbar" style="width: ${stat.positivePercent}%"></div>
                        <div class="progress-bar bg-danger" role="progressbar" style="width: ${stat.negativePercent}%"></div>
                    </div>
                </div>
            </div>
        `;
    }
});