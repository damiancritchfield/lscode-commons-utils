package org.lscode.commons.utils;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class VelocityUtils {

	/**
	 * 根据模板路径获取模板生成字符串
	 * @param templatePath 模板路径
	 * @param replacements 待替换集合
	 * @return 生成的字符串
	 */
	public static String generate(String templatePath, Map<String, Object> replacements) {
		Properties p = new Properties();
		p.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
		p.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
		
		String templateDir = templatePath.substring(0, templatePath.lastIndexOf("/"));
		p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, templateDir);
		Velocity.init(p);

		String[] templatePaths = templatePath.split("/");
		String templateName = templatePaths[templatePaths.length - 1];

		Template template = Velocity.getTemplate(templateName);
		VelocityContext context = toVelocityContext(replacements);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}

	/**
	 * 生成字符串
	 * @param templateContent 模板内容
	 * @param replacements 待替换对象集合
	 * @return 替换后的字符串
	 */
	public static String merge(String templateContent, Map<String, Object> replacements) {
		Properties p = new Properties();
		p.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
		p.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
		// 初始化并取得Velocity引擎
		VelocityEngine engine = new VelocityEngine(p);

		// 取得velocity的上下文context
		VelocityContext context = toVelocityContext(replacements);
		StringWriter writer = new StringWriter();
		engine.evaluate(context, writer, "", templateContent);
		return writer.toString();
	}

	private static VelocityContext toVelocityContext(Map<String, Object> replacements){
		if(null == replacements){
			return null;
		}
		Set<String> keys = replacements.keySet();
		VelocityContext context = new VelocityContext();
		for(String key : keys){
			context.put(key, replacements.get(key));
		}
		return context;
	}
}
