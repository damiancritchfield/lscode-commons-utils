package org.lscode.commons.utils;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;

public class ObjectUtils {
	
	/**
	 * 序列化对象为字符串
	 * @param object 对象
	 * @return 序列化字符串
	 */
	public static String serializeToString(Object object) {
		// 此类实现了一个输出流，其中的数据被写入一个 byte 数组。
		// 缓冲区会随着数据的不断写入而自动增长。可使用 toByteArray() 和 toString() 获取数据。
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 专用于java对象序列化，将对象进行序列化
		ObjectOutputStream objectOutputStream = null;
		String serStr = null;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			serStr = byteArrayOutputStream.toString("ISO-8859-1");
			serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return serStr;
	}
	
	/**
	 * 反序列化字符串为对象
	 * @param serializeStr 序列化字符串
	 * @param clazz 类
	 * @param <T> 返回类型
	 * @return 反序列化后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String serializeStr, Class<T> clazz) {
		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			String deserializeStr = java.net.URLDecoder.decode(serializeStr, "UTF-8");
			byteArrayInputStream = new ByteArrayInputStream(deserializeStr.getBytes("ISO-8859-1"));
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			return (T)objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if(objectInputStream != null) {
					objectInputStream.close();
				}
				if(byteArrayInputStream != null) {
					byteArrayInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Map转换层Bean
	 * @param <T> 返回对象类型
	 * @param map 属性集合
	 * @param clazz 类型
	 * @return 返回指定类型的对象，并将属性集合的内容设置到对象属性中
	 */
	@SuppressWarnings("hiding")
	public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
		try {
			T bean = clazz.newInstance();
			BeanUtils.populate(bean, map);
			return bean;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将bean所有属性转换为Map字符串，对于一些自定义类型属性，会转成xxkey=llsh.common.model.Novel@3675463
	 * @param object 对象
	 * @return 返回对象所有属性转换为的map集合
	 */
	public static Map<String, String> beanToMapString(Object object){
		try {
			return BeanUtils.describe(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * javabean的所有属性转换为Map对象形式
	 * @param obj 对象
	 * @return 返回对象所有属性转换为的map集合
	 */
	public static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>(0);
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!"class".equals(name)) {
					params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
	
	/**
	 * 设置对象的属性值
	 * @param object 对象
	 * @param fieldName 属性名
	 * @param fieldValue 属性值
	 * @return 是否成功
	 */
	public static boolean setField(Object object, String fieldName, Object fieldValue) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, fieldValue);
			return true;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 获取对象属性
	 * @param object 对象
	 * @param fieldName 属性名
	 * @return 属性值
	 */
	public static Object getField(Object object, String fieldName) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
