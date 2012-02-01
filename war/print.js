function preview(isPreview) {
	if (isPreview) {
		$("#frame div").css("border-color", "#000000");
		$("#frame div").css("color", "#000000");
	} else {
		$("#frame div").css("border-color", "#ffffff");
		$("#frame div").css("color", "#ffffff");
		$(".content").css("color", "#000000");
	}	
}

var toolbar = $("<div id='toolbar' class='noprint'><button type='button' onclick='preview(true);'>打印预览</button><button type='button' onclick='preview(true);window.print();'>打印</button>&nbsp;<button type='button' onclick='preview(false);'>套打预览</button><button type='button' onclick='preview(false);window.print();'>套打</button></div>");
$(document).ready(function() {
	$("body").prepend(toolbar);
});
