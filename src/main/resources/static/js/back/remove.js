function remove(url)
{
	Swal.fire({
	  title: 'Are you sure?',
	  text: "You won't be able to revert this!",
	  icon: 'warning',
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: 'Yes, delete it!'
	}).then((result) => {
	  if (result.value) {
	    $.ajax({
			url: url,
			type: "get",
			async: true,
			success: function(data){
				if (data == "Success!") {
					Swal.fire(
				      'Deleted!',
				      data,
				      'success'
				    )
				} else {
					Swal.fire(
				      'Error!',
				      data,
				      'error'
				    )
				}
			},
			error: function(data){
				Swal.fire(
			      'Error!',
			      'Delete fail!Connection failure is the root cause.',
			      'error'
			    )
			}
		});
	  }
	})
}