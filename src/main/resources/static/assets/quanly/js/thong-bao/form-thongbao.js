let fillToForm = (data) => {
	document.querySelector('#id').value = data.id;
	document.querySelector('#title').value = data.title;
	document.querySelector('#description').value = data.description;
	document.querySelector('#date').value = data.date;
	document.querySelector('#image').value =  data.image;
	CKEDITOR.instances['note'].setData(data.content)
}
// add ckeditor
CKEDITOR.replace('note');
const ID = document.querySelector('#id').value;
(function(){
	if(ID === '0'){
		document.querySelector('#level3-menu').innerText = 'Thêm thông báo';
		document.querySelector('.card-title').innerText =  'Thêm thông báo';
	}else{
		$.ajax({
		     url: URL + `api/notification/${ID}`,
		     type: 'GET',
		     dataType: 'json',
		     success: function (result) {
		    	 fillToForm(result.data);
		    	 
		     },
		     error: function (error) {
		    	 sweetalertError(error);	
		     }
		 });
	}
})()


//< ------------------------ insert or update  ---------------------->
document.querySelector('#save').addEventListener('click', () => {	
    let notification = getValueForm();
   
    if(validate(notification)){
	    if (notification.id > 0) {
	        $.ajax({
	            type: 'PUT',
	            url: URL + `api/notification/${notification.id}`,
	            contentType: "application/json",
	            dataType: 'json',
	            cache: false,
	            data: JSON.stringify(notification),
	            success: function (result) {     		
	            	location.href =URL + `quan-ly/thong-bao`; 		
	            },
	            error: function (error) {
	            	sweetalert(error.status)
	            }
	        });
	
	    } else {
	        $.ajax({
	            type: 'POST',
	            url: URL + `api/notification`,
	            contentType: "application/json",
	            dataType: 'json',
	            cache: false,
	            data: JSON.stringify(notification),
	            success: function (result) {
	            	cleanForm();
	                sweetalertSuccess(result.message);
	            },
	            error: function (error) {
	            	sweetalert(error.status);
	            }
	        });
	    }
    }   
});

// < ---------------------- Clean form ---------------------------->
let cleanForm = () => {
    fillToForm({
        "id":"",
        "title": "",
        "description": "",
        "image": "",
        "date": "",
        "content": ""
    });
}


// < ------------------- get value form --------------------------->
let getValueForm = () => {
    return {
        "id": document.querySelector('#id').value,
        "title": document.querySelector('#title').value,
        "description": document.querySelector('#description').value,
        "date": document.querySelector('#date').value,
        "image": document.querySelector('#image').value,
        "content":CKEDITOR.instances['note'].getData(),
        "employee": {
            "id": ID_NV   
        },
        
    }
}



let validate = (data) =>  {
	if(data.title === ''){
		toastrError("Tiêu đề không được để trống!");
		document.querySelector('#title').focus();
		return false;
	}
	if(data.date === ''){
		toastrError("Ngày không được để trống!");
		document.querySelector('#date').focus();
		return false;
	}
	
	if(data.date === ''){
		toastrError("Thông báo không được để trống!");
		document.querySelector('#note').focus();
		return false;
	}
	if(data.description === ''){
		toastrError("Mô tả không được để trống!");
		document.querySelector('#description').focus();
		return false;
	}
	if(data.content === ''){
		toastrError("Nội dung không được để trống!");
		document.querySelector('#note').focus();
		return false;
	}
	
	
	
return true;
}

