(function(){
	 $.ajax({
	        url: URL + "api/own-apartment",
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
    // <- ------------------------- load data to table ---------------------------->
     $('#table-chucanho').DataTable({
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
        //"sAjaxSource": URL + "api/own-apartment",
        "sAjaxDataProp": "",  
        "aaData": data,
        "order": [[0, "asc"]],
        "aoColumns": [
        	 {"mData": "fullname"},
			{"mRender": function (data, type, full) {
					return full.gender ? "Nam" : "Nữ"}
			},
             {"mData": "homeTown"}, 
             {"mData": "phone"},
             {"mData": "email"},   
             {"mData": "apartments"}, 
            {
                "mRender": function (data, type, full) {
                    return `<i  class="material-icons icon-table icon-update" onclick='showFormUpdate(${full.id})' type="button">edit</i>`
                }
            },
            {
                "mRender": function (data, type, full) {
                    return `<i  class="material-icons icon-table icon-delete " onclick='deleteOwn(${full.id},this)' type="button">delete</i>`
                }
            }
        ]
    });
}


//< ----------------------------- Delete ---------------------------->
let deleteOwn = (id, e) => {
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
                url: URL + `api/own-apartment/${id}`,
                contentType: "application/json",
                cache: false,
                success: function (result) {
                    $('#table-chucanho').DataTable().row($(e).parents('tr')) // format date
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


let showFormUpdate = (id) => {
	 location.href= URL + `ui/own-apartment/form/${id}`;  
}


document.querySelector('#save').addEventListener('click', () => {
	 var dto  = getValueForm();
	 if(validate(dto)){
	        $.ajax({
	            type: 'POST',
	            url: URL + `api/own-apartment`,
	            contentType: "application/json",
	            dataType: 'json',
	            cache: false,
	            data: JSON.stringify(dto),
	            success: function (result) {
	                // annount         
	            	sweetalertSuccess(result.message);
	            	
	            	location.href = URL + 'ui/own-apartment' ; 
	                // Clean form
	                cleanForm();	            	              
	            },
	            error: function ( error) {	            
	            	sweetalertError(error);	 
	            }
	        });
	        
	 }
});



let getValueForm = () => {
    return { 
        "fullname": document.querySelector('#fullname').value.trim(),
        "birthday": document.querySelector('#birthday').value.trim(),
        "homeTown": document.querySelector('#homeTown').value.trim(),
        "gender": document.querySelector('#male').checked === true ? true : false,
        "identitycard": document.querySelector('#identityCard').value.trim(),
        "phone": document.querySelector('#phone').value.trim(),
        "job": document.querySelector('#job').value.trim(),
        "email": document.querySelector('#email').value.trim(),
        "apartments": document.querySelector('#id_apartment').value.split(/,/).map( n => n.trim())
    }
}
//< ---------------- clean form when modal close ---------->
$("#form-resident").on("hidden.bs.modal", function () {
	cleanForm();
});

let cleanForm = () =>{  
     document.querySelector('#fullname').value = '',
     document.querySelector('#birthday').value = '',
     document.querySelector('#homeTown').value = '',
	 $('input[name="gender"]').prop('checked', false),
     document.querySelector('#identityCard').value = '',
     document.querySelector('#phone').value = '',
     document.querySelector('#job').value = '',
     document.querySelector('#email').value = '',
     document.querySelector('#id_apartment').value = ''
}

document.querySelector('#clean-form').addEventListener('click', cleanForm);

let validate = (data) =>{
	
	if(data.fullname === ''){
		toastrError("Họ tên không được để trống!");
		document.querySelector('#fullname').focus();
		return false;
	}
	if(data.birthday === ''){
		toastrError("Ngày sinh không được để trống!");
		document.querySelector('#birthday').focus();
		return false;
	}
	if (!$('input[name=gender]:checked').val()) {
		toastrError("Chưa chọn giới tính");
		return false
	}
	if(data.homeTown === ''){
		toastrError("Quê quán không được để trống!");
		document.querySelector('#birthday').focus();
		return false;
	}
	if(data.identitycard === ''){
		toastrError("Số chứng minh - căn cước công dân không được để trống!");
		document.querySelector('#identityCard').focus();
		return false;
	}
	if(isNaN(data.identitycard)){
		toastrError("Số chứng minh - căn cước công dân phải là số!");
		document.querySelector('#identityCard').focus();
		return false;
	}
	if(data.identitycard.length <9 || data.identitycard.length > 12 ){
		toastrError("Số chứng minh - căn cước công dân phải từ 9 đến 12 chữ số!");
		document.querySelector('#identityCard').focus();
		return false;
	}
	if(data.phone === ''){
		toastrError("Số điện thoại không được để trống!");
		document.querySelector('#phone').focus();
		return false;
	}
	if(isNaN(data.phone)){
		toastrError("Số điện thoại phải là số!");
		document.querySelector('#phone').focus();
		return false;
	}
	if(data.phone.length <9 || data.phone.length > 11 ){
		toastrError("Số điện thoại phải từ 9 đến 11 chữ số!");
		document.querySelector('#phone').focus();
		return false;
	}
	if(data.job === ''){
		toastrError("Nghề nghiệp không được để trống!");
		document.querySelector('#job').focus();
		return false;
	}
	if(data.apartments[0] === ''){  
		toastrError("Mã căn hộ không được để trống!");
		document.querySelector('#id_apartment').focus();
		return false;
	}
	return true;
	
}


