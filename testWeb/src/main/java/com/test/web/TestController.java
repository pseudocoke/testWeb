package com.test.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mass.db.service.BasicQueryService;
import com.mass.db.util.ApiResponseUtil;
import com.mass.db.vo.ApiResponseVO;
import com.test.vo.DBGenBaFile;

@Controller
public class TestController {
	
	protected Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	BasicQueryService basicQueryService;

	@RequestMapping("/test.do")
	public @ResponseBody ApiResponseVO test() {
		
		logger.info(">>> API 시작");
		
		DBGenBaFile bf = new DBGenBaFile();
		bf.setFileNo("a3a85055-40a4-40d1-b585-a3104743c993");
		List<DBGenBaFile> dbInfoList = basicQueryService.getSimpleListByObject(bf);
		
		logger.info(">>> 테스트결과 송출");
		
		return ApiResponseUtil.getSuccessResult("정상조회완료", dbInfoList);
		
	}
	
}
