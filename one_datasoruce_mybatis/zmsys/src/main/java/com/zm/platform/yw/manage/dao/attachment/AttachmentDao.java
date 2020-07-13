package com.zm.platform.yw.manage.dao.attachment;

import com.zm.platform.yw.base.BaseDao;
import com.zm.platform.yw.manage.entity.attachment.Attachment;

import java.util.List;

public interface AttachmentDao extends BaseDao<Attachment> {
    List<Attachment> getObjectByUserId(Long id);
    int getCountObjectByDisgest(String disgest);
}
