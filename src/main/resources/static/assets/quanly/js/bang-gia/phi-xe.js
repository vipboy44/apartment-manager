(function () {
    $.ajax({
        url: URL + `api/price-parking`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            table(result.data)
        },
        error: function (error) {
            sweetalertError(error);
        }
    });
})()


let table = (data) => {

    // < ----------------------- load data to table  ------------------------------->
    $('#my-table').DataTable({
        fixedColumns: {leftColumns: 1, rightColumns: 1},
        fixedHeader: true,
        "paging": true,
        "serverSize": true,
        "lengthMenu": [[5, 25, 50, -1], [5, 25, 50, "All"]],
        "responsive": true,
        "autoWidth": false,
        "processing": true,
        "sAjaxDataProp": "",
        "aaData": data,
        "order": [[0, "asc"]],
        "aoColumns": [
            {"mData": "id"},
            {"mData": "date"},
            {"mData": "typeVehicel.name"},
            {"mData": "price"},
            {"mData": "employee.username"},
            {"mData": "note"},
            {
                "mRender": function (data, type, full) {
                    return `<button onclick='showFormUpdate(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-primary btn-lg" data-original-title="Edit Task">
                        <i class="fa fa-edit"></i> </button>`
                }
            },
            {
                "mRender": function (data, type, full) {
                    return `<button onclick='deletePrice(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-danger" data-original-title="Remove">
                        <i class="fa fa-times"></i> </button>`
                }
            }
        ]
    });
}

//< ----------------------------- Delete ---------------------------->
let deletePrice = (id, e) => {
    Swal.fire({
        title: 'Warning',
        text: "Bạn có chắc chắn muốn xóa không!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#d33',
        cancelButtonText: 'Hủy bỏ',
        confirmButtonText: 'Xác nhận'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: 'DELETE',
                url: URL + `api/price-parking/${id}`,
                contentType: "application/json",
                cache: false,
                success: function (result) {
                    $('#my-table').DataTable().row($(e).parents('tr')) // format date
                        .remove().draw();
                    sweetalertSuccess(result.message);
                },
                error: function (error) {
                    sweetalertError(error);
                }
            });
        }
    })
}


let fillToForm = (water) => {
    document.querySelector('#price').value = water.price;
    document.querySelector('#date').value = water.date;
    document.querySelector('#type').value = water.typeVehicel.id;
    document.querySelector('#note').value = water.note;
}

let changetitle = () => {
    document.querySelector('#form-label').innerHTML = "<i class='fas fa-car mr-3'></i>" + 'Thêm Giá Mới'
}


var index = -1;
//< -------------------------- show form update --------------------->
let showFormUpdate = (id, e) => {
    index = $('#my-table').DataTable().row($(e).parents('tr')).index();
    $('#form-building').modal('show')
    document.querySelector('#form-label').innerHTML = "<i class='fas fa-car mr-3'></i>" + "Cập nhật phí gửi xe"
    $.ajax({
        url: URL + `api/price-parking/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            document.querySelector('#id').value = result.data.id;
            fillToForm(result.data)
        },
        error: function (error) {
            sweetalertError(error);
        }
    });
}


document.querySelector('#save').addEventListener('click', () => {
    let price = getValueForm();
    if (validate(price)) {
        if (price.id) {
            $.ajax({
                type: 'PUT',
                url: URL + `api/price-parking/${price.id}`,
                contentType: "application/json",
                dataType: 'json',
                cache: false,
                data: JSON.stringify(price),
                success: function (result) {
                    // Convert date to yy-MM-dd
                    result.data.date = formatDate(result.data.date);
                    //update the row in dataTable
                    $('#my-table').DataTable().row(index).data(result.data).draw();
                    // close modal
                    $('#form-building').modal('hide');
                    // annount
                    sweetalertSuccess(result.message);
                },
                error: function (error) {
                    sweetalertError(error);
                }
            });

        } else {
            $.ajax({
                type: 'POST',
                url: URL + `api/price-parking`,
                contentType: "application/json",
                dataType: 'json',
                cache: false,
                data: JSON.stringify(price),
                success: function (result) {
                    // Convert date to yy-MM-dd
                    result.data.date = formatDate(result.data.date);
                    // Add new data to DataTable
                    $('#my-table').DataTable()
                        .row.add(result.data).draw().node();
                    // Clean form
                    cleanForm();
                    // annount
                    sweetalertSuccess(result.message);
                },
                error: function (error) {
                    sweetalertError(error);
                }
            });
        }
    }

});


//<------------- When modal close -> clean form modal  ----------->
$("#form-building").on("hidden.bs.modal", function () {
    cleanForm();
    document.querySelector('#id').value = '';
});

// < ---------------------- Clean form ---------------------------->
let cleanForm = () => {
    fillToForm({
        "price": "",
        "date": "",
        "typeVehicel": "",
        "note": ""
    });
    document.querySelector('#type').selectedIndex = 0
}

// < -------------- clean form when click button clean ------------>
document.querySelector('#clean-form').addEventListener('click', cleanForm);


let getValueForm = () => {
    return {
        "id": document.querySelector('#id').value.trim(),
        "price": document.querySelector('#price').value.trim(),
        "date": document.querySelector('#date').value.trim(),
        "employee": {
            "id": ID_NV
        },
        "typeVehicel": {
            "id": document.querySelector('#type').value.trim(),
        },
        "note": document.querySelector('#note').value.trim()
    }
}

let validate = (data) => {
    if (data.price === '') {
        toastrError("Giá không được để trống!");
        document.querySelector('#price').focus();
        return false;
    }
    if (data.price < 0) {
        toastrError("Giá không được âm!");
        document.querySelector('#price').focus();
        return false;
    }
    if (data.date === '') {
        toastrError("Ngày không được để trống!");
        document.querySelector('#date').focus();
        return false;
    }
    if (data.typeVehicel.id === '') {
        toastrError("Chưa chọn loại xe!");
        document.querySelector('#type').focus();
        return false;
    }
    return true;
}

