<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>产品编辑</title>
</head>
<body>
<div class="workingArea">
		<ol class=breadcrumb>
<li><a href="admin/admin_category_list">所有分类</a></li>
<li><a href="admin/admin_product_list?cid=${p.category.id }">${p.category.name }</a></li>
<li class="active">编辑产品</li>

</ol>
<div class="panel panel-warning editDiv">
        <div class="panel-heading">编辑产品</div>
        <div class="panel-body">
            <form method="post" id="editForm" action="admin_product_update">
                <table class="editTable">
                    <tr>
                        <td>产品名称</td>
                        <td><input id="name" name="name" value="${p.name}"
                            type="text" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>产品小标题</td>
                        <td><input id="subTitle" name="subTitle" type="text"
                        value="${p.subTitle}"
                            class="form-control"></td>
                    </tr>
                    <tr>
                        <td>原价格</td>
                        <td><input id="orignalPrice" value="${p.orignalPrice}" name="orignalPrice" type="text"
                            class="form-control"></td>
                    </tr>
                    <tr>
                        <td>优惠价格</td>
                        <td><input id="promotePrice"  value="${p.promotePrice}" name="promotePrice" type="text"
                            class="form-control"></td>
                    </tr>
                    <tr>
                        <td>库存</td>
                        <td><input id="stock"  value="${p.stock}" name="stock" type="text"
                            class="form-control"></td>
                    </tr>
                                         
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                        <input type="hidden" name="id" value="${p.id}">
                        <input type="hidden" name="cid" value="${p.category.id}">
                        <button type="submit" class="btn btn-success">提 交</button></td>
                    </tr>
                </table>
            </form>
        </div>

</div>



</div>
</body>
</html>