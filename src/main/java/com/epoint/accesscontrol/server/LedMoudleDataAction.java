package com.epoint.accesscontrol.server;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.epoint.ZtbCommon.ZtbCommonDao;
import com.epoint.basic.bizlogic.uiset.menu.module.domain.FrameModule;
import com.epoint.core.dto.util.JSONArray;
import com.epoint.core.dto.util.JSONException;
import com.epoint.core.dto.util.JSONObject;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.database.jdbc.config.DataSourceFactory;

public class LedMoudleDataAction extends HttpServlet {

	private static final long serialVersionUID = -6457645851898498658L;

	transient protected static final Logger logger = LogUtil.getLog(LedMoudleDataAction.class);

	private static List<FrameModule> remainedModule = new ArrayList<FrameModule>();

	/**
	 * 一级菜单数据
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (remainedModule.isEmpty()) {
			remainedModule = getBigDataCode();
		}
		JSONArray array = new JSONArray();
		if (remainedModule.size() > 0) {
			for (int i = 0; i < remainedModule.size(); i++) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("url", remainedModule.get(i).getModuleUrl());
					jsonObject.put("name", URLEncoder.encode(remainedModule.get(i).getModuleName(), "UTF-8"));
					jsonObject.put("code", URLEncoder.encode(remainedModule.get(i).getModuleCode(), "UTF-8"));
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
	private List<FrameModule> getBigDataCode() {
		ZtbCommonDao service = null;
		service = ZtbCommonDao.getInstance(DataSourceFactory.getFrameDs());
		String sqlmodule = "";
		if (service.isMySql()) {
			sqlmodule = "select modulename,moduleurl,modulecode from frame_module "
					+ "where MODULECODE like concat('',(select modulecode from frame_module where modulename = 'LED大屏控制菜单管理'),'____') "
					+ "order by ordernumber desc";
		}
		if (service.isOracle()) {
			String sql = "select modulecode from frame_module where modulename = 'LED大屏控制菜单管理'";
			String modulecode = service.queryString(sql);
			sqlmodule = "select modulename,moduleurl,modulecode from frame_module where MODULECODE like '" + modulecode
					+ "____' order by ordernumber desc";
		}

		List<FrameModule> remainedModule = service.findList(sqlmodule, FrameModule.class);
		if (service != null) {
			service.close();
		}
		return remainedModule;

	}

}
