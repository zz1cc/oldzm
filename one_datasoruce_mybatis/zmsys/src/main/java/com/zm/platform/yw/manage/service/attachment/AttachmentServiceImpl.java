package com.zm.platform.yw.manage.service.attachment;

import com.zm.platform.common.exception.runtime.CurrentException;
import com.zm.platform.common.util.EmptyUtil;
import com.zm.platform.yw.manage.dao.attachment.AttachmentDao;
import com.zm.platform.yw.manage.entity.attachment.Attachment;
import com.zm.platform.yw.manage.entity.system.SysUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Value("${upload.resourceHandler}")
	private String resourceHandler;//请求 url 中的资源映射，不推荐写死在代码中，最好提供可配置，如 /uploadFiles/**
	@Value("${upload.location}")
	private String location;//上传文件保存的本地目录

	public static final String basePath = "zmsys/photo/";

	@Autowired
	private AttachmentDao attachementDao;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<Attachment> upload(List<MultipartFile> multipartFileList, HttpServletRequest request) {

		if(multipartFileList==null || multipartFileList.size()==0){
			throw new CurrentException("文件上传为空!");
		}
		String projectPath = request.getScheme() + "://" +
				request.getServerName() + ":" +
				request.getServerPort() +
				request.getContextPath() + resourceHandler.substring(0, resourceHandler.lastIndexOf("/") + 1);
		String path = basePath + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		File directory = new File(location + path);
		if (!directory.exists()){
			directory.mkdirs();
		}

		List<File> deleteFileList = new ArrayList<>();
		List<Attachment> attachementList = new ArrayList<>();
		SysUser user = (SysUser)SecurityUtils.getSubject().getSession().getAttribute("user");

		for (MultipartFile multipartFile: multipartFileList) {

			try {

				byte[] bytes = multipartFile.getBytes();
				String fileDisgest = DigestUtils.md5DigestAsHex(bytes);

				/*int count = attachementDao.getCountObjectByDisgest(fileDisgest);
				if(count > 0){
					throw new CurrentException(file.getName() + "文件重复！");
				}*/

				String fileName = multipartFile.getOriginalFilename();
				String end = fileName.split("\\.")[1];
				File file = new File(directory, UUID.randomUUID() + "." + end);

				Attachment attachment = new Attachment();
				//文件名
				attachment.setName(file.getName());
				attachment.setDisgest(fileDisgest);
				attachment.setPath(projectPath + path + "/" + file.getName());
				attachment.setSize(multipartFile.getSize());
				//文件类型jpg、png
				attachment.setType(end);
				attachment.setTitle(fileName);
				attachment.setUserId(user.getId());
				if (!attachementDao.saveObject(attachment)) {
					throw new IOException();
				}
				attachementList.add(attachment);
				//写入
				multipartFile.transferTo(file);
				deleteFileList.add(file);

			} catch (IOException e) {
				//操作失败删除之前传的文件
				if(deleteFileList.size()>0) {
					for (File file: deleteFileList) {
						file.delete();
					}
				}
				e.printStackTrace();
				throw new CurrentException("文件上传失败!");
			}
		}
		return attachementList;
	}

	/**
	 * 下载
	 */


	/**
	 * 通过绑定的id查询附件
	 */
	@Override
	public List<Attachment> getObjectByUserId(Long id) {
		if(EmptyUtil.isHasEmpty(id)){
			throw new CurrentException("文件上传为空!");
		}
		return attachementDao.getObjectByUserId(id);
	}
}
