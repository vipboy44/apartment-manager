// add ckeditor
CKEDITOR.replace('note');
//< ------------------------ insert or update  ---------------------->
document.querySelector('#save').addEventListener('click', () => {	
    let notification = getValueForm();
   
    if(validate(notification)){
	    if (notification.id) {
	        $.ajax({
	            type: 'PUT',
	            url: URL + `api/notification/${notification.id}`,
	            contentType: "application/json",
	            dataType: 'json',
	            cache: false,
	            data: JSON.stringify(notification),
	            success: function (result) {
	            	// Convert date to yy-MM-dd
	                result.date = formatDate(result.date);
	               //update the row in dataTable
	                $('#table-notification').DataTable().row(index).data(result).draw();
	               // close modal
	                $('#form-building').modal('hide');   
	               // annount
	                sweetalert(200,'Success!' , ' Đã cập nhật thông báo ')
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
	            	// Convert date to yy-MM-dd
	                result.date = formatDate(result.date);
	                // Add new data to DataTable
	                $('#table-notification').DataTable()  
	                    .row.add(result).draw().node();
	                // Clean form
	                cleanForm(); 
	                // annount
	                sweetalert(200 ,'Success!' ,'Đã thêm thông báo') 
	            },
	            error: function (error) {
	                sweetalert(error.status)
	            }
	        });
	    }
    }
    
    
});
//<------------- When modal close -> clean form modal  ----------->
$("#form-building").on("hidden.bs.modal", function () {
    cleanForm();
});

// < ---------------------- Clean form ---------------------------->
let cleanForm = () => {
    fillToForm({
        "id":"",
        "title": "",
        "date": "",
        "note":""
    });
}

// < -------------- clean form when click button clean ------------>
document.querySelector('#clean-form').addEventListener('click', cleanForm);

// < ------------------- get value form --------------------------->
let getValueForm = () => {
    return {
        "id": document.querySelector('#id').value,
        "title": document.querySelector('#title').value,
        "date": document.querySelector('#date').value,
        "content":CKEDITOR.instances['note'].getData(),
        "employee": {
            "id": 1   // set mặc định là nv id = 1  sau lm phần đăng nhập rồi get id sau
        },
        
    }
}
let validate = (data) =>  {
	if(data.title === ''){
		toastrError("Tiêu đề không được để trống");
		document.querySelector('#title').focus();
		return false;
	}
	if(data.date === ''){
		toastrError("Ngày không được để trống");
		document.querySelector('#date').focus();
		return false;
	}
	
	if(data.date === ''){
		toastrError("Thông báo không được để trống");
		document.querySelector('#note').focus();
		return false;
	}
	
return true;
}

let fillToForm = (notification) => {
    document.querySelector('#id').value = notification.id;
    document.querySelector('#title').value = notification.title;
    document.querySelector('#date').value = notification.date;
    document.querySelector('#note').value = notification.note;
}