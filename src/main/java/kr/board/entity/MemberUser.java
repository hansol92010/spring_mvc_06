package kr.board.entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;

// 회원 인증 후 사용자 정보를 저장 - ③(UserDatails)
@Data
public class MemberUser extends User {
	
	private Member member;

	public MemberUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	public MemberUser(Member mvo) {
		super(mvo.getMemID(), mvo.getMemPassword(),
				mvo.getAuthList().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));	// List<AuthVO> 타입으로 넘길 수 없고, Collection<SimpleGrantedAuthority> 타입으로 바꾸어야 한다
		
		// 아이디, 비밀번호, 권한을 제외한 정보를 저장
		this.member = mvo;
	}

}
