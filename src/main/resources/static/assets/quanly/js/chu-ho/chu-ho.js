(function(){
	 $.ajax({
	        url: URL + "api/own-apartment",
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
    // <- ------------------------- load data to table ---------------------------->
     $('#table-chucanho').DataTable({
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
					return `<button onclick='showFormUpdate(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-primary btn-lg" data-original-title="Edit Task">
                        <i class="fa fa-edit"></i> </button>`
				}
			},
			{
				"mRender": function (data, type, full) {
					return `<button onclick='deleteOwn(${full.id},this)' type="button" data-toggle="tooltip" title="" class="btn btn-link btn-danger" data-original-title="Remove">
                        <i class="fa fa-times"></i> </button>`
				}
			}
        ]
    });
}

let changetitle = () => {
    document.querySelector('#form-label').innerHTML = "<i class='fas fa-address-card mr-3 '></i>" +'Thêm chủ căn hộ';
}

var index = -1;
//< -------------------------- show form update --------------------->
let showFormUpdate = (id, e) => {
 index = $('#table-chucanho').DataTable().row($(e).parents('tr')).index();
 $('#form-resident').modal('show')
document.querySelector('#form-label').innerHTML = "<i class='fas fa-address-card mr-3 '></i>" +'Cập nhật thông tin chủ căn hộ';
 $.ajax({
     url: URL + `api/own-apartment/${id}`,
     type: 'GET',
     dataType: 'json',
     success: function (result) {
         fillToForm(result.data);
         document.querySelector('#id').value = result.data.id;
     },
     error: function (error) {
     	sweetalertError(error);	
     }
 });
}



//< ----------------------------- Delete ---------------------------->
let deleteOwn = (id, e) => {
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


document.querySelector('#save').addEventListener('click', () => {
	 var dto  = getValueForm();
	 if(validate(dto)){
		 if(dto.id){	 
			 $.ajax({
		            type: 'PUT',
		            url: URL + `api/own-apartment/${dto.id}`,
		            contentType: "application/json",
		            dataType: 'json',
		            cache: false,
		            data: JSON.stringify(dto),
		            success: function (result) {  
		            	 //update the row in dataTable
	                    $('#table-chucanho').DataTable().row(index).data(result.data).draw();
	                    // close modal
	                    $('#form-resident').modal('hide');
	                    // annount
		            	sweetalertSuccess(result.message);         	              
		            },
		            error: function ( error) {	            	
		            	sweetalertError(error);		            	 
		            }
		        });
		 }else{		 
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
	            	
	            	 $('#table-chucanho').DataTable()
                     .row.add(result.data).draw().node();
	                // Clean form
	                cleanForm();	            	              
	            },
	            error: function ( error) {	            
	            	sweetalertError(error);	 
	            }
	        });
	        
		 }
	        
	        
	 }
});

let getValueForm = () => {
    return { 
    	"id": document.querySelector('#id').value.trim(),
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

let fillToForm = (data) => {
	document.querySelector('#fullname').value = data.fullname,
    document.querySelector('#birthday').value = data.birthday,
    document.querySelector('#homeTown').value = data.homeTown,
	$(data.gender ? "#male" : "#female").prop('checked', true),
    document.querySelector('#identityCard').value = data.identitycard,
    document.querySelector('#phone').value = data.phone,
    document.querySelector('#job').value = data.job,
    document.querySelector('#email').value = data.email,
    document.querySelector('#id_apartment').value = data.apartments;
}

//< ---------------- clean form when modal close ---------->
$("#form-resident").on("hidden.bs.modal", function () {
	cleanForm();
	document.querySelector('#id').value ='';
	
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
	var vnf_regex = /((09|03|07|08|05)+([0-9]{7,8})\b)/g;		
	if(!vnf_regex.test(data.phone)){
		toastrError("Số điện thoại sai định dạng!");
		document.querySelector('#phone').focus();
		return false;
	}
	if(data.job === ''){
		toastrError("Nghề nghiệp không được để trống!");
		document.querySelector('#job').focus();
		return false;
	}
	if(data.homeTown === ''){
		toastrError("Quê quán không được để trống!");
		document.querySelector('#homeTown').focus();
		return false;
	}
	if(data.email === ''){
		toastrError("Email không được bỏ trống!");
		document.querySelector('#email').focus();
		return false;
	}
    var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/; 
	if(!filter.test(data.email))
		{
		toastrError("Email sai định dạng!");
		document.querySelector('#email').focus();
		return false;
		}
  
	if(data.apartments[0] === ''){  
		toastrError("Mã căn hộ không được để trống!");
		document.querySelector('#id_apartment').focus();
		return false;
	}
	 
	for(let i = 0; i< data.apartments.length ; i++){
		 let special = data.apartments[i].match((/[!@#$%^&*_]+/g));
		 if (special != null) {
	    	 toastrError("Mã căn hộ không được chứa các ký tự đặc biệt hoặc phải ngăn cách nhau bởi dấu phẩy!");
	         document.querySelector('#id_apartment').focus();
	         return false;
	      }
	}
	
	return true;
	
}


