package cat.nexia.spring.security;

import cat.nexia.spring.security.jwt.AuthEntryPointJwt;
import cat.nexia.spring.security.jwt.AuthTokenFilter;
import cat.nexia.spring.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuració de seguretat de l'aplicació.
 *  Responsable de la configuració de l'autenticació, autorització i gestió de la sessió
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Defineix un filtre d'autenticació JWT personalitzat com a bean.
     *
     * @return Un filtre d'autenticació JWT.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Configurar i exposar el gestor d'autenticació com a bean.
     *
     * @return Un gestor d'autenticació.
     * @throws Exception Si es produeix un error durant la configuració de l'autenticació manager.
     *
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Estableix el codificador de contrasenya per a l'aplicació.
     *
     * @return Un codificador de contrasenyes basat en BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // List of routes that do not require authentication
    private static final String[] AUTH_WHITELIST = {
            "/authenticate",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/webjars/**"
    };

    /**
     * Configura la seguretat de la sol·licitud HTTP i la gestió de l'autenticació a
     * l'aplicació.
     *
     * @param http La configuració de seguretat per a les sol·licituds HTTP.
     * @throws Exception Si es produeix un error durant la configuració de seguretat.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
