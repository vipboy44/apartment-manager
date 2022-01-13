(function () {
    $.ajax({
        url: URL + `api/resident`,
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
    $('#table-resident').DataTable({
        fixedColumns: {leftColumns: 1, rightColumns: 1},
        fixedHeader: true,
        "responsive": true,
        "serverSize": true,
        "lengthMenu": [[5, 25, 50, -1], [5, 25, 50, "All"]],
        "autoWidth": false,
        "processing": true,
        "sAjaxDataProp": "",
        "aaData": data,
        "order": [[0, "asc"]],
        "aoColumns": [
            {"mData": "id"},
            {"mData": "fullname"},
            {
                "mRender": function (data, type, full) {
                    return full.gender ? "Nữ" : "Nam"
                }
            },
            {"mData": "birthday"},
            {"mData": "job"},
            {"mData": "phone"},
            {"mData": "apartment.id"},
            {
                "mRender": function (data, type, full) {
                    return `<button onclick='showFormUpdate(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-primary btn-lg" data-original-title="cập nhật">
                        <i class="fa fa-edit"></i> </button>`
                }
            },
            {
                "mRender": function (data, type, full) {
                    return `<button onclick='deleteResident(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-danger" data-original-title="xóa">
                        <i class="fa fa-times"></i> </button>`
                }
            },
            {
                "mRender": function (data, type, full) {
                    return `<button onclick='showFormResgiterVehicle(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-success" data-original-title="thêm">
                        <i class="fa fa-plus"></i> </button>`
                }
            }
        ]
    });
}


// <---------------------------------- Delete --------------------------->
let deleteResident = (id, e) => {
    swal.fire({
        title: 'Cảnh Báo',
        text: "Bạn có  chắc chắn muốn xóa không!",
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
                url: URL + `api/resident/${id}`,
                contentType: 'application/json',
                cache: false,
                success: function (result) {
                    $('#table-resident').DataTable().row($(e).parents('tr')).remove().draw();
                    sweetalertSuccess(result.message)
                },
                error: function (error) {
                    sweetalertError(error)
                }
            })
        }
    })
}

// ------------------- change title ------------------------
let changetitle = () => {
    document.querySelector('#form-label').innerHTML =
        "<i class='fas fa-address-card mr-2'></i>" + 'Thêm cư dân'
}

let index2 = -1
let showFormResgiterVehicle = (id , e) => {
index2 = $('#table-resident').DataTable().row($(e).parents('tr')).index();
    $('#form-vehicle').modal('show')
    $.ajax({
        url: URL + `api/resident/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            document.querySelector("#resident_id").value = result.data.id;
        },
        error: function (error) {
            sweetalertError(error)
        }
    })
}

// < ----------------- show form update -------------------->
var index = -1;
let showFormUpdate = (id, e) => {
    index = $('#table-resident').DataTable().row($(e).parents('tr')).index();
    $('#form-resident').modal('show')
    document.querySelector('#form-label').innerHTML =
        "<i class='fas fa-address-card mr-2'></i>" + "Cập nhật thông tin cư dân";
    $.ajax({
        url: URL + `api/resident/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            fillToFrom(result.data)
        },
        error: function (error) {
            sweetalertError(error)
        }
    })
}

// < ------------------------- insert or update -------------------->
document.querySelector('#saveResident').addEventListener('click', () => {
    let resident = getValueForm();
    if (validate(resident)) {
        if (resident.id) {
            // < ------------------- update ---------------------->
            $.ajax({
                type: 'PUT',
                url: URL + `api/resident/${resident.id}`,
                contentType: 'application/json',
                dataType: 'json',
                cache: false,
                data: JSON.stringify(resident),
                success: function (result) {
                    result.data.birthday = formatDate(result.data.birthday);
                    // Convert date to yy-MM-dd
                    $('#table-resident').DataTable().row(index).data(result.data).draw();
                    //update the row in dataTable
                    $('#form-resident').modal('hide');
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
                url: URL + `api/resident`,
                contentType: 'application/json',
                dataType: 'json',
                cache: false,
                data: JSON.stringify(resident),
                success: function (result) {
                    result.data.birthday = formatDate(result.data.birthday);
                    $('#table-resident').DataTable().row.add(result.data).draw().node();
                    sweetalertSuccess(result.message);
                    cleanFrom();
                },
                error: function (error) {
                    sweetalertError(error)
                }
            })
        }
    }
})


//< ---------------- clean form when modal close ---------->
$("#form-resident").on("hidden.bs.modal", function () {
    cleanFrom();
    document.querySelector("#id").value = "";
});


// < -------------------- clean form ----- ---------------->
let cleanFrom = () => {
    document.querySelector('#fullname').value = "",
        document.querySelector('#birthday').value = "",
        $('input[name="gender"]').prop('checked', false),
        document.querySelector("#hometown").value = "",
        document.querySelector('#job').value = "",
        document.querySelector('#phone').value = "",
        document.querySelector('#email').value = "",
        document.querySelector('#identitycard').value = "" ;
        $('#apartment_id').val(null).trigger('change');
}

// < -------------- clean form when click button clean ------------>
document.querySelector('#clean-form-resident').addEventListener('click', cleanFrom);


// < ------------------- get value form ------------------------>
let getValueForm = () => {
    return {
        'id': document.querySelector("#id").value.trim(),
        'fullname': document.querySelector('#fullname').value.trim(),
        'birthday': document.querySelector('#birthday').value.trim(),
        'gender': $("input[name='gender']:checked").val() === 'female',
        'hometown': document.querySelector("#hometown").value.trim(),
        'job': document.querySelector('#job').value.trim(),
        'phone': document.querySelector('#phone').value.trim(),
        'email': document.querySelector('#email').value.trim(),
        'identitycard': document.querySelector('#identitycard').value.trim(),
        'apartment': {	"id": document.querySelector('#apartment_id').value.trim()}
    }


}
// < ------------------ fill to form -------------------------->
let fillToFrom = (resident) => {
    document.querySelector("#id").value = resident.id;
    document.querySelector('#fullname').value = resident.fullname;
    document.querySelector('#birthday').value = resident.birthday;
    $(resident.gender ? "#female" : "#male").prop('checked', true);
    document.querySelector("#hometown").value = resident.hometown;
    document.querySelector('#job').value = resident.job;
    document.querySelector('#phone').value = resident.phone;
    document.querySelector('#email').value = resident.email;
    document.querySelector('#identitycard').value = resident.identitycard;
    // document.querySelector('#apartment_id').value = resident.apartment.id
    $('#apartment_id').val(resident.apartment.id).trigger('change');
}
let validate = (data) => {
    if (data.fullname === '') {
        toastrError("Họ tên không được để trống!");
        document.querySelector('#fullname').focus();
        return false;
    }
    if (data.birthday === '') {
        toastrError("Ngày sinh không được để trống");
        document.querySelector('#birthday').focus();
        return false
    }
    if (!$('input[name=gender]:checked').val()) {
        toastrError("Chưa chọn giới tính");
        return false
    }

    if (data.identitycard != '') {
        if (isNaN(data.identitycard)) {
            toastrError("Số chứng minh - căn cước công dân phải là số!");
            document.querySelector('#identityCard').focus();
            return false;
        }
        if (data.identitycard.length < 9 || data.identitycard.length > 12) {
            toastrError("Số chứng minh - căn cước công dân 9 hoặc  12 chữ số!");
            document.querySelector('#identityCard').focus();
            return false;
        }
    }
    if (data.phone === '') {
        toastrError("Số điện thoại không được để trống!");
        document.querySelector('#phone').focus();
        return false;
    }
    if (isNaN(data.phone)) {
        toastrError("Số điện thoại phải là số!");
        document.querySelector('#phone').focus();
        return false;
    }
    var vnf_regex = /((09|03|07|08|05)+([0-9]{7,8})\b)/g;
    if (!vnf_regex.test(data.phone)) {
        toastrError("Số điện thoại sai định dạng!");
        document.querySelector('#phone').focus();
        return false;
    }
    if (data.phone.length < 9 || data.phone.length > 11) {
        toastrError("Số điện thoại phải từ 9 đến 11 chữ số!");
        document.querySelector('#phone').focus();
        return false;
    }
    if (data.job === '') {
        toastrError("Nghề nghiệp không được để trống!");
        document.querySelector('#job').focus();
        return false
    }
    if (data.hometown === '') {
        toastrError("Quê quán không được để trống!");
        document.querySelector('#hometown').focus();
        return false
    }
    if (data.email != '') {
        var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if (!filter.test(data.email)) {
            toastrError("Email sai định dạng!");
            document.querySelector('#email').focus();
            return false;
        }
    }
    if (data.apartment.id === '') {
        toastrError("Mã căn hộ không được để trống!");
        document.querySelector('#apartment_id').focus();
        return false

    }
    return true;
}


