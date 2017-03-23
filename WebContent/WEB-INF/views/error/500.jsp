<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%response.setStatus(200);%>

<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");

	//记录日志
	Logger logger = LoggerFactory.getLogger("500.jsp");
	logger.error(ex.getMessage(), ex);
	
	String xrw = request.getHeader("x-requested-with");
	if(xrw != null && xrw.equals("XMLHttpRequest")){
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write("{\"success\":false,\"message\":\"500-服务器暂时无法响应\",\"data\":null}");
		return ;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>500</title>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

body {
	font-family: "Microsoft Yahei", Arial, Helvetica, sans-serif;
	color: #333;
	background: #FFFFFF;
}

a {
	color: #D93500;
	text-decoration: none;
	font-size: 20px;
}

.main {
	background: #F3F3F3;
	-webkit-box-shadow: 1px 1px 2px 0 rgba(0, 0, 0, .3);
	box-shadow: 1px 1px 2px 0 rgba(0, 0, 0, .3);
	padding: 30px 50px 50px 50px;
}

h1 {
	font-size: 36px;
	padding-bottom: 20px;
}

h1 span {
	font-size: 60px;
	padding-right: 15px;
	color: #B8221C
}

p {
	line-height: 2em;
}

input,button {
	padding: 15px 10px;
	border: 0px;
	background: #F5F6F7;
	border-radius: 0px;
	color: #999;
	outline: 0;
	font-size: 16px;
}

input {
	width: 300px;
}

button {
	background: #0090FF;
	color: #fff;
	cursor: pointer;
	width: 80px;
	overflow: hidden;
}

.form-group {
	margin-top: 10px;
}

.b1,.b2,.b3,.b4,.b1b,.b2b,.b3b,.b4b,.b{display:block;overflow:hidden;}
.b1,.b2,.b3,.b1b,.b2b,.b3b{height:1px;}
.b2,.b3,.b4,.b2b,.b3b,.b4b,.b{border-left:1px solid #EBEBEB;border-right:1px solid #EBEBEB;}
.b1,.b1b{margin:0 5px;background:#EBEBEB;}
.b2,.b2b{margin:0 3px;border-width:2px;}
.b3,.b3b{margin:0 2px;}
.b4,.b4b{height:2px;margin:0 1px;}
.d1{background:#FFFFFF;color:#7D7B7B;}

.content{
	width: 500px;
	margin: 150px auto;
}
.title{
	padding:7px;font-weight: bold;
}
</style>
</head>
<body>
	<div class="content">
		<b class="b1"></b><b class="b2 d1"></b><b class="b3 d1"></b><b class="b4 d1"></b>
		<div class="b d1">
			<div class="title">提示信息</div>
		</div>
		<div class="main">
			<h1>
				<span>500</span>服务器暂时无法响应
			</h1>
			<p>很抱歉!可能是应用程序或者服务器发生错误，请联系管理员</p>
			<p>
				你可以返回 <a href="${ctx}/main/index" target="_top">首页</a>
			</p>
		</div>
	</div>
</body>
</html>
