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
                "/doctor/listpatients/*",
                "/doctor/addpatient/*","/doctor/getPatientById/*",
                "/doctor/tokenDoctor","/doctor/viewRecipes/*","/doctor/AproveRecipe/*",
                "/doctor/DeclineRecipe/*","/doctor/getAllPharmacys"
        );
        //Patient Urls
        filter.addUrlPatterns("/patient/getPatients","/patient/getPatientById/*",
                "/patient/*/addInsurance","/patient/deletePatientById/*",
                "/patient/changePatientPassword/*","/patient/*/addInsurance",
                "/patient/*/makeRecipe","/patient/tokenPatient","/patient/viewDoctors/*",
                "/patient/viewRecipes/*"
                );
        //Pharmacy Urls
        filter.addUrlPatterns("/pharmacy/getPharmacy",
                "/pharmacy/getPharmacyById/*","/pharmacy/tokenPharmacy",
                "/pharmacy/getRecipesByStatus/*",
                "/pharmacy/viewRecipeHistory/*",
                "/pharmacy/markRecipe/*");

        return filter;
    }
}
