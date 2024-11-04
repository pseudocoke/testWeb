package com.test.configure;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan(
		basePackages = {"com.test" }, 
		includeFilters = @Filter(type = FilterType.ANNOTATION, value = { Controller.class }),
		excludeFilters = {
				@Filter(type = FilterType.ANNOTATION, value = { Service.class }),
				@Filter(type = FilterType.ANNOTATION, value = { Repository.class }),
				@Filter(type = FilterType.ANNOTATION, value = { Configuration.class })
		}
)
public class WebMVCApplicationConfiguration extends WebMvcConfigurationSupport  {

	private ObjectMapper objectMapper;

	//	JSON Jackson 설정 
	//	NON_NULL : null 값은 아예 field도 보여주지 말자.
	//	FAIL_ON_UNKNOWN_PROPERTIES : @JsonIgnoreProperties(ignoreUnknown=true) 이거를 모든 VO에 쓴 것과 같은 효과를 활용
	@Bean(name = "jacksonObjectMapper")
	public ObjectMapper settingJacksonObjectMapper()
	{
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		return objectMapper;
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter converter()
	{
		MappingJackson2HttpMessageConverter customJacksonConverter = new MappingJackson2HttpMessageConverter();
    	customJacksonConverter.setObjectMapper(settingJacksonObjectMapper());
    	
    	return customJacksonConverter;
	}
	
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
       StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
       return multipartResolver;
    }
	
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    	MappingJackson2HttpMessageConverter customJacksonConverter = new MappingJackson2HttpMessageConverter();
    	customJacksonConverter.setObjectMapper(settingJacksonObjectMapper());
    	
    	converters.add(customJacksonConverter);
 
        super.configureMessageConverters(converters);
    }
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/img/**").addResourceLocations("/img/");
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		registry.addResourceHandler("/scss/**").addResourceLocations("/scss/");
		registry.addResourceHandler("/vendor/**").addResourceLocations("/vendor/");
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
		registry.addResourceHandler("/index.html").addResourceLocations("/");	//	index.html 조회방지
	}
	
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("/WEB-INF/view/");
		resolver.setSuffix(".jsp");
		resolver.setContentType("text/html; charset=UTF-8");
		return resolver;
	}
	
	//	Interceptor 추가
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//	    	registry.addInterceptor(new RequestProcessingTraceInterceptor());
    }
    
    //	suffix 추가를 위한 설정기능 추가
    //	192.168.0.1 등을 PathVariable로 받아오면 맨 뒤에 .1이 suffix로 인식되어 잘려버리는 이슈 대응
    //	https://www.baeldung.com/spring-mvc-pathvariable-dot 참조
    //	현재는 해당 설정이 true가 가능하나, 앞으로 나오는 Spring 5.3 이후의 버전들은 해당 기능이 false로 고정될 예정이라고 함 (스프링이 좋아지면 없어질 설정)
    @Override
    protected PathMatchConfigurer getPathMatchConfigurer() {
        PathMatchConfigurer pathMatchConfigurer = super.getPathMatchConfigurer();
        pathMatchConfigurer.setUseSuffixPatternMatch(false);

        return pathMatchConfigurer;
    }
	
}
