(function () {
    $.ajax({
        url: URL + 'api/vehicle',
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            table_vihecle(result.data)
        },
        error: function (error) {
        	sweetalertError(error.status)
        }
    });
})()


let table_vihecle = (data) => {
    // < ----------------------- load data to table  ------------------------------->
    $('#table-vehicle').DataTable({
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
            {"mData": "licensePlates"},
            {"mData": "color"},
            {"mData": "date"},
            {"mData": "resident.id"},
            {"mData": "typeVehicle.name"},
            {
                "mRender": function (data, type, full) {
                    return `<button onclick='showFormUpdateVehicle(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-primary btn-lg" data-original-title="Edit Task">
                        <i class="fa fa-edit"></i> </button>`
                }
            },
            {
                "mRender": function (data, type, full) {
                    return `<button onclick='deleteVehicle(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-danger" data-original-title="Remove">
                        <i class="fa fa-times"></i> </button>`
                }
            }
        ]
    });
}

// < ---------------------------- Delete --------------------->
let deleteVehicle = (id, e) => {
    swal.fire({
        title: 'Cảnh Báo',
        text: "Bạn chắc chắn muốn xóa Không!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#d33',
        cancelButtonText: 'Hủy bỏ',
        confirmButtonText: 'Xác nhận'
    }).then((reslut) => {
        if (reslut.value) {
            $.ajax({
                type: 'DELETE',
                url: URL + `api/vehicle/${id}`,
                contentType: 'application/json',
                cache: false,
                success: function (result) {
                    $('#table-vehicle').DataTable().row($(e).parents('tr')).remove().draw();
                    sweetalertSuccess(result.message)
                },
                error: function (error) {
                    sweetalertError(error)
                }
            })
        }
    })
}



// < ------------------------- insert or update -------------------->
document.querySelector('#save-vehicle').addEventListener('click', () => {
    let vehicle = getValueFormVehicle();
    if (validateFormVehicle(vehicle)) {
        if (vehicle.id) {
            // < ------------------- update ---------------------->
            $.ajax({
                type: 'PUT',
                url: URL + `api/vehicle/${vehicle.id}`,
                contentType: 'application/json',
                dataType: 'json',
                cache: false,
                data: JSON.stringify(vehicle),
                success: function (result) {
                    result.data.date = formatDate(result.data.date);
                    // Convert date to yy-MM-dd           
                    $('#table-vehicle').DataTable().row(index).data(result.data).draw();
                    //update the row in dataTable
                    $('#form-vehicle').modal('hide');
                    // close modal
                    sweetalertSuccess(result.message)
                },
                error: function (error) {
                    sweetalertError(error)
                }
            })
        } else {
            // < ----------------------- create ----------------->
            $.ajax({
                type: 'POST',
                url: URL + `api/vehicle`,
                contentType: 'application/json',
                dataType: 'json',
                cache: false,
                data: JSON.stringify(vehicle),
                success: function (result) {
                    result.data.date = formatDate(result.data.date)
                    $('#table-vehicle').DataTable().row.add(result.data).draw().node();
                    sweetalertSuccess(result.message)
                    cleanFormVehicle();
                },
                error: function (error) {
                    sweetalertError(error)
                }
            })
        }
    }
})


// < -------------------- show form upate ------------->
var index = -1;
let showFormUpdateVehicle = (id, e) => {
    index = $('#table-vehicle').DataTable().row($(e).parents('tr')).index();
    $('#form-vehicle').modal('show')
    document.querySelector('.modal-title').innerHTML =
        "  <i class='fas fa-motorcycle mr-2 '></i>" + "Cập nhật thông tin xe  ";
    $.ajax({
        url: URL + `api/vehicle/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            fillToFormVehicle(result.data)
        },
        error: function (error) {
            sweetalertError(error)
        }
    })
}

//<------------- When modal close -> clean form modal  ----------->
$("#form-vehicle").on("hidden.bs.modal", function () {
    document.querySelector('#idVehicle').value = "",
        cleanFormVehicle();
});

// < ---------------------- Clean form ---------------------------->
let cleanFormVehicle = () => {
    //document.querySelector('#idVehicle').value = "",
    document.querySelector('#licensePlates').value = "";
    document.querySelector('#color').value = "";
    document.querySelector('#date').value = "";
    $('#resident_id').val(null).trigger('change');
    document.querySelector('#type').value = 1
}

// < -------------- clean form when click button clean ------------>
document.querySelector('#clean-form-vehicle').addEventListener('click', cleanFormVehicle);


//<----------  fillToForm------------------->
let fillToFormVehicle = (vehicle) => {
    document.querySelector('#idVehicle').value = vehicle.id;
    document.querySelector('#licensePlates').value = vehicle.licensePlates
    document.querySelector('#color').value = vehicle.color;
    document.querySelector('#date').value = vehicle.date;
    document.querySelector('#resident_id').value = vehicle.resident.id;
    document.querySelector('#type').value = vehicle.typeVehicle.id;
}

let getValueFormVehicle = () => {
    return {
        "id": document.querySelector('#idVehicle').value.trim(),
        "licensePlates": document.querySelector('#licensePlates').value.trim(),
        'color': document.querySelector('#color').value.trim(),
        'date': document.querySelector('#date').value.trim(),
        'resident': {'id': document.querySelector('#resident_id').value.trim()},
        "typeVehicle": {"id": document.querySelector('#type').value.trim()},
    }
}

let validateFormVehicle = (data) => {
    let element = document.querySelector('#type');
    let nameType = element.options[element.selectedIndex].text;

    if (data.typeVehicle.id === '') {
        toastrError("Chưa chọn loại xe!");
        document.querySelector('#type').focus();
        return false;
    }
    if (nameType === 'Xe máy' && data.licensePlates === '') {
        toastrError("Biển số xe không được để trống! ")
        document.querySelector('#licensePlates').focus()
        return false
    }
    if (nameType === 'Xe ô tô' && data.licensePlates === '') {
        toastrError("Biển số xe không được để trống! ")
        document.querySelector('#licensePlates').focus()
        return false
    }
    if (data.licensePlates != '') {
        if (data.licensePlates.length < 9 || data.licensePlates.length > 10) {
            toastrError("Biển số xe phải từ 9 hoặc 10 kí tự!")
            document.querySelector('#licensePlates').focus()
            return false
        }
    }

    if (data.date === '') {
        toastrError("Ngày đăng ký không được để trống!");
        document.querySelector('#date').focus();
        return false
    }
    if (data.color === '') {
        toastrError("Màu xe không được để trống!");
        document.querySelector('#color').focus();
        return false
    }

    // if (data.resident.id === '') {
    //     toastrError("Mã cư dân không được để trống!");
    //     document.querySelector('#resident_id').focus();
    //     return false
    // }
    // if (data.resident != null && isNaN(data.resident.id)) {
    //     toastrError("Id cư dân phải là số!");
    //     document.querySelector('#idResident').focus();
    //     return false;
    // }

    return true;
}