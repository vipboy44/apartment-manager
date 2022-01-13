const ID = document.querySelector('#id_own').value;
let OB = null;
(function(){
	 $.ajax({
	     url: URL + `api/own-apartment/${ID}`,
	     type: 'GET',
	     dataType: 'json',
	     success: function (result) {
	    	 OB = result.data;
	         fillToForm(result.data);
	         fillToFormImage(result.data);
	     },
	     error: function (error) {
	    	 sweetalertError(error);	
	     }
	 });
	
})()

/* ------------------------        Update OwnApartment     --------------------------*/


document.querySelector('#save').addEventListener('click', () => {
	 var dto  = getValueForm();
	 console.log(dto);
	 if(validate(dto)){
	        $.ajax({
	            type: 'PUT',
	            url: URL + `api/own-apartment/${ID}`,
	            contentType: "application/json",
	            dataType: 'json',
	            cache: false,
	            data: JSON.stringify(dto),
	            success: function (result) {  
	            	fillToFormImage(result.data);
	            	sweetalertSuccess(result.message);         	              
	            },
	            error: function ( error) {	            	
	            	sweetalertError(error);		            	 
	            }
	        });
	        
	 }
});



/* ------------------------         Upload  File      --------------------------*/
document.querySelector('.img').addEventListener('click', () => {
	document.querySelector('#file').click();
});


function readURL(input) {
	  if (input.files && input.files[0]) {    
	    var reader = new FileReader();
	    reader.onload = function (e) {
	      $('#imgs')
	        .attr('src', e.target.result)
	         .width(130)
	        .height(130); 
	    };
	    reader.readAsDataURL(input.files[0]);
	      
	    document.querySelector('#upload-now').click();    
	 }
}

$("#file-upload-form").on("submit", function (e) {
    // cancel the default behavior
    e.preventDefault();
    $.ajax({
    	 url: URL + `api/own-apartment/upload-file/${ID}`,
        type: "POST",
        data: new FormData(this),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (result) {
        	sweetalertSuccess(result.message); 
        },
        error: function (error) {
        	fillToFormImage(OB);
        	sweetalertError(error);	
        }
    });
});

/* ------------------------       End  Upload  File      --------------------------*/


let fillToForm = (data) => {
	console.log(data);
	
	document.querySelector('#id_own').value = data.id;
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


let fillToFormImage = (data) => {
	if(data.image){	
		document.querySelector('#imgs').src = URL + `assets/photo/${data.image}`;  
	}else{
		document.querySelector('#imgs').src = URL + `assets/photo/someone.png`;  
	}
	document.querySelector('#name-formImg').innerHTML = data.fullname;
	document.querySelector('#email-formImg').innerHTML = data.email;
}

/* ------------------------       Get value form      --------------------------*/
let getValueForm = () => {
    return {
        "id": document.querySelector('#id_own').value.trim(),
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

/* ------------------------      Validate form      --------------------------*/
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


