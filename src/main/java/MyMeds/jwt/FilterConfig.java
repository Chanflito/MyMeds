package MyMeds.jwt;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {
    //JWT Filter
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter(){
        FilterRegistrationBean<JwtFilter> filter= new FilterRegistrationBean<>();
        filter.setFilter(new JwtFilter());
        //Doctor Urls
        filter.addUrlPatterns("/doctor/*");
        //Patient Urls
        filter.addUrlPatterns("/patient/*");
        //Pharmacy Urls
        filter.addUrlPatterns("/pharmacy/*");


        return filter;
    }
}
