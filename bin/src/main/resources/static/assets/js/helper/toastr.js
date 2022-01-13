
let toastrError = (message) => {
	toastr.error(message,"Error", {
        progressBar: true,
        positionClass: "toast-top-right",
        preventDuplicates: true,
        showMethod: "show",
        hideMethod: "hide",
    })
}
