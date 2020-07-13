package com.zm.platform.yw.manage.entity.system;

import com.zm.platform.yw.base.BaseEntity;

public class Organization extends BaseEntity {
	
    /***/
	private static final long serialVersionUID = 1L;
    // 机构名称
    private String name;
    // 机构编码
    private String code;
    //上级机构编号
    private Long parentId;
    //上级机构多级编号
    private String parentIds; 
	// 备注
    private String note;
    // 有效标志
    private Integer valid;
    
    public void setName(String name) {
		this.name = name;
	}
    public String getName() {
		return name;
	}
    public Long getParentId() {
		return parentId;
	}
    public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	
    public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
    public String makeSelfAsParentIds() {

        return getParentIds() + getId() + "/";
    }
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "Organization [id=" + id + ", name=" + name + ", parentId=" + parentId + ", note=" + note + ", valid="
				+ valid + "]";
	}
}
