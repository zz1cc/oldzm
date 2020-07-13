package com.zm.platform.yw.manage.entity.system;

import java.util.List;

import com.zm.platform.yw.base.BaseEntity;

public class Menu extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String url;
	private String component;
	private Integer type;   //菜单类型：   1：菜单   2：按钮
	private Integer sort;
	private String note;
	private String icon;
	private Long parentId;  //父 菜单id
	private String permission;   //授权，如sys:user:create
	private Integer isJumpLink;  //是否跳转链接 1.是
	
	private List<Menu> children; //子菜单
	private String typeName; //类型字典值
	private Integer halfCheck;  //是否半选中
	
	public Integer getHalfCheck() {
		return halfCheck;
	}
	public void setHalfCheck(Integer halfCheck) {
		this.halfCheck = halfCheck;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public Integer getIsJumpLink() {
		return isJumpLink;
	}
	public void setIsJumpLink(Integer isJumpLink) {
		this.isJumpLink = isJumpLink;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public List<Menu> getChildren() {
		return children;
	}
	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	@Override
	public String toString() {
		return "Menu [id=" + id + ", name=" + name + ", url=" + url + ", type=" + type + ", typeName=" + typeName + ", sort=" + sort + ", note="
				+ note + ", parentId=" + parentId + ", permission=" + permission + ", isJumpLink=" + isJumpLink + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Menu other = (Menu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
