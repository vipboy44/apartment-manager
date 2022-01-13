let fillToForm = (data) => {
    document.querySelector('#name').value = data.fullName;
    document.querySelector('#birthday').value = data.birthday;
    document.querySelector('#address').value = data.address;
    document.querySelector('#identitycard').value = data.identitycard;
    document.querySelector('#phone').value = data.phone;
    document.querySelector('#username').value = data.username;
    document.querySelector('#email').value = data.email;
    $(data.gender ? "#female" : "#male").prop('checked', true);


}
(function () {
    let username = document.getElementById("account").innerText;
    $.ajax({
        type: 'GET',
        url: URL + `api/account/${username}`,
        dataType: 'json',
        cache: false,
        success: function (result) {
            fillToForm(result.data);
            if (result.data.image) {
        //        document.querySelector('#imgs').src = URL + `assets/quanly/image/${result.data.image}`;
                document.querySelector('#imgs').src = `https://storage.googleapis.com/apartment-management-15f74.appspot.com/photo/user/${result.data.image}`;
            } else {
                document.querySelector('#imgs').src = URL + `assets/quanly/image/someone.png`;
            }
        },
        error: function (error) {

            sweetalertError(error);
        }
    });

})()


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
                .width(300)
                .height(350);
        };
        reader.readAsDataURL(input.files[0]);
        document.querySelector('#upload-now').click();
    }
}

/*  --------------------------------- upload image ---------------------------*/
$("#file-upload-form").on("submit", function (e) {
    e.preventDefault();

    $.ajax({
        url: URL + `api/account/upload-file/${ID_NV}`,
        type: "POST",
        data: new FormData(this),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (result) {
            fillToForm(result.data);
            sweetalertSuccess(result.message);
        },
        error: function (error) {
            sweetalertError(error);
        }
    });
})


document.querySelector('#open-usename').addEventListener('click', () => {
    document.getElementById("username").disabled = false;
    document.getElementById("username").focus();
    document.addEventListener('keypress', function (event) { // sự kiện nhán bàn phím
        if (event.keyCode === 13 || event.which === 13) { // nhấn enter
            let username = document.getElementById("account").innerText;
            let newUserName = document.querySelector('#username').value.trim();
            if (newUserName === '') {
                toastrError("Tên đăng nhập không được để trống!");
                document.querySelector('#username').focus();
            } else if (newUserName.length < 5 || newUserName.length > 20) {

                toastrError("Tên đăng nhập từ 5 đến 20 ký tự!");
                document.querySelector('#username').focus();
            } else {
                $.ajax({
                    type: 'PUT',
                    url: URL + `api/account/change-username/${username}?new_username=${newUserName}`,
                    contentType: 'application/json',
                    dataType: 'json',
                    cache: false,
                    success: function (result) {
                        Swal.fire({
                            title: 'Cập nhật thành công',
                            text: "Xin vui lòng đăng nhập lại để tiếp tục!",
                            icon: 'success',
                            allowOutsideClick: false,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: 'Đồng ý!'
                        }).then((result) => {
                            location.href = URL + `logout`
                        })
                    },
                    error: function (error) {
                        sweetalertError(error)
                    }
                });
            }
        }
        ;

    });


});









