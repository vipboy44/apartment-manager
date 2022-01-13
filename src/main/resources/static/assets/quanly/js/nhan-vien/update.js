/* ---------------------------------- get role form data -------------------------------------  */
let checked = (roles) => {
    let admin = false;
    let user = false;
    for (i = 0; i < roles.length; i++) {
        if (roles[i].name === "ROLE_ADMIN") {
            admin = true;
        }
        if (roles[i].name === "ROLE_USER") {
            user = true;
        }
    }
    if (admin) {
        document.querySelector('#role-admin').checked = true;
    } else {
        document.querySelector('#role-admin').checked = false
    }
    if (user) {
        document.querySelector('#role-user').checked = true;
    } else {
        document.querySelector('#role-user').checked = false;
    }
}
/* ------------------------------- fill  data to form employee ------------------------------ */
let fillToForm = (data) => {
    document.querySelector('#id_employee').value = data.id,
        document.querySelector('#fullName').value = data.fullName,
        document.querySelector('#birthday').value = data.birthday,
        $(data.gender ? "#female" : "#male").prop('checked', true),
        document.querySelector('#address').value = data.address,
        document.querySelector('#identitycard').value = data.identitycard,
        document.querySelector('#phone').value = data.phone,
        document.querySelector('#username').value = data.username,
        document.querySelector('#email').value = data.email,
        checked(data.roles)

}

/* -------------------------------------  fill image ----------------------------------- */
let fillToFormImage = (data) => {
    if(data.image){
   //     document.querySelector('#imgs').src = URL + `assets/quanly/image/${data.image}`;
        document.querySelector('#imgs').src = `https://storage.googleapis.com/apartment-management-15f74.appspot.com/photo/user/${data.image}`;
    }
    else{
        document.querySelector('#imgs').src = URL + `assets/quanly/image/someone.png`;
    }
}
const ID = document.querySelector('#id_employee').value;
let OB = null;
(function () {
    $.ajax({
        url: URL + `api/employee/${ID}`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            OB = result.data;
            fillToForm(result.data);
            fillToFormImage(result.data)
        },
        error: function (error) {
            sweetalertError(error)
        }

    });
})()


/*  ------------------------------------- get value form ------------------------------------ */
let getValueForm = () => {
    return {
        'id': document.querySelector('#id_employee').value.trim(),
        'fullName': document.querySelector('#fullName').value.trim(),
        'birthday': document.querySelector('#birthday').value.trim(),
        'gender': $("input[name='gender']:checked").val() === 'female',
        'address': document.querySelector('#address').value.trim(),
        'identitycard': document.querySelector('#identitycard').value.trim(),
        'phone': document.querySelector('#phone').value.trim(),
        'username': document.querySelector('#username').value.trim(),
        'email': document.querySelector('#email').value.trim(),
        'password': "123@123a", /// cái chuỗi này xuống data sẽ ko lấy
        'roles': $('input[type=checkbox]:checked').map(function (_, role) {
            return $(role).val();
        }).get()
    }

}
/* --------------------- Update Employee --------------- */
document.querySelector('#save').addEventListener('click', () => {
    var employee = getValueForm();
    if (validate(employee)) {
    	  if ( employee.password === 'tgFxcP4b') 
    		  employee.password = null ;
        $.ajax({
            type: 'PUT',
            url: URL + `api/employee/${ID}`,
            contentType: 'application/json',
            dataType: 'json',
            cache: false,
            data: JSON.stringify(employee),
            success: function (result) {
                sweetalertSuccess(result.message)
                fillToFormImage(result.data);
            },
            error: function (error) {
                sweetalertError(error)
            }
        });
        
        
        
    }
});

/* ------------------------------- Reset password -----------------------------*/
document.querySelector('#reset-password').addEventListener('click', () => {
	$.ajax({
        type: 'PUT',
        url: URL + `api/employee/reset-password/${ID}`,
        contentType: 'application/json',
        dataType: 'json',
        cache: false,   
        success: function (result) {
            sweetalertSuccess(result.message)   
        },
        error: function (error) {
            sweetalertError(error)
        }
    });
	
});

document.querySelector('#clean-form').addEventListener('click', () => {
     document.querySelector('#fullName').value = '';
     document.querySelector('#birthday').value = '';
     document.querySelector('#address').value  = '';
     $('input[name="gender"]').prop('checked', false),
     document.querySelector('#identitycard').value = '';
     document.querySelector('#phone').value = '';
     document.querySelector('#username').value = '';
     document.querySelector('#email').value = ''	 
     document.querySelector('#role-admin').checked = false;
     document.querySelector('#role-user').checked = false;
     

});

/* ------------------------------- Upload  File -----------------------------*/
document.querySelector('#imgs').addEventListener('click', () => {
    document.querySelector('#file').click();
});

/* -----------------------------  read url for image -------------------*/
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#imgs')
                .attr('src', e.target.result)
                .width(300).height(350)

        };
        reader.readAsDataURL(input.files[0]);
        document.querySelector('#upload-now').click();
    }
}

/*  --------------------------------- upload image ---------------------------*/
$("#file-upload-form").on("submit", function (e) {
    e.preventDefault();
    $.ajax({
        url: URL + `api/employee/upload-file/${ID}`,
        method: 'POST',
        data: new FormData(this),
        enctype: 'multipart/form-data',
        processData: false, 
        contentType: false,
        //contentType: "image/jpeg",
        cache: false,
        success: function (result) {
            sweetalertSuccess(result.message);
        },
        error: function (error) {
            fillToFormImage(OB);
            sweetalertError(error);
        }
    });
})



/* ------------------------- ---------------------------------------------*/
let validate = (data) => {

    if (data.fullName === '') {
        toastrError("Họ tên không được để trống!");
        document.querySelector('#fullName').focus();
        return false;
    }
    if (data.birthday === '') {
        toastrError("Ngày sinh không được để trống!");
        document.querySelector('#birthday').focus();
        return false;
    }
    if (data.identitycard === '') {
        toastrError("Số chứng minh - căn cước công dân không được để trống!");
        document.querySelector('#identitycard').focus();
        return false;
    }
    if (isNaN(data.identitycard)) {
        toastrError("Số chứng minh - căn cước công dân phải là số!");
        document.querySelector('#identitycard').focus();
        return false;
    }
    if (data.identitycard.length < 9 || data.identitycard.length > 12) {
        toastrError("Số chứng minh - căn cước công dân phải từ 9 đến 12 chữ số!");
        document.querySelector('#identitycard').focus();
        return false;
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
	if(!vnf_regex.test(data.phone)){
		toastrError("Số điện thoại sai định dạng!");
		document.querySelector('#phone').focus();
		return false;
	}
    if (data.phone.length < 9 || data.phone.length > 11) {
        toastrError("Số điện thoại phải từ 9 đến 11 chữ số!");
        document.querySelector('#phone').focus();
        return false;
    }
    if (!$('input[name=gender]:checked').val()) {
        toastrError("Chưa chọn giới tính");
        return false
    }

    if (data.address === '') {
        toastrError("Địa chỉ không được để trống!");
        document.querySelector('#address').focus();
        return false;
    }
    if(data.email === ''){
		toastrError("Email không được để trống!");
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
	   if (data.username === '') {
	        toastrError("Tên đăng nhập không được để trống!");
	        document.querySelector('#username').focus();
	        return false;
	    }
	    if(data.username.length < 5 || data.username.length > 20){
	        toastrError("Tên đăng nhập từ 5 đến 20 ký tự!");
	        document.querySelector('#username').focus();
	        return false;
	    }
	    
    return true;

}
