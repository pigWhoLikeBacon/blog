function commentsRefresh(url) {
	var articleIdUrl = $("#articleIdUrl").val()
	$('#comments_refresh').fadeOut(300);
	setTimeout(function(){
		$('#comments_refresh').load(url + articleIdUrl);
	},300);
	setTimeout(function(){
		$('#comments_refresh').fadeIn(300);
	},100);
}