package com.epoint.accesscontrol.server;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PermissionFilter implements Filter {

	private HttpServletRequest req;
	private HttpServletResponse res;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		this.req = (HttpServletRequest) request;
		this.res = (HttpServletResponse) response;
		// 取得根目录所对应的绝对路径:
		String currentURL = this.req.getRequestURI();
		// 截取到当前文件名用于比较
		String targetURL = currentURL.substring(currentURL.indexOf("/", 1), currentURL.length());
		// 不过滤登入的action 用户登录请求和提示用户登录请求直接过
		switch (targetURL) {
		case "/index.do":
		case "/error/errorpage/blank.html":
		case "/publicresource":
		case "/index.html":
		case "/ledMoudle":
		case "/video.html":
		case "/picture.html":
		case "/word.html":
		case "/frame.html":
			break;
		default:
//			// 用户未登录，跳转到提示接口
//			// 获取浏览器访问访问服务器时传递过来的cookie数组
//			Cookie[] cookies = req.getCookies();
//			// 如果用户是第一次访问，那么得到的cookies将是null
//			if (cookies != null) {
//				for (int i = 0; i < cookies.length; i++) {
//					Cookie cookie = cookies[i];
//					if ("loginstate".equals(cookie.getName())) {
//						String state = cookie.getValue();
//						if ("false".equals(state)) {
//							this.req.getRequestDispatcher("/error/errorpage/blank.html").forward(req, res);
//							return;
//						}
//					}
//				}
//			} else {
//				this.req.getRequestDispatcher("/error/errorpage/blank.html").forward(req, res);
//				return;
//			}
			break;
		}

		// 加入filter链继续向下执行
		chain.doFilter(this.req, this.res);
	}

	@Override
	public void destroy() {
		this.req.getSession(false).invalidate();
	}
}
