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
	
	function enable(obj){
		obj=$(obj);	
		textArea = $("<input type='text' value='"+obj.html()+"'>");
		textArea.css("width",$(obj).parent().width());
		textArea.css("height",$(obj).parent().height());
		obj.html("");
		obj.append(textArea);
		textArea.focus();
		textArea.blur(function(){
			obj.html(textArea.val());
		});
		textArea.keydown(function(e){
		  if(e.keyCode==13){
			  obj.html(textArea.val());
		  }
		}); 
	}
	$(".content").parent().dblclick(function(){
		enable($(this).children().filter(".content")[0]);
	});
});
