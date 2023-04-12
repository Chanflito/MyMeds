package MyMeds.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.GenericFilterBean;
import static MyMeds.jwt.JwtGeneratorImpl.secretKey;
import java.io.IOException;
@CrossOrigin
public class JwtFilter extends GenericFilterBean {
    //Makes filter on the url specified in FilterConfig, checks users Token.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request=(HttpServletRequest) servletRequest;
        final HttpServletResponse response=(HttpServletResponse) servletResponse;
        final String authHeader=request.getHeader("authorization");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        if ("OPTIONS".equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request,response);
        }
        else{
            try{
                if (authHeader!=null && authHeader.startsWith("Bearer " )) {
                    String[] jwtParts = authHeader.substring(7).split("\\.");
                    if (jwtParts.length == 3) {
                        final String token = authHeader.substring(7); //Porque miramos apartir del Bearer
                        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
                        request.setAttribute("claims", claims);
                        request.setAttribute("user", servletRequest.getParameter("id"));
                        String role = (String) claims.get("Role");
                        String requestURL = request.getRequestURI();
                        if (requestURL.startsWith("/patient") && role.equals("PATIENT")) {
                            filterChain.doFilter(request, response);
                        } else if (requestURL.startsWith("/doctor") && role.equals("DOCTOR")) {
                            filterChain.doFilter(request, response);
                        } else if (requestURL.startsWith("/pharmacy") && role.equals("PHARMACY")) {
                            filterChain.doFilter(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        }
                    } else {
                        throw new ServletException("Error");
                    }
                }else{
                    throw  new ServletException("Error");
                }


            }catch (SignatureException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"The token is invalid.");
            }
            catch (ServletException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
            }
        }
    }
}
