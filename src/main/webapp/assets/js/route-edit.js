let vehicleNumbers = [];
let originalVehicles = [];
let confirmModal;

$(document).ready(function () {
    confirmModal = new bootstrap.Modal(document.getElementById('confirmActionModal'));

    $('#citySelect, #transportSelect').on('change', function() {
        const cityId = $('#citySelect').val();
        const transportModeId = $('#transportSelect').val();

        if (cityId && transportModeId) {
            loadRoutes(cityId, transportModeId);
        } else {
            resetRouteSelect();
        }
    });

    $('#routeSelect').on('change', function() {
        const routeId = $(this).val();
        if (routeId) {
            loadRouteDetails(routeId);
        }
    });

    $('#addVehicleBtn').click(function () {
        const vehicleInput = $('#vehicleNumber');
        const vehicleNumber = vehicleInput.val().trim();
        const errorDiv = $('#vehicleError');

        errorDiv.text('');
        vehicleInput.removeClass('is-invalid');
        hideFormAlert();

        if (vehicleNumber === '') {
            showError('Введите номер транспортного средства');
            return;
        }

        if (vehicleNumbers.includes(vehicleNumber)) {
            showError('Транспортное средство с таким номером уже добавлено');
            return;
        }

        const routeId = $('#routeId').val();
        checkVehicleExists(vehicleNumber, routeId);
    });

    $('#vehiclesList').on('click', '.remove-vehicle-btn', function() {
        const vehicleItem = $(this).closest('.vehicle-item');
        const vehicleNumber = vehicleItem.data('vehicle');

        vehicleNumbers = vehicleNumbers.filter(num => num !== vehicleNumber);
        vehicleItem.remove();

        if (vehicleNumbers.length === 0) {
            $('#noVehiclesMsg').show();
        }

        updateVehiclesData();
        updateSubmitButton();
    });

    $('#deleteBtn').click(function() {
        $('#confirmActionLabel').text('Подтверждение удаления');
        $('#confirmActionText').text('Вы уверены, что хотите удалить этот маршрут? Все транспортные средства будут также удалены.');
        $('#confirmActionBtn').removeClass('btn-primary').addClass('btn-danger').text('Удалить');

        confirmModal.show();
    });

    $('#confirmActionBtn').off('click').on('click', function() {
        const routeId = $('#routeId').val();
        if (routeId) {
            deleteRoute(routeId);
        }
    });

    $('#routeForm').submit(function(e) {
        e.preventDefault();

        if (vehicleNumbers.length === 0) {
            showFormAlert('Добавьте хотя бы одно транспортное средство');
            return false;
        }

        const routeId = $('#routeId').val();
        saveRoute(routeId);
    });

    function loadRoutes(cityId, transportModeId) {
        $.ajax({
            url: ctx + '/api/company/routes',
            method: 'GET',
            data: {
                cityId: cityId,
                transportModeId: transportModeId
            },
            dataType: 'json',
            success: function(response) {
                const routeSelect = $('#routeSelect');
                routeSelect.empty();

                if (response.length === 0) {
                    routeSelect.append('<option value="" selected disabled>Нет доступных маршрутов</option>');
                    routeSelect.prop('disabled', true);
                    $('#editSection').hide();
                } else {
                    routeSelect.append('<option value="" selected disabled>Выберите маршрут</option>');
                    response.forEach(function(route) {
                        routeSelect.append(`<option value="${route.id}">${route.number}</option>`);
                    });
                    routeSelect.prop('disabled', false);
                }
            },
            error: function () {
                showFormAlert('Ошибка загрузки маршрутов');
            }
        });
    }

    function loadRouteDetails(routeId) {
        $.ajax({
            url: ctx + 'api/company/routes/' + routeId,
            method: 'GET',
            dataType: 'json',
            success: function(response) {
                $('#routeId').val(routeId);

                vehicleNumbers = response.vehicles || [];
                originalVehicles = [...vehicleNumbers];

                $('#vehiclesList').empty();
                vehicleNumbers.forEach(num => addVehicleToList(num));

                if (vehicleNumbers.length === 0) {
                    $('#noVehiclesMsg').show();
                } else {
                    $('#noVehiclesMsg').hide();
                }

                updateVehiclesData();
                updateSubmitButton();
                $('#editSection').show();
            },
            error: function () {
                showFormAlert('Ошибка загрузки данных маршрута');
            }
        });
    }

    function checkVehicleExists(vehicleNumber, currentRouteId) {
        $.ajax({
            url: ctx + '/company/routes/check-vehicle',
            method: 'GET',
            data: {
                vehicleNumber: vehicleNumber,
                excludeRouteId: currentRouteId
            },
            dataType: 'json',
            success: function(response) {
                if (response.exists) {
                    showError('Этот номер ТС уже используется в другом маршруте');
                } else {
                    addVehicleToList(vehicleNumber);
                }
            },
            error: function () {
                showError('Ошибка проверки существования номера ТС');
            }
        });
    }

    function addVehicleToList(vehicleNumber) {
        if (!vehicleNumbers.includes(vehicleNumber)) {
            vehicleNumbers.push(vehicleNumber);
        }

        const vehicleItem = $(`
            <div class="vehicle-item" data-vehicle="${vehicleNumber}">
                <span class="vehicle-number">${vehicleNumber}</span>
                <button type="button" class="remove-vehicle-btn" title="Удалить">
                    <i class="bi bi-x-lg"></i>
                </button>
            </div>
        `);

        $('#vehiclesList').append(vehicleItem);
        $('#noVehiclesMsg').hide();
        $('#vehicleNumber').val('');

        updateVehiclesData();
        updateSubmitButton();
        hideFormAlert();
    }

    function saveRoute(routeId) {
        $.ajax({
            url: ctx + '/company/routes/' + routeId,
            method: 'POST',
            data: {
                vehicles: JSON.stringify(vehicleNumbers)
            },
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    window.location.href = ctx + '/company/dashboard';
                } else {
                    showFormAlert(response.message || 'Ошибка сохранения');
                }
            },
            error: function () {
                showFormAlert('Ошибка при сохранении маршрута');
            }
        });
    }

    function deleteRoute(routeId) {
        $.ajax({
            url: ctx + '/company/routes/' + routeId + '/delete',
            method: 'POST',
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    window.location.href = ctx + '/company/dashboard';
                } else {
                    confirmModal.hide();
                    showFormAlert(response.message || 'Ошибка удаления');
                }
            },
            error: function () {
                confirmModal.hide();
                showFormAlert('Ошибка при удалении маршрута');
            }
        });
    }

    function resetRouteSelect() {
        $('#routeSelect').empty()
            .append('<option value="" selected disabled>Сначала выберите город и тип</option>')
            .prop('disabled', true);
        $('#editSection').hide();
    }

    function updateVehiclesData() {
        $('#vehiclesData').val(JSON.stringify(vehicleNumbers));
    }

    function updateSubmitButton() {
        const submitBtn = $('#submitBtn');
        if (vehicleNumbers.length === 0) {
            submitBtn.prop('disabled', true);
            submitBtn.find('i').removeClass('bi-check-circle').addClass('bi-lock');
        } else {
            submitBtn.prop('disabled', false);
            submitBtn.find('i').removeClass('bi-lock').addClass('bi-check-circle');
        }
    }

    function showError(message) {
        $('#vehicleError').text(message);
        $('#vehicleNumber').addClass('is-invalid');
    }

    function showFormAlert(message) {
        $('#formAlertText').text(message);
        $('#formAlert').css('display', 'block').addClass('show');

        setTimeout(function() {
            hideFormAlert();
        }, 5000);
    }

    function hideFormAlert() {
        $('#formAlert').removeClass('show').fadeOut(300, function() {
            $(this).css('display', 'none');
        });
    }
});
