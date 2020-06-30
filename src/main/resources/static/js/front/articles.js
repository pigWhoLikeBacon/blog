$('#list').fadeIn(1000);

function selectArticleSort(e) {
	var url = $(e).find("option:selected").attr("url");
	articlesSortRefresh(url);
}

function articlesSortRefresh(url) {
	var otherUrl = $("#otherUrl").val()
	$('#list').fadeOut(300);
	setTimeout(function(){
		$('#articles_refresh').load(url + otherUrl);
	},300);
	setTimeout(function(){
		$('#list').fadeIn(300);
	},100);
}

function articlesRefresh(url) {
	var sortUrl = $("#sortUrl").val()
	var otherUrl = $("#otherUrl").val()
	$('#list').fadeOut(300);
	setTimeout(function(){
		$('#articles_refresh').load(url + sortUrl + otherUrl);
	},300);
	setTimeout(function(){
		$('#list').fadeIn(300);
	},100);
}