$(document).ready(function() {
    select2Config = {
        theme: 'bootstrap-5',
        language: 'ru',
        width: '100%',
    }

    const MAX_FILE_SIZE = 10 * 1024 * 1024;

    const now = new Date();
    const offset = now.getTimezoneOffset() * 60000;
    const localTime = new Date(now - offset).toISOString().slice(0, 16);
    $('#rideTime').val(localTime);

    $('#citySelect').select2($.extend({}, select2Config, {
        placeholder: 'Выберите город',
        minimumResultsForSearch: Infinity,
        allowClear: false
    }));

    $('#transportSelect').select2($.extend({}, select2Config, {
        placeholder: 'Выберите тип транспорта',
        minimumResultsForSearch: Infinity,
        allowClear: false
    }));

    $('#routeSelect').select2($.extend({}, select2Config, {
        placeholder: 'Сначала выберите город и тип транспорта',
        allowClear: false
    }));

    $('#vehicleSelect').select2($.extend({}, select2Config, {
        placeholder: 'Сначала выберите маршрут',
        allowClear: false
    }));

    $('#citySelect, #transportSelect').on('change', function () {
        const cityId = $('#citySelect').val();
        const transportId = $('#transportSelect').val();

        resetSelect('#routeSelect', 'Выберите маршрут');
        resetSelect('#vehicleSelect', 'Выберите ТС');

        if (cityId && transportId) {
            loadRoutes(cityId, transportId);
        } else {
            $('#routeSelect').prop('disabled', true);
            $('#vehicleSelect').prop('disabled', true);
        }

    })

    function loadRoutes(cityId, transportId) {
        $.ajax({
            url: ctx + "/api/routes",
            method: 'GET',
            dataType: 'json',
            data: {
                cityId: cityId,
                transportModeId: transportId
            },
            success: function (routes) {
                $('#routeSelect').select2('destroy');
                $('#routeSelect').empty();
                $('#routeSelect').append('<option value="">Выберите маршрут</option>');

                routes.forEach(function (route) {
                    $('#routeSelect').append(
                        new Option(route.number, route.id, false, false)
                    )
                })

                $('#routeSelect').select2($.extend({}, select2Config, {
                    placeholder: 'Выберите маршрут',
                    allowClear: false
                }))

                $('#routeSelect').prop('disabled', false);
            },
            error: function (jqXHR) {
                showAlert('Ошибка загрузки маршрутов')
            }
        })
    }

    $('#routeSelect').on('change', function () {
        routeId = $(this).val();

        resetSelect('#vehicleSelect', 'Выберите ТС');
        if (routeId) {
            loadVehicles(routeId);
        }  else {
            $('#vehicleSelect').prop('disabled', true);
        }
    })

    function loadVehicles(routeId) {
        $.ajax({
            url: ctx + '/api/vehicles',
            method: 'GET',
            dataType: 'json',
            data: {
                routeId: routeId
            },
            success: function (vehicles) {
                $('#vehicleSelect').select2('destroy');

                $('#vehicleSelect').empty();
                $('#vehicleSelect').append('<option value="">Выберите ТС</option>');

                vehicles.forEach(function (vehicle) {
                    $('#vehicleSelect').append(
                        new Option(vehicle, vehicle, false, false)
                    )
                })

                $('#vehicleSelect').select2($.extend({}, select2Config, {
                    placeholder: 'Выберите ТС',
                    allowClear: false
                }));

                $('#vehicleSelect').prop('disabled', false);
            },
            error: function(jqXHR) {
                showAlert('Ошибка загрузки транспортных средств');
            }
        })
    }

    function resetSelect(selector, placeholder) {
        $(selector).select2('destroy');
        $(selector).empty();
        $(selector).append('<option value="">' + placeholder + "</option>");
        $(selector).select2($.extend({}, select2Config, {
            placeholder: placeholder,
            allowClear: false
        }))
        $(selector).prop('disabled', true);
    }

    function showAlert(message) {
        $('#formAlertText').text(message);
        $('#formAlert').addClass('show').show();

        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });

        setTimeout(function () {
            $('#formAlert').removeClass('show').fadeOut(150);
        }, 5000);
    }

    $('#photoUpload').on('change', function() {
        const file = this.files[0];

        if (file) {
            if (file.size > MAX_FILE_SIZE) {
                showAlert('Файл слишком большой. Максимальный размер: 5 МБ');
                $(this).val('');
                return;
            }

            const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];
            if (!allowedTypes.includes(file.type)) {
                showAlert('Недопустимый формат. Разрешены: JPG, JPEG, PNG');
                $(this).val('');
                return;
            }
        }
    });

    $('#reviewForm').on('submit', function (e) {
       e.preventDefault();

       const selectedTags = [];
       $('input[type="radio"]:checked').each(function() {
          const value = $(this).val();
          if (value) {
              selectedTags.push(value);
          }
       });

       if (selectedTags.length === 0) {
           showAlert('Дайте оценку хотя бы одному из показателей');
           return false;
       }

       $('#selectedTags').val(selectedTags.join(','));

       this.submit();
    });

});