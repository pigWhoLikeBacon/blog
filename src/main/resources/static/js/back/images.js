function selectImg(e) {
	$("#box").find("*").removeClass("simg-active");
	$(e).addClass("simg-active");
}

function imagesRefresh(url) {
    $('#images_refresh').load(url);
}