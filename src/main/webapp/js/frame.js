/**
 * ! [LED大屏控制系统] date:2017-06-08 author: [chengang];
 */
$(function() {
	var getUrlParams = function(prop) {
		var params = {}, query = location.search.substring(1), arr = query
				.split('&'), rt;

		$.each(arr, function(i, item) {
			var tmp = item.split('='), key = tmp[0], val = tmp[1];

			if (typeof params[key] == 'undefined') {
				params[key] = val;
			} else if (typeof params[key] == 'string') {
				params[key] = [ params[key], val ];
			} else {
				params[key].push(val);
			}
		});
		rt = prop ? params[prop] : params;
		return rt;
	};

	$.ajax({
		type : 'POST',
		dataType : 'json',
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		async : false,
		url : "ledMoudle",
		success : function(data) {
			var li = "";
			for (var i = 0; i < data.length; i++) {
				var name = decodeURI(data[i].name);
				var arr = data[i].url.split("/");
				li = li + " <li class='" + arr[arr.length - 1].toLowerCase()
						+ "'><a href='" + data[i].url + ".html?modulecode="
						+ data[i].code + "'>" + name + "</a></li>";

			}
			$("#ulnav").html(li);
		}
	});

	var $navwrap = $("#navwrap"), $pageframe = $("#pageframe");
	$navwrap.niceScroll({
		cursorcolor : "transparent",
		cursorborder : "1px solid transparent",
	});
	$navwrap.on('click', 'a', function(event) {
		event.preventDefault();
		$("li", $navwrap).removeClass("active");
		var $li = $(this).parent("li");
		$li.addClass("active");
		var href = this.href;
		$pageframe.attr('src', href);
	});
	var subpage = getUrlParams("subpage") || "11190001";
	$("a[href*='" + subpage + "']", $navwrap).click();
})
