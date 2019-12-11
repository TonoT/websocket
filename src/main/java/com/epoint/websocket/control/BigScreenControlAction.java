package com.epoint.websocket.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;

public class BigScreenControlAction extends HttpServlet {

    private static final long serialVersionUID = 1552441563513291926L;
    private HttpServletRequest req;
    private HttpServletResponse res;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.req = (HttpServletRequest) request;
        this.res = (HttpServletResponse) response;
        this.doPost(this.req, this.res);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String href = request.getParameter("href");
        
        
        if(StringUtil.isNotBlank(href)){
            List<Object> lst = new ArrayList<>();
            lst.add(href);

            String data = JsonUtil.listToJson(lst);
            ZtbbiWebSocket.BroadMsg(data);  
        }
           
       
    }
}



