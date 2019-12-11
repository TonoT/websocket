$(function() {

	function GetRequest() {
		var name, value;
		var str = location.href; // 取得整个地址栏
		var num = str.indexOf("?")
		str = str.substr(num + 1); // 取得所有参数 stringvar.substr(start [, length ]

		var arr = str.split("&"); // 各个参数放到数组里
		for (var i = 0; i < arr.length; i++) {
			num = arr[i].indexOf("=");
			if (num > 0) {
				name = arr[i].substring(0, num);
				value = arr[i].substr(num + 1);
				this[name] = value;
			}
		}
	}

	var request = new GetRequest();
	$.ajax({
		type : 'POST',
		dataType : 'json',
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		async : false,
		url : "publicresource",
		data : request.modulecode,
		success : function(data) {

			var div = "";
			var num = Math.ceil(data.length / 4);
			var start = 0;
			var end = 0;
			var strurl = window.location.href;
			strurl = strurl.substring(0, strurl.lastIndexOf("/"));
			for (var j = 0; j < num; j++) {
				start = j * 4;
				end = (j + 1) * 4;
				if (end > data.length) {
					end = data.length;
				}
				div += " <div class='swiper-slide'>  <ul class='piclist'>";
				for (var i = start; i < end; i++) {
					var name = decodeURI(data[i].name);
					var srcurl = data[i].url.substring(data[i].url
							.lastIndexOf('/') - 14);

					div = div + " <li><a data-href='" + strurl
							+ "/picture.html?pictureurl=" + data[i].url
							+ "' class='mlink'><img src='" + srcurl
							+ "' height='260' width='640' /><h2 class='title'>"
							+ name + "</h2></a></li>";

				}
				div += "</ul>";
				div += "</div>";
			}

			$(".swiper-wrapper").html(div);
		}
	});

})