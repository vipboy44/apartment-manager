// add ckeditor
CKEDITOR.replace('note');

(function(){
	 $.ajax({
        type: 'GET',
        url: URL + `api/regulation`,    
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

//< ------------------- fill to form------------------------------>
let fillToForm = (regulation) => {
	CKEDITOR.instances['note'].setData(regulation.content)
}

//< ------------------------ insert or update  ---------------------->
document.querySelector('#save').addEventListener('click', () => {	
    let regulation = getValueForm();

    if(validate(regulation)){
	        $.ajax({
	            type: 'PUT',
	            url: URL + `api/regulation`,
	            contentType: "application/json",
	            dataType: 'json',
	            cache: false,
	            data: JSON.stringify(regulation),
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

let validate =(data)=>
{
	if(data.content === ''){
		toastrError("Nội quy không được để trống!");
		document.querySelector('#note').focus();
		return false;
	}
	return true;
}

let getValueForm = () => {
    return {
    	  "content":CKEDITOR.instances['note'].getData(),
    }
}