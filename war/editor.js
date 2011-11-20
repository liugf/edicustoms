/**
 * 
 */

$(document).ready(function() {
	function enable(obj){
		obj=$(obj);	
		textArea = $("<input type='text' value='"+obj.html()+"'>");
		textArea.css("width",$(obj).width());
		textArea.css("height",$(obj).height());
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
	
	$(".editable").dblclick(function(){
		enable(this);
	});		
	
});