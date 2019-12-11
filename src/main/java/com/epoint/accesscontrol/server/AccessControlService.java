package com.epoint.accesscontrol.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.epoint.ZtbCommon.ZtbCommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.config.DataSourceFactory;
import com.epoint.database.jdbc.connection.DataSourceConfig;

import javax.servlet.http.Cookie;

/**
 * 权限控制servlet
 * 
 * @author djc
 *
 */
public class AccessControlService extends HttpServlet {

	private static final long serialVersionUID = -8998192599841249065L;
	private static Logger log = LogManager.getLogger(AccessControlService.class);
	private String[] macAddressArr = null;
	private HttpServletRequest req;
	private HttpServletResponse res;

	public void init() throws ServletException {
		// 参数初始化，读取系统参数
		ZtbCommonDao service = null;
		try {
			service = ZtbCommonDao.getInstance();
			String macAddress = service
					.queryString("select CONFIGVALUE from frame_config where CONFIGNAME='macaddress'");
			this.macAddressArr = macAddress.split(";");
		} catch (Exception e) {
			log.error("数据链接错误!", e);
		} finally {
			if (service != null) {
				service.close();
			}
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.req = (HttpServletRequest) request;
		this.res = (HttpServletResponse) response;
		this.doPost(this.req, this.res);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String macaddressStr = request.getParameter("macaddress");
		request.getRequestDispatcher("/index.html").forward(request, response);
//		if (StringUtil.isBlank(macaddressStr)) {
//			request.getRequestDispatcher("/index.html").forward(request, response);
//			return;
//		}
//		// 获取浏览器访问访问服务器时传递过来的cookie数组
//		Cookie[] cookies = request.getCookies();
//		// 如果用户是第一次访问，那么得到的cookies将是null
//		if (cookies != null) {
//			if (true) {
//				for (int i = 0; i < cookies.length; i++) {
//					Cookie cookie = cookies[i];
//					if ("loginstate".equals(cookie.getName())) {
//						cookie.setValue("true");
//						request.getRequestDispatcher("/index.html").forward(request, response);
//						return;
//					}
//				}
//			} else {
//				for (int i = 0; i < cookies.length; i++) {
//					Cookie cookie = cookies[i];
//					if ("loginstate".equals(cookie.getName())) {
//						cookie.setValue("false");
//						request.getRequestDispatcher("/error/errorpage/blank.html").forward(request, response);
//						return;
//					}
//				}
//			}
//
//		} else {
//			if (true) {
//				Cookie cookie = new Cookie("loginstate", "true");
//				response.addCookie(cookie);
//				request.getRequestDispatcher("/index.html").forward(request, response);
//				return;
//			} else {
//				Cookie cookie = new Cookie("loginstate", "false");
//				response.addCookie(cookie);
//				request.getRequestDispatcher("/error/errorpage/blank.html").forward(request, response);
//				return;
//			}
//		}

		/*
		 * if (isPass(macaddressStr)) { HttpSession oldSession =
		 * request.getSession(); Enumeration<String> attrNames =
		 * oldSession.getAttributeNames(); Map<String,String>
		 * attrMap=Common.attrMap; while(attrNames != null &&
		 * attrNames.hasMoreElements()) { String attrName =
		 * String.valueOf(attrNames.nextElement()); attrMap.put(attrName,
		 * String.valueOf(oldSession.getAttribute(attrName))); }
		 * oldSession.invalidate(); HttpSession newSession =
		 * request.getSession(true);
		 * 
		 * Set<String> keySet = attrMap.keySet(); if(keySet != null &&
		 * !keySet.isEmpty()) { Iterator<String> it = keySet.iterator();
		 * while(it != null && it.hasNext()) { String key = (String)it.next();
		 * newSession.setAttribute(key, attrMap.get(key)); } }else{ String
		 * sessionId = newSession.getId(); newSession.setAttribute(sessionId,
		 * "true"); } //response.sendRedirect("index.html");
		 * request.getRequestDispatcher("/index.html").forward(request,
		 * response); return; } else { HttpSession sessionTemp =
		 * request.getSession(); sessionTemp.setAttribute(macaddressStr,
		 * "false"); //response.sendRedirect("/error/errorpage/blank.html");
		 * request.getRequestDispatcher("/error/errorpage/blank.html").forward(
		 * request, response); return; }
		 */
	}

	public boolean isPass(String macaddressStr) {
		// if (macaddressStr == null) {
		// macaddressStr = "";
		// }
		// boolean forward = false;
		// int len = macAddressArr.length;
		// if (macAddressArr != null) {
		// for (int i = 0; i < len; i++) {
		// if (macaddressStr.equals(macAddressArr[i])) {
		// forward = true;
		// break;
		// }
		// }
		// }
		return true;
	}

	@Override
	public void destroy() {
		this.req.getSession(false).invalidate();
	}
}
