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
        filter.addUrlPatterns("/doctor/getDoctors","/doctor/getDoctorById/*",
                "/doctor/deletePatientById/*",
                "/doctor/listpatients/*",
                "/doctor/addpatient/*"
        );
        //Patient Urls
        filter.addUrlPatterns("/patient/getPatients","/patient/getPatientById/*",
                "/patient/*/addInsurance",
                "/patient/*/makeRequest","/patient/deletePatientById/*"
                );
        //Pharmacy Urls
        filter.addUrlPatterns("/pharmacy/getPharmacy",
                "/pharmacy/getPharmacyById/*",
                "/pharmacy/deletePharmacyById/*");
        return filter;
    }
}
