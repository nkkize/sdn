/**
 * 
 */
package com.talentica.sdn.util;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * @author NarenderK
 *
 */
public class CommonUtil {

	public static boolean isEmpty(String str) {
		return (StringUtils.isEmpty(str)) ? Boolean.TRUE : Boolean.FALSE;
	}

	public static boolean isNotEmpty(String str) {
		return (isEmpty(str)) ? Boolean.FALSE : Boolean.TRUE;
	}

	public static boolean isEmpty(String... args) {
		return (null == args || args.length == 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	public static boolean isNotEmpty(String... args) {
		return (isEmpty(args)) ? Boolean.FALSE : Boolean.TRUE;
	}

	public static boolean isNull(Object object) {
		return (null == object) ? Boolean.TRUE : Boolean.FALSE;
	}

	public static boolean isNotNull(Object object) {
		return (isNull(object)) ? Boolean.FALSE : Boolean.TRUE;
	}

	public static <T> boolean isEmpty(Collection<T> list) {
		return (isNull(list) || list.size() == 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	public static <T> boolean isNotEmpty(Collection<T> list) {
		return (isEmpty(list)) ? Boolean.FALSE : Boolean.TRUE;
	}

	public static <K, V> boolean isEmpty(Map<K, V> map) {
		return (isNull(map) || map.size() == 0) ? Boolean.FALSE : Boolean.TRUE;
	}

	public static <K, V> boolean isNotEmpty(Map<K, V> map) {
		return (isEmpty(map)) ? Boolean.FALSE : Boolean.TRUE;
	}

}