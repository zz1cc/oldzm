package com.zm.platform.common.util;


public class EmptyUtil {
	
	/**  判空  **/
	
	/**
	 * 	单个对象非空 
	 *  
	 * !=null !=""
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		return obj != null && !"".equals(obj);
	}
	
	/**
	 * 	为空
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		return obj == null || "".equals(obj);
	}

	/**
	 * 	多个对象非空
	 * @param objs
	 * @return
	 */
	public static boolean isNotEmpty_All(Object ...objs) {
		for(Object obj: objs) {
			if(obj == null || "".equals(obj)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 *	全部为空
	 * @param objs
	 * @return
	 */
	public static boolean isEmpty_All(Object ...objs) {
		for(Object obj: objs) {
			if(obj != null && !"".equals(obj)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 	对象至少包含一个空的
	 * @param objs
	 * @return
	 */
	public static boolean isHasEmpty(Object ...objs) {
		for(Object obj: objs) {
			if(obj == null || "".equals(obj)) {
				return true;
			}
		}
		return false;
	}
}
