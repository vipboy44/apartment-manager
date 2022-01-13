(function () {
    $.ajax({
        url: URL + `api/apartment/listid`,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            $("#apartment_id").select2({
                data: result,
                "language": {
                    "noResults": function(){
                        return "Không tìm thấy căn hộ";
                    }
                },
                escapeMarkup: function (markup) {
                    return markup;
                }

            });
        },
        error: function (error) {
            sweetalertError(error.status)
        }
    });
    // $.ajax({
    //     url: URL + `api/resident/listid`,
    //     type: 'GET',
    //     dataType: 'json',
    //     success: function (result) {
    //         $("#resident_id").select2({
    //             data: result,
    //             "language": {
    //                 "noResults": function(){
    //                     return "Không tìm thấy cư dân";
    //                 }
    //             },
    //             escapeMarkup: function (markup) {
    //                 return markup;
    //             }
    //
    //         });
    //     },
    //     error: function (error) {
    //         sweetalertError(error.status)
    //     }
    // });
})()

