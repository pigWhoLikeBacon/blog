function getAjax(url) {
	$.ajax({
		url : url,
		type : "get",
		async : true,
		success : function(data) {
			Swal.fire(
				'Success!',
				'Operation success.data: ' + data,
				'success'
			)
		},
		error : function(data) {
			Swal.fire(
				'Error!',
				'Operation fail.Connection failure is the root cause.',
				'error'
			)
		}
	});
}