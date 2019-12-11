$(function() {
	function decodeUTF8(str) {
		return str.replace(/(\\u)(\w{4}|\w{2})/gi, function($0, $1, $2) {
			return String.fromCharCode(parseInt($2, 16));
		});
	}
	function GetRequest() {
		var name, value;
		var str = location.href; // 取得整个地址栏
		console.log(str);
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
	$
			.ajax({
				type : 'POST',
				dataType : 'json',
				contentType : "application/x-www-form-urlencoded; charset=utf-8",
				async : false,
				url : "publicresource",
				data : request.modulecode,
				success : function(data) {

					var div = "";
					var num = Math.ceil(data.length / 8);
					var start = 0;
					var end = 0;
					for (var j = 0; j < num; j++) {
						start = j * 8;
						end = (j + 1) * 8;
						if (end > data.length) {
							end = data.length;
						}
						div += " <div class='swiper-slide'>  <ul class='resourcelist'>";
						for (var i = start; i < end; i++) {
							var name = decodeURI(data[i].name);
							var englishname = data[i].englishname.replace("+",
									" ");
							div = div + "  <li><a data-href='" + data[i].url
									+ "' class='mlink'><p class='cn'>" + name
									+ "</p><p class='en'>" + englishname
									+ "</p><span class='down'></span></a></li>";
						}
						div += "</ul>";
						div += "</div>";
					}

					$(".swiper-wrapper").html(div);

				}
			});

})