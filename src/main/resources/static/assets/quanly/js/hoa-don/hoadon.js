
(function(){
	 $.ajax({
	        url: URL + 'api/apartment-index',
	        type: 'GET',
	        dataType: 'json',
	        success: function (result) {
	        	table_bill(result.data)
	        },
	        error: function (error) {
	            sweetalert(error.status)
	        }
	    });
})()


let table_bill = (data) => {
    // < ----------------------- load data to table  ------------------------------->
    $('#my-table').DataTable({
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
            {"mData": "idApartment"},
            {"mData": "date"},
            {"mData": "totalPrice"},
            {"mData": "username"},
            {"mData": function(data){
            	return data.paid == true ? 'Đã thanh toán' : 'Chưa thanh toán' ;
            }
            },
          //  {"mData": "paid"},
            {
                "mRender": function (data, type, full) {
                    return `<button onclick='showFormUpdate(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-primary btn-lg" data-original-title="Remove">
                          <i class="fa fa-edit"></i> </button>`
                }
            },
            {
                "mRender": function (data, type, full) {
                    return `<button onclick='deleteBill(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-danger" data-original-title="Remove">
                        <i class="fa fa-times"></i> </button>`
                }
            }
        ]
    });
}

//< ----------------------------- Delete ---------------------------->
let deleteBill = (id, e) => {
    Swal.fire({
        title: 'Warning',
        text: "Bạn có chắc chắn muốn xóa!",
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
                url: URL + `api/apartment-index/${id}`,
                contentType: "application/json",
                cache: false,
                success: function (result) {
                    $('#my-table').DataTable().row($(e).parents('tr')).remove().draw();
                    sweetalertSuccess(result.message);
                },
                error: function (error) {
                    sweetalertError(error);
                }
            });
        }
    })
}
let showFormUpdate = (id) => {
	 location.href= URL + `quan-ly/hoa-don/${id}`;
}


//< ------------------------- insert  -------------------->
document.querySelector('#insert').addEventListener('click', () => {
	let newBill = getValueFormInsert();
	if(validateFormInsert(newBill)){
		$.ajax({
            type: 'POST',
            url: URL + `api/apartment-index`,
            contentType: 'application/json',
            dataType: 'json',
            cache: false,
            data: JSON.stringify(newBill),
            success: function (result) {
                result.data.date = formatDate(result.data.date)
                $('#my-table').DataTable().row.add(result.data).draw().node();
                sweetalertSuccess(result.message)
                cleanFormInsert();
            },
            error: function (error) {
            	sweetalertError(error)
            }
        })
		
		
	}

});


document.querySelector('#clean-form-insert').addEventListener('click', () => {
	cleanFormInsert();
});

//<------------- When modal close -> clean form modal  ----------->
$("#form-building").on("hidden.bs.modal", function () {
	cleanFormInsert();
});



let getValueFormInsert = () => {
	return {
		"apartment" : {
			"id": document.querySelector('#apartment_id').value.trim()
		},
		"electricityNumber": document.querySelector('#electricity-insert').value.trim(),
		"warterNumber": document.querySelector('#water-insert').value.trim(),
		"date": document.querySelector('#date-insert').value.trim(),
		"employee": {
			"id": ID_NV
		}
	}
}

let cleanFormInsert = () => {
    // document.getElementById("apartment_id").selectedIndex = 0;
    $('#apartment_id').val(null).trigger('change');
	 document.querySelector('#electricity-insert').value = '';
	 document.querySelector('#water-insert').value = '';
	document.querySelector('#date-insert').value = '';
}


let validateFormInsert = (data) => {

    if (data.apartment.id === '') {
        toastrError("Căn hộ không được để trống!");
        document.querySelector('#apartment_id').focus();
        return false
    }
    if( data.apartment.id.length > 8 ){
        toastrError("Id căn hộ không quá 8 ký tự!");
        document.querySelector('#id-apartment').focus();
        return false;
    }
    if (data.date === '') {
        toastrError("Ngày tạo không được để trống");
        document.querySelector('#date-insert').focus();
        return false
    }
    if (data.electricityNumber === '') {
        toastrError("Số điện không được để trống");
        document.querySelector('#electricity-insert').focus();
        return false
    }
    if (data.electricityNumber < 0) {
        toastrError("Số điện không được âm");
        document.querySelector('#electricity-insert').focus();
        return false
    }
    if( isNaN(data.electricityNumber) ){
		toastrError("Số điện phải là số!");
		document.querySelector('#electricity-insert').focus();
		return false;
	}
    if (data.warterNumber === '') {
        toastrError("Số nước không được để trống");
        document.querySelector('#water-insert').focus();
        return false
    }
    if (data.warterNumber < 0) {
        toastrError("Số nước không âm");
        document.querySelector('#water-insert').focus();
        return false
    }
    if( isNaN(data.warterNumber) ){
		toastrError("Số nước phải là số!");
		document.querySelector('#water-insert').focus();
		return false;
	}
 
    return true;
}


