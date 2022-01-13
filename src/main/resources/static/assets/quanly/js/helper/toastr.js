
let toastrError = (message) => {
	toastr.error(message,"", {
        progressBar: true,
        positionClass: "toast-top-right",
        preventDuplicates: true,
        showMethod: "show",
        hideMethod: "hide",
        timeOut: 2000,
    })
}
let toastrWarning = (message) => {
        toastr.warning(message, "", {
                progressBar: true,
                positionClass: "toast-top-right",
                preventDuplicates: true,
                showMethod: "show",
                hideMethod: "hide",
                timeOut: 2000,
        })
}

