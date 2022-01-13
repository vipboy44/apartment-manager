

/*  --------------------------------- upload image ---------------------------*/

$("#send-email").on("submit", function (e) {
    e.preventDefault();  
	 if(ValidateFomrSendEmail()){
		 $.ajax({
		        url: URL + `api/email`,
		        type: "POST",
		        data: new FormData(this),
		        enctype: 'multipart/form-data',
		        processData: false,
		        contentType: false,
		        dataType: 'json',
		        cache: false,
		        beforeSend: function() {
					let timerInterval
					Swal.fire({
						allowOutsideClick: false,
						html: 'Đang gửi...',
						//timer: 5000,				
						onBeforeOpen: () => {
							Swal.showLoading()
							timerInterval = setInterval(() => {
								const content = Swal.getContent()
								if (content) {
									const b = content.querySelector('b')
									if (b) {
										b.textContent = Swal.getTimerLeft()
									}
								}
							}, 100)
						},
						onClose: () => {
							clearInterval(timerInterval)
						}
					}).then((result) => {
						/* Read more about handling dismissals below */
						if (result.dismiss === Swal.DismissReason.timer) {
							console.log('I was closed by the timer')
						}
					})      
		        },
		        success: function (result) {
		        	$('#form-send-email').modal('hide');
		        	console.log(1);
		        	sweetalertSuccess("Gửi email thành công");        	
		        },
		        error: function (error) {
		        	Swal.fire({
		                title: 'Error',
		                text: 'Gửi mail thất bại!',
		                icon: 'error'
		            });
		        }
		    });
	 }   
});

$("#form-send-email").on("hidden.bs.modal", function () {
	document.getElementById("to").value='';
	document.getElementById("subject").value='';
	document.getElementById("body").value='';
	document.getElementById("file-mail").value='';
	
});


$("#file-mail").on("change", function (e) {
    let files = e.currentTarget.files; // puts all files into an array
    // call them as such; files[0].size will get you the file size of the 0th file
    let size = 0;
    for (let x in files) {
    	size += files[x].size;     
    	if(size > 10000000){
    		Swal.fire({
                title: 'File quá lớn!',
                text : 'Kích thước file phải nhỏ hơn 10M',
                icon: 'warning'
            });
            document.getElementById("file-mail").value='';
    		break;
    	}
    }
   
  

});







let ValidateFomrSendEmail= () =>{
	let mail = getValueFormSendEmail();
	if(mail.to === ''){
		toastrError("Chưa nhập người nhận!");
        document.querySelector('#to').focus();
        return false;
	}
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/; 
	if(!filter.test(mail.to))
		{
		toastrError("Email sai dịnh dạng!");
        document.querySelector('#to').focus();
		return false;
	}
	if(mail.subject === ''){
		toastrError("Chưa nhập chủ đề!");
        document.querySelector('#subject').focus();
        return false;
	}
	if(mail.body === ''){
		toastrError("Chưa nhập nội dung!");
        document.querySelector('#body').focus();
        return false;
	}
	return true;
	
}

let getValueFormSendEmail = () => {
	return {
		"to": document.getElementById('to').value.trim(),
		"subject": document.getElementById('subject').value.trim(),
		"body": document.getElementById('body').value.trim(),
	}
}

