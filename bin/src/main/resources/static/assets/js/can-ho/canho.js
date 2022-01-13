
(function(){
	 $.ajax({
	        url: URL + `api/apartment`,
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
        "scrollCollapse": true,
        "paging": true,
        "serverSize": true,
        "lengthMenu": [[5, 25, 50, -1], [5, 25, 50, "All"]],
        "responsive": true,
        "scroller": true,
        "autoWidth": true,
        "processing": true,
        "scrollY": "250px",
       // "sAjaxSource": URL + 'api/apartment', 
        "sAjaxDataProp": "",          
        "aaData": data,
        "order": [[0, "asc"]],
        "aoColumns": [
            {"mData": "id"},
            {"mData": function(data){
                    return data.ownApartment ? data.ownApartment.id : 'Chưa có';
                }
            },
            {"mData": "location"},
            {"mData": "acreage"},
            {"mData": "password"},
            {
                "mRender": function (aaData, type, full) {           	
                    return `<i  class="material-icons icon-table icon-update" onclick='showFormUpdate("${full.id}",this)'  type="button">edit</i>`
                },"mWidth": "5%"
            },
            {
                "mRender": function (data, type, full) {
                    return `<i  class="material-icons icon-table icon-delete " onclick='deleteApartment("${full.id}",this)'  type="button">delete</i>`
                }
            }
        ]
    });
}


// < ----------------------------- Delete ---------------------------->
let deleteApartment = (id,e) => {
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
                url: URL + `api/apartment/${id}`,
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



//< -------------------------------------------- INSERT  ------------------------------------->

document.querySelector('#insert').addEventListener('click', () => {
    let data = getValueForm();
    if(validateInsert(data)){
        $.ajax({
            type: 'POST',
            url: URL + `api/apartment`,
            contentType: "application/json",
            dataType: 'json',
            cache: false,
            data: JSON.stringify(data),
            success: function (result) {
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
});

$("#form-building").on("hidden.bs.modal", function () {
    cleanForm();
});


// < ---------------------------------------- Clean form ---------------------------->
let cleanForm = () => {
    document.querySelector('#id').value = '';
    document.querySelector('#password').value = '';
    document.querySelector('#id_own').value = '';
    document.querySelector('#acreage').value = '';
    document.querySelector('#location').value = '';
    document.querySelector('#note').value = '';
}


// < -------------- clean form when click button clean ------------>
document.querySelector('#clean-form').addEventListener('click', cleanForm);


// < ------------------- get value form --------------------------->
let getValueForm = () => {
    let id_own = document.querySelector('#id_own').value.trim();
    let ownapartment = null;
    if(id_own) {
        ownapartment = {
            "id": id_own,
        }
    }
    return {
        "id": document.querySelector('#id').value.trim(),
        "password": document.querySelector('#password').value.trim(),
        "ownApartment": ownapartment ,
        "acreage": document.querySelector('#acreage').value.trim(),
        "location": document.querySelector('#location').value.trim(),
        "note": document.querySelector('#note').value.trim()
    }
}

let validateInsert = (data) => {
    if (data.id === '') {
        toastrError("Id căn hộ không được để trống!");
        document.querySelector('#id').focus();
        return false;
    }
    if( data.id.length > 8 ){
        toastrError("Id căn hộ không quá 8 ký tự!");
        document.querySelector('#id').focus();
        return false;
    }
    if( data.password === '' ){
        toastrError("Mật khẩu không được để trống!");
        document.querySelector('#password').focus();
        return false;
    }
    if( data.password.length <3  ||  data.password.length > 8 ){
        toastrError("Mật khẩu phải từ 3 đên 8 ký tự!");
        document.querySelector('#password').focus();
        return false;
    }
    if( data.ownApartment != null && isNaN(data.ownApartment.id) ){
        toastrError("Id chủ căn hộ phải là số!");
        document.querySelector('#id_own').focus();
        return false;
    }
    if (data.acreage === '') {
        toastrError("Diện tích không được để trống!");
        document.querySelector('#acreage').focus();
        return false;
    }
    if (data.location === '') {
        toastrError("Vị trí không được để trống!");
        document.querySelector('#location').focus();
        return false;
    }
    return true;
}





//< --------------------------------------- UPDATE  ------------------------------------->

var index = -1;
//< -------------------------- show form update --------------------->
let showFormUpdate = (id, e) => {
    index = $('#my-table').DataTable().row($(e).parents('tr')).index();
    $('#form-update').modal('show')
    document.querySelector('#title-update').innerHTML =  "<i class='fas fa-building mr-3'></i>" +`Cập nhật căn hộ ${id}`;
    $.ajax({
        url: URL + `api/apartment/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            fillToFormUpdate(result.data)
        },
        error: function (error) {
        	sweetalertError(error);	
        }
    });

}


document.querySelector('#update').addEventListener('click', () => {
    let data = getValueFormUpdate();
    if(validateUpdate(data)){
        $.ajax({
            type: 'PUT',
            url: URL + `api/apartment/${data.id}`,
            contentType: "application/json",
            dataType: 'json',
            cache: false,
            data: JSON.stringify(data),
            success: function (result) {     
                //update the row in dataTable
                $('#my-table').DataTable().row(index).data(result.data).draw();
                // close modal
                $('#form-update').modal('hide');
                // annount
                sweetalertSuccess(result.message);
            },
            error: function (error) {
            	sweetalertError(error);	
            }
        });
    }
});


let fillToFormUpdate = (data) => {
    document.querySelector('#id-update').value = data.id;
    document.querySelector('#password-update').value = data.password;
    document.querySelector('#id-own-update').value = data.ownApartment ? data.ownApartment.id : '';
    document.querySelector('#acreage-update').value = data.acreage;
    document.querySelector('#location-update').value = data.location;
    document.querySelector('#note-update').value = data.note;

}


//<------------- When modal close -> clean form modal  ----------->
$("#form-update").on("hidden.bs.modal", function () {
    cleanFormUpdate();
});


// < ---------------------------------------- Clean form ---------------------------->
let cleanFormUpdate = () => {
    fillToFormUpdate({
        "id": "",
        "password": "",
        "ownApartment": null,
        "acreage": "",
        "location": "",
        "note": ""
    });
}


// < -------------- clean form when click button clean ------------>
document.querySelector('#clean-form-update').addEventListener('click', cleanFormUpdate);

//< ------------------- get value form --------------------------->
let getValueFormUpdate = () => {
    let id_own = document.querySelector('#id-own-update').value.trim();
    let ownapartment = null;
    if(id_own) {
        ownapartment = {
            "id": id_own,
        }
    }
    return {
        "id": document.querySelector('#id-update').value.trim(),
        "password": document.querySelector('#password-update').value.trim(),
        "ownApartment": ownapartment ,
        "acreage": document.querySelector('#acreage-update').value.trim(),
        "location": document.querySelector('#location-update').value.trim(),
        "note": document.querySelector('#note-update').value.trim()
    }
}



let validateUpdate = (data) => {

    if( data.password === '' ){
        toastrError("Mật khẩu không được để trống!");
        document.querySelector('#password-update').focus();
        return false;
    }
    if( data.password.length < 3  ||  data.password.length > 8 ){
        toastrError("Mật khẩu phải từ 3 đên 8 ký tự!");
        document.querySelector('#password-update').focus();
        return false;
    }
    if( data.ownApartment != null && isNaN(data.ownApartment.id) ){
        toastrError("Id chủ căn hộ phải là sô!");
        document.querySelector('#id-own-update').focus();
        return false;
    }
    if (data.acreage === '') {
        toastrError("Diện tích không được để trống!");
        document.querySelector('#acreage-update').focus();
        return false;
    }
    if (data.location === '') {
        toastrError("Vị trí không được để trống!");
        document.querySelector('#location-update').focus();
        return false;
    }
    return true;
}






