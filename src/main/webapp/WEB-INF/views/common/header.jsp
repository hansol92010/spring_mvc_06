<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<c:set var="mvo" value="${SPRING_SECURITY_CONTEXT.authentication.principal}"/>
<c:set var="auth" value="${SPRING_SECURITY_CONTEXT.authentication.authorities}"/>
	
	<script>
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfTokenValue = "${_csrf.token}";
		function logout() {
			$.ajax({
				url: "<c:url value='/logout' />",
				type: "post",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(csrfHeaderName, csrfTokenValue)
				},
				success : function() {
					location.href = "<c:url value='/' />"
				},
				error : function() {
					alert("error");
				}
			});
		}
	</script>	
	
	<nav class="navbar navbar-default">
		<div class="container-fluid">
		    <div class="navbar-header">
		      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>                        
		      </button>
		      <a class="navbar-brand" href="${contextPath}/">스프1탄</a>
		    </div>
		    
		    <!-- 위 button(data-target이 #myNavbar)을 눌렀을 때 펼쳐질, id가 myNavbar인 div -->
		    <div class="collapse navbar-collapse" id="myNavbar">
				<ul class="nav navbar-nav">
					<li class="active"><a href="${contextPath}/">Home</a></li>
					<li><a href="${contextPath}/boardMain.do">게시판</a></li>
				</ul>
			
				<!-- 로그인하지 않았을 경우-->
				<security:authorize access="isAnonymous()">	<!-- 인증이 되지 않았음 -->
					<ul class="nav navbar-nav navbar-right">
						<li><a href="${contextPath}/memJoin.do"><span class="glyphicon glyphicon-user"></span> 회원가입</a></li>
						<li><a href="${contextPath}/memLoginForm.do"><span class="glyphicon glyphicon-log-in"></span> 로그인</a></li>
					</ul>
				</security:authorize>
		      
		      	<!-- 로그인한 경우 -->
				<security:authorize access="isAuthenticated()"> <!-- 인증 성공 -->
					<ul class="nav navbar-nav navbar-right">  
						<li><a href="${contextPath}/memUpdateForm.do"><span class="glyphicon glyphicon-wrench"></span> 회원정보수정</a></li>
						<li><a href="${contextPath}/memImageForm.do"><span class="glyphicon glyphicon-picture"></span> 사진등록</a></li>
						<li><a href="javascript:logout()"><span class="glyphicon glyphicon-log-out"></span> 로그아웃</a></li>
					
						<!-- 로그인 상태에 따라 다른 글을 출력 -->
						<li>
						<!-- 기본이미지 -->
						<c:if test="${empty mvo.member.memProfile}">
							<img src="${contextPath}/resources/images/person.png" class="img-circle" style="width:50px; height:50px;"/>
						</c:if>
						<!-- 등록한 이미지 -->
						<c:if test="${!empty mvo.member.memProfile}">
							<li><img src="${contextPath}/resources/upload/${mvo.member.memProfile}" class="img-circle" style="width:50px; height:50px;"/>
						</c:if>
						${mvo.member.memName}님
						(
							<security:authorize access="hasRole('ROLE_USER')">
								U 
							</security:authorize>
							<security:authorize access="hasRole('ROLE_MANAGER')">
								M 
							</security:authorize>
							<security:authorize access="hasRole('ROLE_ADMIN')">
								A 
							</security:authorize>														
						), Welcome!
						</li>	      	
					</ul>
				</security:authorize>
		    </div>
		</div>
	</nav>
