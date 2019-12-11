/**
 * ! [LED大屏控制系统] date:2017-06-05 author: [chengang];
 */
$(function() {
	var $navcol = $("#navcol");
	$navcol.niceScroll({
		cursorcolor : "transparent",
		cursorborder : "1px solid transparent",
	});

	$navcol.on('click', 'a', function(event) {
		event.preventDefault();
		var href = $(this).data("href");
		//window.location.href = "frame.html?subpage=" + href;
		window.location.href = "frontLed?subpage=" + href;
	});
	function decodeUTF8(str) {
		return str.replace(/(\\u)(\w{4}|\w{2})/gi, function($0, $1, $2) {
			return String.fromCharCode(parseInt($2, 16));
		});
	}
	$.ajax({
		type : 'POST',
		dataType : 'json',
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		async : false,
		url : "ledMoudle",
		success : function(data) {
			var li = "";
			for (var i = 0; i < data.length; i++) {
				var name = decodeURI(data[i].name);
				var arr = data[i].url.split("/");
				if (i % 2 == 0) {
					li = li + " <li class='" + arr[arr.length-1].toLowerCase() + " odd'><a data-href='"
							+ data[i].code + "'>" + name + "</a></li>";
				} else {
					li = li + " <li class='" + arr[arr.length-1].toLowerCase() + "'><a data-href='" + data[i].code
							+ "'>" + name + "</a></li>";
				}
			}
			$(".mainnavlist").html(li);
		}
	});

})
