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
        maxItems: null,
        render: {
            option: function (data, escape) {
                return `<div>${data.badgeHtml}</div>`;
            },
            item: function (data, escape) {
                return `<div>${data.badgeHtml}</div>`;
            }
        }
    });
    httpClient('/batch/monitor/job/status/list', 'GET', null, function (data) {
        data.forEach((value) => {
            tomSelect.addOption({
                value: value,
                text: value,
                badgeHtml: statusFormatter(value)
            });
        });
    });
}

function statusFormatter(arg) {
    if (!arg) {
        return '';
    }
    let bg = mapping(arg);
    return `<span class="badge ${bg}">${arg}</span>`;
}

function mapping(code) {
    switch (code) {
        case 'COMPLETED':
            return 'bg-success';
        case 'FAILED':
            return 'bg-danger';
        case 'UNKNOWN':
            return 'bg-warning';
        case 'STARTING':
        case 'STARTED':
            return 'bg-primary';
        default:
            return 'bg-secondary';
    }
}
