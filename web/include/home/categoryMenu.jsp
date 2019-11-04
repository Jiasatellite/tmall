<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<div class="categoryMenu">
	<c:forEach items="${cs }" var="c" varStatus="sta">
		<div cid="${c.id }" class="eachCategory">
			<span class="glyphicon plyphicon-link"></span> <a
				href="forecategory?cid=${c.id }"> ${c.name } </a>

		</div>



	</c:forEach>


</div>
