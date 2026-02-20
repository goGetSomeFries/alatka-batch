function initTable() {
    $("#resetButton").click(function () {
        $("#searchForm")[0].reset();
    })

    $("#searchButton").click(function () {
        refresh();
    })

    interact('.modal-dialog').draggable({
        allowFrom: '.modal-header',
        modifiers: [
            interact.modifiers.restrictRect({
                restriction: 'parent'
            })
        ],
        listeners: {
            move: function (event) {
                const target = event.target;
                const x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx;
                const y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

                target.style.transform = `translate(${x}px, ${y}px)`;
                target.setAttribute('data-x', x);
                target.setAttribute('data-y', y);
            }
        }
    });

    $("#dataTable").bootstrapTable({
        undefinedText: '',
        pageList: [10, 25, 50, 100],
        onLoadError: function (status) {
            showErrorToast(`接口请求失败, http code: ${status}`);
        },

        queryParams: function (params) {
            const formData = new FormData($('#searchForm')[0]);
            const searchData = {};
            formData.forEach((value, key) => {
                if (searchData.hasOwnProperty(key)) {
                    if (!Array.isArray(searchData[key])) {
                        searchData[key] = [searchData[key]];
                    }
                    searchData[key].push(value);
                } else if (value) {
                    searchData[key] = value;
                }
            });

            return {
                pageNo: params.offset / params.limit + 1,
                pageSize: params.limit,
                orderBy: params.sort,
                direction: params.order,
                ...searchData
            }
        },

        responseHandler: function (res) {
            if (res.code !== "0000") {
                showErrorToast(`接口响应失败: ${res.msg}`);
                return {
                    total: 0,
                    rows: []
                };
            }
            return {
                total: res.totalRecords,
                rows: res.data
            };
        }
    })
}

function showEditModal(url, created) {
    if (created) {
        $('#editForm')[0]?.reset();
        $('#editForm input[type="hidden"]').val('');
    } else {
        const selection = $('#dataTable').bootstrapTable('getSelections');
        if (selection.length !== 1) {
            showWarningToast("请选择一条记录进行编辑");
            return;
        }

        const row = selection[0];
        Object.keys(row).forEach(field => {
            const $input = $(`#editForm [name="${field}"], #editForm #${field}`);
            if ($input.length) {
                let value = row[field];
                if (typeof value === 'boolean') {
                    value = value ? 'true' : 'false';
                }
                $input.val(value);
            }
        });
    }

    $('#editModal').modal('show');
    $('#saveEditBtn').off('click').on('click', function (event) {
        const formData = {};
        const $editForm = $('#editForm');
        if (!$editForm[0].checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
            $editForm.addClass('was-validated');
        } else {
            $editForm.serializeArray().forEach(item => {
                formData[item.name] = item.value === '' ? null : item.value;
            });
            submitFunction(url, created ? 'POST' : 'PUT', formData, created ? '新增' : '更新');
            $('#editModal').modal('hide');
        }
    });
}

function showDeleteModal(url) {
    const selections = $('#dataTable').bootstrapTable('getSelections');
    if (selections.length !== 1) {
        showWarningToast("请选择一条记录");
        return;
    }

    $('#deleteModal').modal('show');
    $('#saveDeleteBtn').off('click').on('click', function () {
        const ids = selections.map(row => row.id);
        submitFunction(`${url}?id=${ids[0]}`, 'DELETE', null, '删除');
        $('#deleteModal').modal('hide');
    });
}

function submitFunction(url, methodType, data, actionName) {
    httpClient(url, methodType, data, function (data) {
        showSuccessToast(`${actionName}成功`);
        refresh();
    });
}

function refresh() {
    $('#dataTable').bootstrapTable('refreshOptions', {
        pageNumber: 1,
        sortOrder: "desc",
        sortName: "id"
    });
}

function enabledFormatter(arg) {
    if (arg === true) {
        return "正常";
    }
    if (arg === false) {
        return "禁用";
    }
    return "/";
}