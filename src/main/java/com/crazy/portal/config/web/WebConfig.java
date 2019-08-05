package com.crazy.portal.config.web;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setSupportedMediaTypes(Arrays.asList(new MediaType(
				"text", "plain", Charset.forName("UTF-8"))));
		converters.add(stringConverter);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//类目文件导入页面
		registry.addViewController("/upload.html").setViewName("upload");
		registry.addViewController("/").setViewName("forward:/scheduleJob/list");
	}

}
