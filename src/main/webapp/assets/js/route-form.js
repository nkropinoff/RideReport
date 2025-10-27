let vehicleNumbers = [];

$(document).ready(function () {
    updateSubmitButton();

    $('#routeNumber').on('blur', function() {
        const routeNumber = $(this).val().trim();
        const cityId = $('#citySelect').val();

        if (routeNumber && cityId) {
            checkRouteNumberExists(routeNumber, cityId);
        }
    });

    $('#citySelect').on('change', function() {
        $('#routeNumber').removeClass('is-invalid');
        const routeNumber = $('#routeNumber').val().trim();
        const cityId = $(this).val();

        if (routeNumber && cityId) {
            checkRouteNumberExists(routeNumber, cityId);
        }
    });

    $('#addVehicleBtn').click( function () {
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

        checkVehicleExists(vehicleNumber);
    });

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

    function checkRouteNumberExists(routeNumber, cityId) {
        $.ajax({
            url: ctx + '/company/routes/check-route-number',
            method: 'GET',
            data: {
                routeNumber: routeNumber,
                cityId: cityId
            },
            dataType: 'json',
            success: function(response) {
                const routeInput = $('#routeNumber');
                if (response.exists) {
                    routeInput.addClass('is-invalid');
                    routeInput.siblings('.invalid-feedback').text('Маршрут с таким номером уже существует в этом городе');
                } else {
                    routeInput.removeClass('is-invalid');
                }
            },
            error: function () {
                console.error('Ошибка проверки номера маршрута');
            }
        });
    }

    function checkVehicleExists(vehicleNumber) {
        $.ajax({
            url: ctx + '/company/routes/check-vehicle',
            method: 'GET',
            data: { vehicleNumber: vehicleNumber },
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
        vehicleNumbers.push(vehicleNumber);

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

    $('#routeForm').submit(function(e) {
        e.preventDefault();

        const form = this;

        if (!form.checkValidity()) {
            e.stopPropagation();
            form.classList.add('was-validated');
            return false;
        }

        if ($('#routeNumber').hasClass('is-invalid')) {
            showFormAlert('Исправьте ошибки в форме');
            return false;
        }

        if (vehicleNumbers.length === 0) {
            showFormAlert('Добавьте хотя бы одно транспортное средство');
            return false;
        }

        form.submit();
    });
});
