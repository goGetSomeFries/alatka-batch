function initFlatPickr($containerId) {
    const fp = flatpickr($containerId, {
        locale: {
            ...flatpickr.l10ns.zh,
            rangeSeparator: '~'
        },
        mode: "range",
        enableTime: true,
        enableSeconds: true,
        time_24hr: true,
        dateFormat: "Y-m-d H:i:S",
        defaultHour: 0,
        defaultMinute: 0,
        onClose: function (selectedDates, dateStr, instance) {
            if (selectedDates.length === 1) {
                let startTime = new Date(selectedDates[0]);
                let endTime = new Date();
                instance.setDate([startTime, endTime], false);
            }
        }
    });
}

function initStatusSelect() {
    let tomSelect = new TomSelect('#status', {
        plugins: ['remove_button'],
        placeholder: '选择',
        maxItems: null,
        render: {
            option: function (data, escape) {
                return `<div>${data.text}</div>`;
            },
            item: function (data, escape) {
                return `<div>${data.text}</div>`;
            }
        }
    });
    httpClient('/batch/monitor/job/status/list', 'GET', null, function (data) {
        data.forEach((value) => {
            tomSelect.addOption({
                value: value,
                text: statusFormatter(value)
            });
        });
    });
}

function initExitCodeSelect() {
    let tomSelect = new TomSelect('#exitCode', {
        plugins: ['remove_button'],
        placeholder: '选择',
        maxItems: null,
        create: function (input) {
            return {
                value: input,
                text: statusFormatter(input)
            }
        },
        createOnBlur: true,
        render: {
            option: function (data, escape) {
                return `<div>${data.text}</div>`;
            },
            item: function (data, escape) {
                return `<div>${data.text}</div>`;
            }
        }
    });
    httpClient('/batch/monitor/job/exitCode/list', 'GET', null, function (data) {
        data.forEach((value) => {
            tomSelect.addOption({
                value: value,
                text: statusFormatter(value)
            });
        });
    });
}

function statusFormatter(key) {
    if (!key) {
        return `<span class="badge bg-warning">未知</span>`;
    }
    switch (key) {
        case 'COMPLETED':
            return `<span class="badge bg-success">${key}</span>`;
        case 'FAILED':
            return `<span class="badge bg-danger">${key}</span>`;
        case 'UNKNOWN':
            return `<span class="badge bg-warning">${key}</span>`;
        case 'STARTING':
        case 'STARTED':
        case 'NOOP':
        case 'EXECUTING':
            return `<span class="badge bg-primary">${key}</span>`;
        default:
            return `<span class="badge bg-secondary">${key}</span>`;
    }
}

function exitMessageFormatter(message) {
    if (!message) {
        return "";
    }
    let displayMessage = message.length > 80 ? message.substring(0, 80) + "..." : message;
    const $link = $('<a></a>')
        .attr('href', 'javascript:void(0)')
        .addClass('text-danger text-decoration-none')
        .attr('data-content', message)
        .attr('onclick', 'exitMessageFunction(this)')
        .text(displayMessage);

    return $link[0].outerHTML;
}

function exitMessageFunction(element) {
    const value = $(element).attr('data-content');
    $('#exitMessageModal code').text(value);
    $('#exitMessageModal').modal('show');
}