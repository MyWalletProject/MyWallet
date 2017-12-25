package com.mywallet.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mywallet.annotaion.ApiAction;
import com.mywallet.domain.Action;

public class MyWalletUtilities {
	
	private static final Logger logger = LoggerFactory.getLogger(MyWalletUtilities.class);
	
	private static String DOT = ".";

	public static boolean isNull(Object obj){
		return (obj == null)? true : false;
	}
	
	/*
	 * Utility Function For Receiving Activity Object
	 * Corresponding to Handler Method Name  
	 */
	public static Action getActionObject(ApiAction annotaion,String handlerMethodName,String classNameWithPkg){
		Action action = new Action( ( annotaion.actionName().equals("")) ? handlerMethodName : annotaion.actionName()  ,
				annotaion.actionDesc(),
				annotaion.active(),
				(annotaion.handlerMethodName().equals("")) ? classNameWithPkg+"_"+handlerMethodName : annotaion.handlerMethodName()  );
		return action;
	}

	/*
	 * Utilty For Getting All Activity Object used in KYC System
	 */
	public static ArrayList<Action> getAllActionObject(String[] packageNames){
		ArrayList<Action> actions = new ArrayList<Action>();
		Set<String> mappingNames = new HashSet<>();
		for(String packageName : packageNames){
			System.out.println("package is : "+packageName);
			for(String className : classNamesForPackage(packageName.trim())){
				Class<?> handlerClassObj = classForClassName(className);
				if(isNull(handlerClassObj))
					continue;
				Method[] methods = handlerClassObj.getDeclaredMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(ApiAction.class) && Modifier.isPublic(method.getModifiers())) {
						if(!mappingNames.contains(method.getName())){
							actions.add(getActionObject(method.getAnnotation(ApiAction.class),method.getName(),className));
							mappingNames.add(className+"_"+method.getName());
						}else{
							logger.info("Mapping Name ::::::: \""+className+"_"+method.getName()+"\" already exist.");
						}
					}
				}
			}
		}
		return actions;
	}
	
	
	/*
	 * Utility For Fetching All Classes Available in Package
	 */
	public static ArrayList<String> classNamesForPackage(String packageName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ArrayList<String> classNamesOfPackage = new ArrayList<String>();
		String packageNameOrig = packageName;
		packageName = packageName.replace(".", "/");
		URL packageURL = classLoader.getResource(packageName);
		if (!packageURL.getProtocol().equals("jar")) {
			URI uri = null;
			try {
				uri = new URI(packageURL.toString());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			if (uri != null) {
				File folder = new File(uri);
				File[] contenuti = folder.listFiles();
				if (contenuti != null) {
					for (File actual : contenuti) {
						if (actual.isDirectory()) {
							classNamesOfPackage.addAll(classNamesForPackage(packageNameOrig + DOT + actual.getName()));
							continue;
						}
						String entryName = actual.getName();
						entryName = entryName.substring(0, entryName.lastIndexOf(DOT));
						classNamesOfPackage.add(packageNameOrig + DOT + entryName);
					}
				}
			}
		}else {
			try {
				URLConnection urlConnection = packageURL.openConnection();
				if (urlConnection instanceof JarURLConnection) {
					JarURLConnection connection = ((JarURLConnection) urlConnection);
					JarFile jarFile = connection.getJarFile();
					getClassesFromJarEntry(jarFile, packageName, classNamesOfPackage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return classNamesOfPackage;
	}
	
	public static ArrayList<String> getClassesFromJarEntry(JarFile jarFile, String packageName, ArrayList<String> classNamesOfPackage) {
		Enumeration<JarEntry> enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry jarEntry = enumeration.nextElement();
			if ((!jarEntry.isDirectory()) && (jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class"))) {
				String className = jarEntry.getName().replaceAll("/", "\\.");
				classNamesOfPackage.add(className.substring(0, className.length() - 6));
			}
		}
		return classNamesOfPackage;
	}
	
	/**
	 * <p>
	 * This method is used to create an object of class using reflection.
	 * </p>
	 * 
	 * @param className
	 *            Name of the class.
	 * @return Returns an object of specified class.
	 * @exception ClassNotFoundException
	 *                Thrown when an application tries to load in a class
	 *                through its string name.
	 * @see {@link Class#forName(String)}
	 * 
	 */
	public static Class<?> classForClassName(String className) {
		Class<?> object = null;
		try {
			object = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static <E> List<E> getListA_Minus_ListBObjects(List<E> list1,List<E> list2,String comparisionFieldName){
		List<E> uniquesObjArray = new ArrayList<E>();
		if(list1.size() > list2.size()){
			for(E element1 : list1){
				try {
					Field field1 = element1.getClass().getDeclaredField(comparisionFieldName);
					field1.setAccessible(true);
					Object element1Value =field1.get(element1);
					boolean flag = false;
					for(E element2 :  list2){
						Field field2  = element2.getClass().getDeclaredField(comparisionFieldName);
						field2.setAccessible(true);
						Object element2Value = field2.get(element2);
						if(element1Value.equals(element2Value)){
							flag = true;
							break;
						} 	
					}
					if(!flag)
						uniquesObjArray.add(element1);

				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				}catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}

			}
		}
		return uniquesObjArray;
	}
	
}
