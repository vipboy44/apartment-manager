(function(){
	 $.ajax({
	        url: URL + `api/price-electricity`,
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
    $('#table-electricity').DataTable({
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
       // "sAjaxSource": URL + 'api/price-electricity',
        "sAjaxDataProp": "",
        "aaData": data,
        "order": [[0, "asc"]],
        "aoColumns": [
            {"mData": "id"},
            {"mData": "limits"},
            {"mData": "price"},
            {"mData": "date"},
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

let deletePrice = (id, e) => {
    swal.fire({
        title: 'Cảnh Báo',
        text: "Bạn chắc chắn muốn xóa",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes'
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
    document.querySelector('#form-label').innerHTML = "<i class='fas fa-bolt mr-3'></i>" +'Thêm Giá Mới'
}

// < ----------------------- show form update ---------------->
var index = -1;
let showFormUpdate = (id, e) => {
    index = $('#table-electricity').DataTable().row($(e).parents('tr')).index();
    $('#form-building').modal('show')
    document.querySelector('.modal-title').innerHTML =  "<i class='fas fa-bolt mr-3 '></i>" + "Cập Nhật Giá Điện";
    $.ajax({
        url: URL + `api/price-electricity/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            fillToForm(result.data)
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
});

// <------------------ clean form ---------------------------->
let cleanForm = () => {
    fillToForm(
        {
            'id': '',
            'limits': '',
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
            'id': 1
        },
        'note': document.querySelector('#note').value.trim()
    }
}

// < ------------- fill to form ---------------------------->
let fillToForm = (electricity) => {
    document.querySelector('#id').value = electricity.id;
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
