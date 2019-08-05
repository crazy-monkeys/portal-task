package com.crazy.portal;
import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
public class App extends SpringBootServletInitializer{
	
	public static void main(String[] args) {
		final ApplicationContext context = new SpringApplicationBuilder(App.class).run(args);
		final CamelSpringBootApplicationController controller = context.getBean(CamelSpringBootApplicationController.class);
		controller.run();
//		new SpringApplicationBuilder().sources(App.class).run(args);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		//start-up system
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(App.class);
	}
}
