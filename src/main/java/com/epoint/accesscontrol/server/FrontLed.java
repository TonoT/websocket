package com.epoint.accesscontrol.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontLed extends HttpServlet {

	private static final long serialVersionUID = 1552441563513291926L;
	private HttpServletRequest req;
	private HttpServletResponse res;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.req = (HttpServletRequest) request;
		this.res = (HttpServletResponse) response;
		this.doPost(this.req, this.res);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String subpage = request.getParameter("subpage");
		res.sendRedirect("frame.html?subpage=" + subpage);
	}
}
