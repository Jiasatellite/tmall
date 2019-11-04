<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script>
	$(function() {
		$("#addForm").submit(function() {
			if (!checkEmpty("name", "屬性名稱")) {
				return false;
			}
			return true;
		});
	});
</script>
<title>屬性管理</title>
</head>
<body>
	<div class="workingAera">
		<ol class="breadcrumb">
			<li><a href="admin_category_list">所以分類</a></li>
			<li><a href="admin_property_list?cid=${c.id }">${c.name}</a></li>
			<li class="active">屬性管理</li>
		</ol>
		<div class="listDataTableDiv">
			<table
				class="table table-striped table-bordered table-hover table-condensed">
				<thead>
					<tr class="success">
						<th>ID</th>
						<th>屬性名稱</th>
						<th>編輯</th>
						<th>刪除</th>
					</tr>
				<tbody>
					<c:forEach items="${ps }" var="p">
						<tr>
							<td>${p.id }</td>
							<td>${p.name }</td>
							<td><a href="admin_property_edit?id=${p.id }"><span
									class="glyphicon glyphicon-edit"></span></a></td>
							<td><a deleteLink="true"
								href="admin_property_delete?id=${p.id }"><span
									class="glyphicon glyphicon-trash"></span></a></td>
						</tr>
					</c:forEach>
				</tbody>
				</thead>
			</table>
		</div>
		<div class="pageDiv">
			<%@include file="../include/admin/adminPage.jsp"%>
		</div>
		<div class="panel panel-warning addDiv">
			<div class="panel-heading">新增屬性</div>
			<div class="panel-body">
				<form method="post" id="addForm" action="admin_property_add">
					<table class="addTable">
						<tr>
							<td>屬性名稱</td>
							<td><input id="name" name="name" type="text"
								class="form-control"></td>
						</tr>
						<tr class="submitTR">
							<td colspan="2" align="center"><input type="hidden"
								name="cid" value="${c.id }">
								<button type="submit" class="btn btn-success">提交</button></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<%@include file="../include/admin/adminFooter.jsp"%>
</body>
</html>





























