package com.zm.platform.yw.manage.service.attachment;

import com.zm.platform.yw.manage.entity.attachment.Attachment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AttachmentService {

    public List<Attachment> upload(List<MultipartFile> multipartFileList, HttpServletRequest request);

    public List<Attachment> getObjectByUserId(Long id);
}
