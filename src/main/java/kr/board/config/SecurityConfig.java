package kr.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import kr.board.security.MemberUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService memberUserDetailsService() {
		return new MemberUserDetailsService();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberUserDetailsService()).passwordEncoder(passwordEncoder());
		System.out.println("인증매니저 시작");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);
		
		// 요청에 따른 권한을 확인하여 서비스 - ①
		http
			.authorizeRequests()		// 요청에 따른 권한 설정
				.antMatchers("/")		// url에 따른 권한 설정
				.permitAll()			// 특별한 권한이 없어도 접근(접속)을 허용
				.and()					// 다음 권한을 위해 사용
			.formLogin()				// 스프링은 스프링 시큐리티에 기본적으로 제공하는 로그인 UI가 있으니, 별도의 로그인 폼을 사용하려면 아래에 loginPage()를통해 폼의 경로를 주면 된다
				.loginPage("/memLoginForm.do")		// 해당 경로가 로그인 폼을 이동하는 경로임을 지정
				.loginProcessingUrl("/memLogin.do")	// 스프링에서 제공하는 인증 모듈로 향할 경로(memLogin.do는 스프링 필터로 향한다. 따라서 Controller에 별도로 설정한 메서드는 필요없다(memLogin 메서드는 삭제해도 상관없다))
				.permitAll()						// 모든 유저들은 로그인을 할 수 있다
				.and()
			.logout()
				.invalidateHttpSession(true)	// 세션을 끊는다(스프링에서는 로그아웃 시 기본적으로 '/logout' 경로를 쓰도록 정해져 있다)
				.logoutSuccessUrl("/")			// 로그아웃이 성공하면 이동할 경로
				.and()
			.exceptionHandling().accessDeniedPage("/access-denied");	 // 위의 과정들에서 에러가 발생할 경우 - 특정 권한 없이 특정 웹 페이지로 이동하려는 경우 클라이언트를 이동시킬 경로(/access-denided 경로에 대해 Controller에 생성해주어야 한다)
	}

}
