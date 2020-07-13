package com.zm.platform.yw.manage.entity.system;

import java.util.List;

import com.zm.platform.yw.base.BaseEntity;

public class Role extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer sort;
	private Integer valid;
	private String note;
	private List<Menu> menuList;
	private List<Permissions> permissionsList;
	
	public List<Menu> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
	public List<Permissions> getPermissionsList() {
		return permissionsList;
	}
	public void setPermissionsList(List<Permissions> permissionsList) {
		this.permissionsList = permissionsList;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", note=" + note + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((permissionsList == null) ? 0 : permissionsList.hashCode());
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
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (permissionsList == null) {
			if (other.permissionsList != null)
				return false;
		} else if (!permissionsList.equals(other.permissionsList))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
