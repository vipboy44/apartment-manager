(function(){
	 $.ajax({
         type: 'GET',
         url: URL + `api/contact`,    
         dataType: 'json',
         cache: false,   
         success: function (result) {
        	 fillToForm(result.data);         
         },
         error: function (error) {
        	 sweetalertError(error);	
         }
     });
	
	
})()


//< ------------------------ insert or update  ---------------------->
document.querySelector('#save').addEventListener('click', () => {	
    let contact = getValueForm();
    
    if(validate(contact)){
	        $.ajax({
	            type: 'PUT',
	            url: URL + `api/contact`,
	            contentType: "application/json",
	            dataType: 'json',
	            cache: false,
	            data: JSON.stringify(contact),
	            success: function (result) {
	            	fillToForm(result.data);      
	            	sweetalertSuccess(result.message);
	            },
	            error: function (error) {
	            	 sweetalertError(error);
	            }
	        });
    }
       
});

//< ------------------- get value form --------------------------->
let getValueForm = () => {
    return {
        "name": document.querySelector('#name').value,
        "phone": document.querySelector('#phone').value,
        "email": document.querySelector('#email').value,
        "address": document.querySelector('#address').value,
        "bank": document.querySelector('#bank-name').value,
        "accountNumber": document.querySelector('#account-number').value
    }
}

//< ------------------- fill to form------------------------------>
let fillToForm = (contact) => {
    document.querySelector('#name').value = contact.name;
    document.querySelector('#phone').value = contact.phone;
    document.querySelector('#email').value = contact.email;
    document.querySelector('#address').value = contact.address;
    document.querySelector('#bank-name').value = contact.bank;
    document.querySelector('#account-number').value = contact.accountNumber;
}


let validate = (data) =>  {
	if(data.name === ''){
		toastrError("Tên chung cư không được để trống!");
		document.querySelector('#name').focus();
		return false;
	}
	if(data.phone === ''){
		toastrError("Số điện thoại không được để trống!");
		document.querySelector('#phone').focus();
		return false;
	}
	var vnf_regex = /((09|03|07|08|05)+([0-9]{7,8})\b)/g;	
	
	if(!vnf_regex.test(data.phone)){
		toastrError("Số điện thoại sai định dạng!");
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
	if(data.address === ''){
		toastrError("Địa chỉ không được để trống!");
		document.querySelector('#address').focus();
		return false;
	}
	if(data.bank === ''){
		toastrError("Tên ngân hàng không được để trống!");
		document.querySelector('#bank-name').focus();
		return false;
	}
	if(data.accountNumber === ''){
		toastrError("Số tài khoản không được để trống");
		document.querySelector('#account-number').focus();
		return false;
	}
	if(isNaN(data.accountNumber)){
		toastrError("Số tài khoản phải là số!");
		document.querySelector('#account-number').focus();
		return false;
	}
	if(data.accountNumber.length <8 || data.accountNumber.length > 13 ){
		toastrError("Số tài khoản phải từ 8 đến 13 chữ số!");
		document.querySelector('#account-number').focus();
		return false;
	}
	
return true;
}







