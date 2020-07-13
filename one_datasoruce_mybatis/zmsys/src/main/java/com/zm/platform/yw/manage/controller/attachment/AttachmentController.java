package com.zm.platform.yw.manage.controller.attachment;

import com.zm.platform.yw.manage.entity.attachment.Attachment;
import com.zm.platform.yw.manage.service.attachment.AttachmentService;
import com.zm.platform.yw.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value="/attachment/",produces="text/plain;charset=utf-8")
public class AttachmentController {

	@Autowired
	private AttachmentService attachmentService;

	/**
	 * 文件
	 * @return
	 */
	@RequestMapping(value="upload", produces="application/json;charset=utf-8")
	public R upload(HttpServletRequest request){
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
		List<MultipartFile> multipartFileList = multipartRequest.getFiles("file");
		if (multipartFileList!=null && multipartFileList.size()>0) {
			List<Attachment> attachmentList = attachmentService.upload(multipartFileList, request);
			return R.success(attachmentList);
		}
		return R.error("上传失败！");
	}

	@RequestMapping(value="download",produces="application/json;charset=utf-8")
	public R download(){
		return R.success();
	}
}

