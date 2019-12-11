$(function() {



	
	
	// ----------------------------------------------------------------------------//
	// websocket跳转控制
	$(document).ready(function() {

		try {
			var WsShell = new ActiveXObject('WScript.Shell');
			var backurl = document.referrer;
			if (backurl.indexOf("index") < 0 && backurl.indexOf("mediaView") < 0) {
				WsShell.SendKeys('{F11}');
			}
		} catch (e) {

		}
		initBroadcast();
	});

	function initBroadcast() {
		var isfrmclose = false;
		window.onbeforeunload = function() {
			isfrmclose = true;
		};
		var transport = 'websocket';
		var request = {
			url : window.location.protocol + "//" + window.location.host + _rootPath + "/websocket/ztbbi",
			contentType : "application/json",
			logLevel : 'info',
			transport : 'websocket',
			fallbackTransport : 'long-polling',
			headers : {

			}
		};
		// 监听连接打开
		request.onOpen = function(response) {
			transport = response.transport;
			// 发送初使信息
			// push方法推送数据
			subSocket.push(JSON.stringify({
				"Type" : "Enter"
			}));
		};
		// 监听连接关闭
		request.onClose = function(response) {
			if (!isfrmclose)
				alert("您已断线，请尝试重新刷新页面！");
		};
		request.onError = function(response) {
			if (!onError) {
				alert('您与服务器的连接已中断，请尝试重新刷新页面！');
				onError = true;
			}
		};
		// 监听服务端发送的数据
		request.onMessage = function(response) {
			var msg = response.responseBody;
			// 注意：在正常WebSocket连接中，一个json服务端会推送一次，所以msgStr一定是个json格式的字符串；
			// 在长轮询中，一次请求可能会取到多个json，但是并不是以数组格式返回，而是简单的拼接
			// eg:在服务端推送了{"text","你好"}和{"text","谢谢"}两个json，如果在同一次轮需被获取到msgStr的格式是{"text","你好"}{"text","谢谢"}
			// 在下面需要对长轮询取到的消息做处理，转成数组格式的字符串，然后进行操作。
			if (transport == "long-polling") {
				msg = "[" + msg.replace(/\}\{/g, '},{').replace(/\}\[/g, '},[') + "]";
			}
			msg = JSON.parse(msg);
			DoMessage(msg);
		};
		// websocket客户端
		var socket = atmosphere;
		// 建立连接，异步执行
		var subSocket = socket.subscribe(request);
		var index = 0;
		var DoMessage = function(messageList) {
			if (!(messageList instanceof Array)) {
				messageList = [ messageList ];
			}
			var msg = messageList;
			if (msg.length == 1)
				msg = msg[0];// 兼容IE9
			if(typeof(msg)=="string"){				
				$('.pages-frame').attr('src', msg)
			}

		}
	}

	var _rootPath = (function() {
		var path = location.pathname;

		if (path.indexOf('/') == 0) {
			path = path.substring(1);
		}

		return '/' + path.split('/')[0];
	}());

	function IFrameReSize(iframename) {

		var pTar = document.getElementById(iframename);

		if (pTar) { //ff

			if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight) {

				pTar.height = pTar.contentDocument.body.offsetHeight;

			} //ie

			else if (pTar.Document && pTar.Document.body.scrollHeight) {

				pTar.height = pTar.Document.body.scrollHeight;

			}

		}

	}

	//iframe宽度自适应

	function IFrameReSizeWidth(iframename) {

		var pTar = document.getElementById(iframename);

		if (pTar) { //ff

			if (pTar.contentDocument && pTar.contentDocument.body.offsetWidth) {

				pTar.width = pTar.contentDocument.body.offsetWidth;

			} //ie

			else if (pTar.Document && pTar.Document.body.scrollWidth) {

				pTar.width = pTar.Document.body.scrollWidth;

			}

		}

	}

})
