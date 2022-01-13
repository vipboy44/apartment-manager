$('#form-reset-password').on('submit', function (e) {
    e.preventDefault();
    validate()

    if ($('#form-reset-password').valid()) {
        $('#form-reset-password').unbind('submit').submit();
    }
})

function validate() {
    $.validator.addMethod('strongPassword', function (value, element) {
        var regex = new RegExp( "^[a-zA-Z0-9]+$")
            var key = value ;
        if(!regex.test(key)){
            return  false;
        }
        return  true
    }, 'Mật khẩu không được chứa ký tự đặc biệt'),

        $("#form-reset-password").validate({
        onKeyUp: true,
        highlight: function (element) {
            $(element)
                .closest('.form-control')
                .addClass('is-invalid')
        },
        unhighlight: function (element) {
            $(element)
                .closest('.form-control')
                .removeClass('is-invalid')
                .addClass('is-valid')
        },
        rules: {
            email: {
                required: true,
                email: true
            },
            password: {
                strongPassword: true,
                required: true,
                minlength: 8,
                maxlength: 12
            },
            confirmPassword: {
                required: true,
                equalTo: "#password"
            }

        },
        messages: {
            email: {
                required: "E-mail không được để trống",
                email: "Sai định dạng e-mail"
            },
            password: {
                required: "Mật khẩu mới không được để trống",
                minlength: "Mật khẩu tối thiểu 8 ký tự ",
                maxlength: "Mật khẩu tối đa 12 ký tự"
            },
            confirmPassword: {
                required: "chưa nhập xác nhận mật khẩu",
                equalTo: "Xác nhận mật khẩu không khớp nhau"
            }
        }
    });
}


