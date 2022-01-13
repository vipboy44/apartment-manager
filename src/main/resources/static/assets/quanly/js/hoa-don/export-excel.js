(function () {
    $.ajax({
        url: URL + `api/apartment-index/all-year`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
        	let html = '';
        	result.forEach(e => html += `<option value="${e}">${e}</option>`);
        	document.getElementById("select_year").innerHTML = html;
        	fillMonth(result[0])
        },
        error: function (error) {
        	console.log(error)
             Swal.fire({
                title : 'Error',
                text :  "Truy vân năm thất bại!",
                icon: 'error'
            })
        }
    });
})();

let fillMonth = (year) =>{
	 $.ajax({
	        url: URL + `api/apartment-index/all-month/${year}`,
	        type: 'GET',
	        dataType: 'json',
	        success: function (result) {
	        	let html = '';
	        	result.forEach(e => html += `<option value="${e}">${e}</option>`);
	        	document.getElementById("select_month").innerHTML = html;
	        },
	        error: function (error) {
	        	console.log(error)
	             Swal.fire({
	                title : 'Error',
	                text :  "Truy vân tháng thất bại!",
	                icon: 'error'
	            })
	        }
	    });
}

document.querySelector('#select_year').addEventListener('change', (e) => {
	fillMonth(e.target.value);
});

