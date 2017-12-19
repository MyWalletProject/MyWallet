package com.mywallet.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;


public class ObjectMap {

	
	private static HashMap<String,Class<?>> wrapperTypeHash = new HashMap<String,Class<?>>();
	final public static String DEFAULT = "DEFAULT";

	static{
		wrapperTypeHash.put("Short", Short.class);
		wrapperTypeHash.put("Byte", Byte.class);
		wrapperTypeHash.put("Integer", Integer.class);
		wrapperTypeHash.put("Long", Long.class);
		wrapperTypeHash.put("Double", Double.class);
		wrapperTypeHash.put("Float", Float.class);
		wrapperTypeHash.put("Void", Void.class);
		wrapperTypeHash.put("Character", Character.class);
		wrapperTypeHash.put("Boolean", Boolean.class);
	}

	private static boolean isUserDefineType(Field field){
		return !(field.getType() == String.class || field.getType().isPrimitive() || isWrrapperType(field.getType().getSimpleName()));
	}

	private static boolean isWrrapperType(String typeName){
		return (wrapperTypeHash.get(typeName) == null) ? false : true; 
	}

	private static boolean  isClass(Class<?> classObj){
		return (!(classObj.isAnnotation() || classObj.isInterface() || classObj.isEnum())); 
	} 

	public static ArrayList<Method> getObjectHashAnnotatedMethods(Method[] methods){
		ArrayList<Method> methodArray = new ArrayList<>();
		for(Method method : methods){
			if(method.isAnnotationPresent(ObjectHash.class)){
				methodArray.add(method);
			}
		}
		return methodArray;
	}
	public static HashMap<String,Object> add_ObjectHash_Annotated_Methods(HashMap<String,Object> objectMap,Object obj){

		for( Method method : getObjectHashAnnotatedMethods(obj.getClass().getMethods())){

			try {
				if(method.isAnnotationPresent(ObjectHash.class)){
					method.setAccessible(true);
					objectMap.put(method.getName(),method.invoke(obj));
				}	
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return objectMap;
	}
	
	private static boolean isAllKeyExist(String keys[],Field[] Fields){
		Set<String> keyMap = new HashSet<String>();
		for(Field field : Fields){
			keyMap.add(field.getName());
		}
		for(String key : keys){
			if(!keyMap.contains(key)){
				try {
					throw new RuntimeException("\""+key+"\" key not found in Object");
				} catch (KeyNotFoundException e) {
					e.printStackTrace();
				}
				return false;
			}
		}
		return true;
	}

	private static String[] getDefaultKeys(Field[] fields){
		String[] keys = new String[fields.length];
		for(int index=0 ;index < keys.length;index++){
			keys[index] = fields[index].getName();
		}
		return keys;
	}

	public static String jsonString(Object obj){
		return new JSONObject(objectMap(obj)).toString();
	}

	public static String jsonString(Object obj,String key){
		return new JSONObject(objectMap(obj,key)).toString();
	}

	public static HashMap<String,Object> objectMap(Object obj) {
		return objectMap(obj,DEFAULT);
	}

	public static HashMap<String,Object> objectMap(Object obj,String keys) {

		if(obj == null || keys == null)
			return null;

		Class<? extends Object> classObj = obj.getClass();

		if (!isClass(classObj))
			return null;

		HashMap<String,Object> objectMap = new HashMap<String,Object>();

		String[] keyArray = keys.trim().split("~");
		Field[] fields = classObj.getDeclaredFields();
		if(keyArray.length < 1)
			return null;

		if(!keyArray[0].equals(DEFAULT)){
			if(keyArray[0].equals("") || fields.length == 0)
				return objectMap;

			if(!isAllKeyExist(keyArray,fields))
				return null;
		}else{
			keyArray= getDefaultKeys(fields);
		}

		for(String key : keyArray){
			try {
				Field field;
				try {
					field = classObj.getDeclaredField(key.trim());
					if(field.isAnnotationPresent(ObjectHash.class) && isUserDefineType(field)){
						ObjectHash objectHash = field.getAnnotation(ObjectHash.class);
						field.setAccessible(true);
						Object newObjectMap = (objectHash.keys().equals("")) ? objectMap(field.get(obj)) : objectMap(field.get(obj),objectHash.keys());
						objectMap.put(field.getName(),newObjectMap);
					}else if(!isUserDefineType(field)){
						field.setAccessible(true);
						objectMap.put(field.getName(),field.get(obj));
					}
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		add_ObjectHash_Annotated_Methods(objectMap,obj);
		return objectMap;
	}

	
	public static List<Object> objectMap(List<?> list,String keys){
		if(list == null)
			return null;
		List<Object> objectArray = new ArrayList<Object>(); 
		for(Object obj : list){
			objectArray.add(objectMap(obj,keys));
		}
		return objectArray;
	}
	
	public static List<Object> objectMap(List<?> list){
		/*if(list == null)
			return null;
		List<Object> objectArray = new ArrayList<Object>(); 
		for(Object obj : list){
			objectArray.add(objectMap(obj));
		}*/
		return ObjectMap.objectMap(list, DEFAULT);
	}
}
