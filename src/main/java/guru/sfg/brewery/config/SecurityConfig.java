package guru.sfg.brewery.config;

import guru.sfg.brewery.security.JpaUserDetailsService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//        return new LdapShaPasswordEncoder();
//        return new StandardPasswordEncoder();
//        return new BCryptPasswordEncoder();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        http.addFilterBefore(restUrlAuthFilter(authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class);

        http
            .authorizeRequests(authorize -> {
                authorize
                        .antMatchers("/h2-console/**").permitAll() // do not use in production
                        .antMatchers("/", "/webjars/**", "/resources/**").permitAll()
                        .antMatchers("/find/**").permitAll()
                        .antMatchers("/beers/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
            })
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .and()
            .httpBasic();

        // h2-console configuration
        http.headers().frameOptions().sameOrigin();
    }

//    @Autowired
//    JpaUserDetailsService jpaUserdetailsService;

    // With fluent API
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(this.jpaUserdetailsService).passwordEncoder(passwordEncoder());

//        auth.inMemoryAuthentication()
//                .withUser("spring")
//                .password("{bcrypt}$2a$10$1IfKLsicrWRQM7r2xCjvV.0AaTPfqEbyYJgluI3TAKxRK1qE2wS7y") //{noop} excludes the password encoder
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("{sha256}2a10461ead13c0617cec88b193acfda517ede979791e2d80758706a1a5f0b0d1875777bcb6e6a0a6")
//                .roles("USER");

//        auth.inMemoryAuthentication().withUser("scott").password("{ldap}{SSHA}3ZYLKqZwUNlW79gjeK9p8qaQLVNTMjg37w1p+Q==").roles("CUSTOMER");
//        auth.inMemoryAuthentication().withUser("scott").password("{bcrypt15}$2a$15$.eLePtnPY.N8Pzca6zO9W.x4Vpt3MMGFQiAb0KzNy/t8J/cB39/K.").roles("CUSTOMER");
//    }


    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}
