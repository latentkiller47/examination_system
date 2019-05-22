package com.system.controller;

import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {
  
	 @RequestMapping(value = "upload", produces = "text/html;charset=UTF-8")
		public String StuUpload(@RequestParam("file") MultipartFile file, HttpSession session) {
	 
			// 错误信息  （可以定义一个结果类用于保存信息-错误个数，正确个数，错误信息等）
			ArrayList<String> errors = new ArrayList<String>();
	        
	 
			try {
				if (!file.isEmpty() && file.getOriginalFilename().endsWith("xls")) {
					// poi--exl解析
					InputStream is = file.getInputStream();
					HSSFWorkbook hssf = new HSSFWorkbook(is);
	                //Sheet1-----是Excel表格左下方的名称
					HSSFSheet sheet = hssf.getSheet("Sheet1");
	 
	                //读取Excel表格从下标0开始（表头开始）
					for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	                    
						HSSFCell riName = sheet.getRow(i).getCell(0);
						HSSFCell riBegin = sheet.getRow(i).getCell(1);
						HSSFCell sName1 = sheet.getRow(i).getCell(2);
						HSSFCell sName2 = sheet.getRow(i).getCell(3);
						HSSFCell riEnd = sheet.getRow(i).getCell(4);
						HSSFCell riDistance = sheet.getRow(i).getCell(5);
						HSSFCell riFreight = sheet.getRow(i).getCell(6);
	 
						if (riName == null) {
							errors.add("错误行："+(i+1)+",错误信息:路线名称----不能为空！");
							continue;
						}
						if (riBegin == null) {
							errors.add("错误行："+(i+1)+",错误信息:起点站----不能为空");
							continue;
						}
						if (riEnd == null) {
							errors.add("错误行："+(i+1)+",错误信息:终点站----不能为空");
							continue;
						}
						if (riDistance == null || Double.parseDouble(riDistance.toString()) < 0) {
							errors.add("错误行："+(i+1)+",错误信息:距离----不能为空并且>0");
							continue;
						}
						if (riFreight == null || Double.parseDouble(riFreight.toString()) < 0) {
							errors.add("错误行："+(i+1)+",错误信息:价格----不能为空并且>0");
							continue;
						}
						
						// 途经站个数
						int riStandNum = 0;
						if (sName1 != null) {
							riStandNum++;
						}
						if (sName2 != null) {
							riStandNum++;
						}
						int rs_riId = 0;
						RouteInfo ri = new RouteInfo(riName.toString(), riBegin.toString(), riEnd.toString(), riStandNum,
								Double.parseDouble(riDistance.toString()), Double.parseDouble(riFreight.toString()));
						//判断路线是否存在
						if(routeInfoService.findIsRiName(ri) != 1) {
							//不存在
							//插入数据
							routeInfoService.insert(ri);
							//获得插入时的路线id
							rs_riId = ri.getRiId();
						}else {
							//存在
							errors.add("该路线："+ri.getRiName()+"  已存在");
							continue;
						}
	 
						Stand s1 = new Stand(riBegin.toString());
						Stand s2 = new Stand(riEnd.toString());
						if(standService.findIsSName(s1) != 1) {
							//数据库中不存在
							standService.insert(s1);
						}
						if(standService.findIsSName(s2) != 1) {
							//数据库中不存在
							standService.insert(s2);
						}
						
						//获得途经站id
						Integer rs_sId1 = null;
						Integer rs_sId2 = null;
						if (sName1 != null) {
							Stand s = new Stand(sName1.toString());
							//根据途经站名称查询数据库中是否存在
							if(standService.findIsSName(s) != 1) {
								//数据库中不存在
								standService.insert(s);
								rs_sId1 = s.getsId();
							}else {
								//数据库中存在
								rs_sId1 = standService.findsIdBySName(s);
							}
						}
						if (sName2 != null) {
							Stand s = new Stand(sName2.toString());
							//根据途经站名称查询数据库中是否存在
							if(standService.findIsSName(s) != 1) {
								//数据库中不存在
								standService.insert(s);
								rs_sId2 = s.getsId();
							}else {
								//数据库中存在
								rs_sId2 = standService.findsIdBySName(s);
							}
						}
						
						//插入路线和路线点对应关系
						RouteStand rs = new RouteStand(rs_riId, rs_sId1, rs_sId2);
						routeStandService.insert(rs);
					}
					is.close();
				}
	            //json 数据数据格式
	            //Gson gson = new Gson();
	            //gson.toJson(errors);
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.setAttribute("errors", errors);
			return "ok";
		}
	
}
