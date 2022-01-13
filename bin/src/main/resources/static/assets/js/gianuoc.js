(function(){
	 $.ajax({
	        url: URL + 'api/price-water',
	        type: 'GET',
	        dataType: 'json',
	        success: function (result) {
	        	table(result.data)
	        },
	        error: function (error) {
	            sweetalert(error.status)
	        }
	    });
})()

let table = (data) => {
    // < ----------------------- load data to table  ------------------------------->
    $('#my-table').DataTable({
        fixedColumns:   {leftColumns: 1, rightColumns: 1},
        "scrollCollapse": true,
        "paging": true,
        "serverSize": true,
        "lengthMenu": [[5, 25, 50, -1], [5, 25, 50, "All"]],
        "responsive": true,
        "scroller": true,
        "autoWidth": true,
        "processing": true,
        "scrollY": "250px",
       // "sAjaxSource": URL + 'api/price-water',
        "sAjaxDataProp": "",
        "aaData": data,
        "order": [[0, "asc"]],
        "aoColumns": [
            {"mData": "id"},
            {"mData": "date"},
            {"mData": "price"},
            {"mData": "employee.fullName"},
            {"mData": "note"},
            {
                "mRender": function (data, type, full) {
                    return `<i  class="material-icons icon-table icon-update" onclick='showFormUpdate(${full.id},this)' type="button">edit</i>`
                }
            },
            {
                "mRender": function (data, type, full) {
                    return `<i  class="material-icons icon-table icon-delete " onclick='deletePrice(${full.id},this)' type="button">delete</i>`
                }
            }
        ]
    });

}


// < ----------------------------- Delete ---------------------------->
let deletePrice = (id, e) => {
    Swal.fire({
        title: 'Warning',
        text: "Bạn có chắc chắn muốn xóa không!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: 'DELETE',
                url: URL + `api/price-water/${id}`,
                contentType: "application/json",
                cache: false,
                success: function (result) {
                    $('#my-table').DataTable().row($(e).parents('tr'))
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

let changetitle = () => {
    document.querySelector('#form-label').innerHTML = "<i class='fas fa-tint mr-3'></i>" +'Thêm Giá Mới'
}


var index = -1;
// < -------------------------- show form update --------------------->
let showFormUpdate = (id, e) => {
    index = $('#my-table').DataTable().row($(e).parents('tr')).index();
    $('#form-building').modal('show')
    document.querySelector('.modal-title').innerHTML =  "<i class='fas fa-tint mr-3'></i>" +"Cập nhập giá nước";
    $.ajax({
        url: URL + `api/price-water/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            fillToForm(result.data)
        },
        error: function (error) {
        	sweetalertError(error);	
        }
    });
}


// < ------------------------ insert or update  ---------------------->
document.querySelector('#save').addEventListener('click', () => {
    let water = getValueForm();
    if (validate(water)) {
        if (water.id) {
            $.ajax({
                type: 'PUT',
                url: URL + `api/price-water/${water.id}`,
                contentType: "application/json",
                dataType: 'json',
                cache: false,
                data: JSON.stringify(water),
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
                url: URL + `api/price-water`,
                contentType: "application/json",
                dataType: 'json',
                cache: false,
                data: JSON.stringify(water),
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


// <------------- When modal close -> clean form modal  ----------->
$("#form-building").on("hidden.bs.modal", function () {
    cleanForm();
});


// < ---------------------------------------- Clean form ---------------------------->
let cleanForm = () => {
    fillToForm({
        "id": "",
        "price": "",
        "date": "",
        "note": ""
    });
}


// < -------------- clean form when click button clean ------------>
document.querySelector('#clean-form').addEventListener('click', cleanForm);


// < ------------------- get value form --------------------------->
let getValueForm = () => {
    return {
        "id": document.querySelector('#id').value.trim(),
        "price": document.querySelector('#price').value.trim(),
        "date": document.querySelector('#date').value.trim(),
        "employee": {
            "id": 1   // set mặc định là nv id = 1  sau lm phần đăng nhập rồi get id sau
        },
        "note": document.querySelector('#note').value.trim()
    }
}


// < ------------------- fill to form------------------------------>
let fillToForm = (water) => {
    document.querySelector('#id').value = water.id;
    document.querySelector('#price').value = water.price;
    document.querySelector('#date').value = water.date;
    document.querySelector('#note').value = water.note;
}


// < ------------------- validate ----------------------------->
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


