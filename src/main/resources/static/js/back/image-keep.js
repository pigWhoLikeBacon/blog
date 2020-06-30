//example <img id="image1" onload="keepImage(1, 120, 180)" src="hhd.png">
function keepImage(i, max_h, max_w) {
	var img = new Image();
	img.src = $("#image" + i)[0].src;
	
	img.onload = function(){
		
		var h = img.height;
		var w = img.width;
		
		if (h * 3 > w * 2) {
			$("#image" + i).css("max-width", max_w + "px");
		} else {
			$("#image" + i).css("max-height", max_h + "px");
		}
	};
}