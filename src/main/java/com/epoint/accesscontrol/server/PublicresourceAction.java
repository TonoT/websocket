package com.epoint.accesscontrol.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.epoint.ZtbCommon.ZtbCommonDao;
import com.epoint.basic.bizlogic.uiset.menu.module.domain.FrameModule;
import com.epoint.core.dto.util.JSONArray;
import com.epoint.core.dto.util.JSONException;
import com.epoint.core.dto.util.JSONObject;
import com.epoint.database.jdbc.config.DataSourceFactory;
import com.epoint.msg.operate.MsgOperate;

public class PublicresourceAction extends HttpServlet {

	private static final long serialVersionUID = -6271454967137137666L;

	private List<FrameModule> PublicresourceModule = new ArrayList<FrameModule>();

	private static Logger log = LogManager.getLogger(MsgOperate.class);

	/**
	 * 二级菜单数据
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String urlcode = readJSONString(request);
		PublicresourceModule = getBigDataCode(urlcode);
		JSONArray array = new JSONArray();

		if (PublicresourceModule.size() > 0) {
			for (FrameModule module : PublicresourceModule) {
				JSONObject jsonObject = new JSONObject();
				try {
					if (module.getModuleCode().contains("11190001")) {
						jsonObject.put("url", URLEncoder.encode(module.getModuleName(), "UTF-8"));
					} else {
						jsonObject.put("url", module.getModuleUrl());
					}
					jsonObject.put("englishname", URLEncoder.encode(module.getMoudleMenuName(), "UTF-8"));
					jsonObject.put("name", URLEncoder.encode(module.getModuleName(), "UTF-8"));

				} catch (JSONException e) {
					e.printStackTrace();
				}
				array.put(jsonObject);
			}

			response.getWriter().write(array.toString());
		}
	}

	/**
	 * 取得大数据frameall 菜单 modulecode
	 * 
	 * @return
	 */
	private List<FrameModule> getBigDataCode(String urlcode) {
		ZtbCommonDao service = null;
		service = ZtbCommonDao.getInstance(DataSourceFactory.getFrameDs());
		List<FrameModule> remainedModule = new ArrayList<FrameModule>();
		String sqlmodule = "select modulename,moduleurl,modulecode,moudlemenuname from frame_module where MODULECODE like '"
				+ urlcode + "____' order by ordernumber desc ";
		remainedModule = service.findList(sqlmodule, FrameModule.class);

		if (service != null) {
			service.close();
		}
		return remainedModule;

	}

	public String readJSONString(HttpServletRequest request) {
		
		StringBuffer json = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			log.error("读取json数据错误!", e);
		}
		return String.valueOf(json);
	}
}
