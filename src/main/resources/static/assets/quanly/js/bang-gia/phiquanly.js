(function(){
	 $.ajax({
	        url: URL + 'api/price-management',
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
    $('#table-phiquanly').DataTable({
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

let fillToForm = (management) => {
    document.querySelector('#price').value = management.price;
    document.querySelector('#date').value = management.date;
    document.querySelector('#note').value = management.note;
}

let changetitle = () => {
    document.querySelector('#form-label').innerHTML = "<i class='fas fa-shield-alt mr-3'></i>" +'Thêm phí quản lý'
}
var index = -1;
//< -------------------------- show form update --------------------->
let showFormUpdate = (id, e) => {
    index = $('#table-phiquanly').DataTable().row($(e).parents('tr')).index();
    $('#form-building').modal('show')
    document.querySelector('#form-label').innerHTML = "<i class='fas fa-shield-alt mr-3'></i>" + "Cập nhật phí quản lý";
    $.ajax({
        url: URL + `api/price-management/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            fillToForm(result.data)
            document.querySelector('#id').value = result.data.id;
        },
        error: function (error) {
        	sweetalertError(error)
        }
    });
}

//< ------------------------ insert or update  ---------------------->
document.querySelector('#save').addEventListener('click', () => {
    let management = getValueForm();
    if (validate(management)) {
        if (management.id) {
            $.ajax({
                type: 'PUT',
                url: URL + `api/price-management/${management.id}`,
                contentType: "application/json",
                dataType: 'json',
                cache: false,
                data: JSON.stringify(management),
                success: function (result) {
                    result.data.date = formatDate(result.data.date);  // Convert date to yy-MM-dd
                    $('#table-phiquanly').DataTable().row(index).data(result.data).draw();  //update the row in dataTable
                    $('#form-building').modal('hide');     // close modal
                    sweetalertSuccess(result.message)
                },
                error: function (error) {
                	sweetalertError(error)
                }
            });

        } else {
            $.ajax({
                type: 'POST',
                url: URL + `api/price-management`,
                contentType: "application/json",
                dataType: 'json',
                cache: false,
                data: JSON.stringify(management),
                success: function (result) {
                    // Convert date to yy-MM-dd
                	result.data.date = formatDate(result.data.date);
                    // Add new data to DataTable
                    $('#table-phiquanly').DataTable()
                        .row.add(result.data).draw().node();
                    // Clean form
                    cleanForm();
                    // message
                    sweetalertSuccess(result.message)
                },
                error: function (error) {
                	sweetalertError(error)
                }
            });
        }
    }


});


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
                url: URL + `api/price-management/${id}`,
                contentType: "application/json",
                cache: false,
                success: function (result) {
                    $('#table-phiquanly').DataTable().row($(e).parents('tr')) // format date
                        .remove().draw();
                    sweetalertSuccess(result.message)
                },
                error: function (error) {
                	sweetalertError(error)
                }
            });
        }
    })
}

//< ---------------------- Clean form ---------------------------->
let cleanForm = () => {
    fillToForm({ 
        "price": "",
        "date": "",
        "note": ""
    });
}

//<------------- When modal close -> clean form modal  ----------->
$("#form-building").on("hidden.bs.modal", function () {
    cleanForm();
    document.querySelector('#id').value = '';
});
// < -------------- clean form when click button clean ------------>
document.querySelector('#clean-form').addEventListener('click', cleanForm);


//< ------------------- get value form --------------------------->
let getValueForm = () => {
    return {
        "id": document.querySelector('#id').value.trim(),
        "price": document.querySelector('#price').value.trim(),
        "date": document.querySelector('#date').value.trim(),
        "employee": {
            "id": ID_NV   // set mặc định là nv id = 1  sau lm phần đăng nhập rồi get id sau
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
    if(data.price < 0){
		toastrError("Giá không được âm!");
		document.querySelector('#price').focus();
		return false;
	}
    if (data.date === '') {
        toastrError("Ngày không được để trống!");
        document.querySelector('#date').focus();
        return false;
    }
    return true;
}



