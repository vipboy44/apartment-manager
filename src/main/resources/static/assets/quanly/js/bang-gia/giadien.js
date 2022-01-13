(function () {
    $.ajax({
        url: URL + `api/price-electricity`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            table(result.data)
        },
        error: function (error) {
        	sweetalertError(error.status)
        }
    });
})()


let table = (data) => {
    // < ----------------------- load data to table  ------------------------------->
    $('#table-electricity').DataTable({
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
            {"mData": "limits"},
            {"mData": "price"},
            {"mData": "date"},
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

let deletePrice = (id, e) => {
    swal.fire({
        title: 'Cảnh Báo',
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
                type: `DELETE`,
                url: URL + `api/price-electricity/${id}`,
                contentType: "application/json",
                cache: false,
                success: function (result) {
                    $('#table-electricity').DataTable().row($(e).parents('tr')).remove().draw(); //
                    sweetalertSuccess(result.message);
                },
                error: function (error) {
                    sweetalertError(error);
                }
            });
        }
    })
}

let changetitle = () => {
    document.querySelector('#form-giadien').innerHTML = "<i class='fas fa-bolt mr-3'></i>" + 'Thêm giá điện'
}

// < ----------------------- show form update ---------------->
var index = -1;
let showFormUpdate = (id, e) => {
    index = $('#table-electricity').DataTable().row($(e).parents('tr')).index();
    $('#form-building').modal('show')
    document.querySelector('#form-giadien').innerHTML = "<i class='fas fa-bolt mr-3 '></i>" + "Cập nhật giá điện";
    $.ajax({
        url: URL + `api/price-electricity/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            fillToForm(result.data)
            document.querySelector('#id').value = result.data.id;
        },
        error: function (error) {
            sweetalertError(error);
        }
    })
}
// < ------------------------ inser or update ---------------->
document.querySelector('#save').addEventListener('click', () => {
    let electricity = getValueForm();
    //< -------------- update --------------->
    if (validate(electricity)) {
        if (electricity.id) {
            $.ajax({
                type: 'PUT',
                url: URL + `api/price-electricity/${electricity.id}`,
                contentType: 'application/json',
                dataType: 'json',
                cache: false,
                data: JSON.stringify(electricity),
                success: function (result) {
                    result.data.date = formatDate(result.data.date);  // Convert date to yy-MM-dd
                    $('#table-electricity').DataTable().row(index).data(result.data).draw();  //update the row in dataTable
                    $('#form-building').modal('hide');     // close modal
                    sweetalertSuccess(result.message);
                },
                error: function (error) {
                    sweetalertError(error);
                }
            })
        }
        // < --------------- insert ---------->
        else {
            $.ajax({
                type: 'POST',
                url: URL + `api/price-electricity`,
                contentType: 'application/json',
                dataType: 'json',
                cache: false,
                data: JSON.stringify(electricity),
                success: function (result) {
                    result.data.date = formatDate(result.data.date);
                    $('#table-electricity').DataTable().row.add(result.data).draw().node();
                    cleanForm();
                    sweetalertSuccess(result.message);
                },
                error: function (error) {
                    sweetalertError(error);
                }

            })
        }
    }
});

// <------------- When modal close -> clean form modal  ----------->
$("#form-building").on("hidden.bs.modal", function () {
    cleanForm();
    document.querySelector('#id').value = '';
});

// <------------------ clean form ---------------------------->
let cleanForm = () => {
    fillToForm(
        {
            'limits': 50,
            'price': '',
            'date': '',
            'note': ''
        }
    )
}
// < -------------- clean form when click button clean ------------>
document.querySelector('#clean-form').addEventListener('click', cleanForm);

// < ---------------- get value form ------------------------->
let getValueForm = () => {
    return {
        'id': document.querySelector('#id').value.trim(),
        'limits': document.querySelector('#limits').value.trim(),
        'price': document.querySelector('#price').value.trim(),
        'date': document.querySelector('#date').value.trim(),
        'employee': {
            'id': ID_NV
        },
        'note': document.querySelector('#note').value.trim()
    }
}

// < ------------- fill to form ---------------------------->
let fillToForm = (electricity) => {
    document.querySelector('#limits').value = electricity.limits;
    document.querySelector('#price').value = electricity.price;
    document.querySelector('#date').value = electricity.date;
    document.querySelector('#note').value = electricity.note;

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
        return false
    }
    if (data.date === '') {
        toastrError("Ngày không được để trống!");
        document.querySelector('#date').focus();
        return false;
    }
    if (data.limits === '') {
        toastrError("Hạn mức không được để trống!")
        document.querySelector('#limits').focus();
        return false
    }
    if (data.limits < 0) {
        toastrError("Hạn mức không được âm!")
        document.querySelector('#limits').focus();
        return false
    }
    return true;
}
