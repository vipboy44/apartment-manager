
$(document).ready(function () {
    // < ----------------------- load data to table  ------------------------------->
    $('#table-notification').DataTable({
        "responsive": true,
        "scroller": {loadingIndicator: true},
        "autoWidth": false,
        "processing": true,
        "autoWidth": false,
        "scrollY": "300px",
        "scrollCollapse": true,
        "sAjaxSource": URL + 'api/notification',
        "sAjaxDataProp": "",
        "order": [[0, "asc"]],
        "aoColumns": [
            {"mData": "id"},
            {"mData": "title"},
            {"mData": "date"},
          
            {"mData": "employee.fullName"},         
            {
                "mRender": function (data, type, full) {
                    return `<i  class="material-icons icon-table icon-update" id="mybtn" onclick='showFormUpdate(${full.id},this)' type="button">edit</i>`
                }
            },
            {
                "mRender": function (data, type, full) {
                    return `<i  class="material-icons icon-table icon-delete " onclick='deleteNotification(${full.id},this)' type="button">delete</i>`
                }
            }
        ]
    });
});





//< -------------------------- show form update --------------------->
let showFormUpdate = (id) => {
   location.href ='http://localhost:8081/apartment-manage.com.vn/ui/notification/form';
   
}



// < ------------------- fill to form------------------------------>
let fillToForm = (notification) => {
    document.querySelector('#id').value = notification.id;
    document.querySelector('#title').value = notification.title;
    document.querySelector('#date').value = notification.date;
    document.querySelector('#note').value = notification.note;
}



//< ----------------------------- Delete ---------------------------->
let deleteNotification = (id, e) => {
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
                url: URL + `api/notification/${id}`,
                contentType: "application/json",
                cache: false,
                success: function (result) {
                    $('#table-notification').DataTable().row($(e).parents('tr'))
                        .remove().draw();
                    sweetalert(200, 'Success!', 'Đã xóa thông báo') // message
                },
                error: function (error) {
                   sweetalert(error.status) //message
                }
            });
        }
    })
}

	
  