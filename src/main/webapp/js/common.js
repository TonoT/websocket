/**
 * ! [LED大屏控制系统] date:2017-06-05 author: [chengang];
 */

function getNow() {
	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	if (month < 10)
		month = "0" + month;
	var day = now.getDate();
	if (day < 10)
		day = "0" + day;
	var hours = now.getHours();
	if (hours < 10)
		hours = "0" + hours;
	var minutes = now.getMinutes();
	if (minutes < 10)
		minutes = "0" + minutes;
	var seconds = now.getSeconds();
	if (seconds < 10)
		seconds = "0" + seconds;
	var week = new Array("日", "一", "二", "三", "四", "五", "六");
	var weekday = week[now.getDay()];
	return "<p class='time'>" + hours + ":" + minutes
			+ "</p><p class='date' > " + year + "/" + month + "/" + day
			+ "&nbsp;&nbsp; 星期" + weekday + "</p>"
	return timeStr;
}

$(function() {
	(function() {
		var arg = arguments;
		var nows = getNow();
		$("#timer").html(nows);
		setTimeout(function() {
			arg.callee();
		}, 1000);
	})();

	if ($(".swiper-container").length) {
		var mySwiper = new Swiper('.swiper-container', {
			pagination : '.swiper-pagination',
			paginationClickable : true
		});

		var $listcontainer = $(".swiper-container");
		$listcontainer.on("click", "a", function(event) {
			event.preventDefault();
			$("li", $listcontainer).removeClass("active");
			$(this).closest("li").addClass("active");
			var href=$(this).data("href")
			if(href){
				$.ajax({
					type : 'POST',
					dataType : 'json',
					contentType : "application/x-www-form-urlencoded; charset=utf-8",
					async : false,
					url : "screencontroller",
					data : {
						href : href
					},
					success : function(data) {
						
					}
				});
			}
			

		})
	}
})

/*$(function() {
	var links = document.getElementsByTagName('a');
	for (var i = 0; i < links.length; i++) {
		links[i].tName = links[i].innerText;
		links[i].onclick = (function(dingObj) {
			return function() {
				var path = dingObj.getAttribute("data-href");
				path = '{"path":"' + path + '"}';
				$.ajax({
					type : "POST",
					async : false,
					url : "msgOperate",
					data : path,
					dataType : 'json',
					success : function(msg) {
						// alert(msg.status);
					},
					error : function(msg) {
						// alert(msg.status);
					}
				});
			};
		})(links[i]);
	}
})*/