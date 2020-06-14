<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../public.jsp"/>

<section id="content_wrapper">
    <!-- Begin: Content -->
    <section id="content" class="table-layout animated fadeIn">
        <!-- begin: .tray-center -->
        <div class="tray tray-center">
            <!-- Begin: Content Header -->
            <div class="content-header">
                <h2>交接单总表</h2>
                <p class="lead">集团研究院资产交接清单</p>
            </div>
            <!-- message listing panel -->
            <div class="admin-form theme-primary mw1100 center-block">
                <form action="${pageContext.request.contextPath}/handover_order/search" method="post">
                    <div class="section">
                        <div class="section row center-block">
                            <div class="col-md-3">
                                <div class="form-group">
                                    <div class="input-group">
                                        <input type="text" class="form-control" id="assetNumbers" name="assetNumbers"
                                               placeholder="编号搜索（多编号以“,”分割）..." style="width:120%" value="${assetNumbers}">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <select type="text" class="form-control" id="transferTeam" name="transferTeam" style="width:90%">
                                        <option value="" selected="selected">请选择移交单位搜索...</option>
                                        <c:forEach items="${teams}" var="team" >
                                            <option value="${team.teamName}" <c:if test="${team.teamName==transferTeam}"> selected="selected" </c:if> >${team.teamName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <select type="text" class="form-control" id="acceptTeam" name="acceptTeam" style="width:90%">
                                        <option value="" selected="selected">请选择接收单位搜索...</option>
                                        <c:forEach items="${teams}" var="team" >
                                            <option value="${team.teamName}" <c:if test="${team.teamName==acceptTeam}"> selected="selected" </c:if> >${team.teamName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-1">
                                <div class='input-group'>
                                    <button type="submit" class="btn btn-default btn-lg" id="search">
                                        <span class="glyphicon glyphicon-search"></span> Go！
                                    </button>
                                </div>
                            </div>
                            <%--<div class="col-md-1">
                                <div class='input-group'>
                                    <button type="button" class="btn btn-default btn-lg" id="excel" title="点击导出Excel">
                                        <span class="glyphicon glyphicon-download-alt"></span> 导出
                                    </button>
                                </div>
                            </div>--%>
                        </div>
                    </div>
                </form>
                <div class="panel  heading-border">
                    <!-- message listings table -->
                    <div class="panel-body pn">
                        <table id="message-table" class="table table-striped table-hover">
                            <thead>
                            <tr class="">
                                <th class="hidden-xs">序号</th>
                                <th class="hidden-xs">资产编号</th>
                                <th class="hidden-xs">移交单位</th>
                                <th class="hidden-xs">接收单位</th>
                                <th class="hidden-xs">移交时间</th>
                                <th class="hidden-xs">状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="message-unread">
                                <c:forEach items="${list}" var="handoverOrder" varStatus="status">
                                <td class="">${status.index+1}</td>
                                <td class="">
                                    <c:forEach items="${handoverOrder.assets}" var="asset">
                                        <a href="#" onclick="showAsset(${asset.id})"
                                           title="${asset.categoryAsset.name},${asset.model},${asset.specification}">${asset.number}</a>
                                    </c:forEach>
                                </td>
                                <td class="">${handoverOrder.transferTeam}</td>
                                <td class="">${handoverOrder.acceptTeam}</td>
                                <td class="">${handoverOrder.transferDate}</td>
                                <td class="">
                                    <c:choose>
                                        <c:when test="${handoverOrder.statusNo==4}">
                                            已完成
                                        </c:when>
                                        <c:when test="${handoverOrder.statusNo==3}">
                                            接收审核中
                                        </c:when>
                                        <c:when test="${handoverOrder.statusNo==2}">
                                            移交审核中
                                        </c:when>
                                        <c:when test="${handoverOrder.statusNo==1}">
                                            驳回
                                        </c:when>
                                        <c:when test="${handoverOrder.statusNo==0}">
                                            新建
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td class="">
                                    <a class="btn btn-primary" onclick="show(${handoverOrder.id})">查看</a>&nbsp;
                                    <c:if test="${handoverOrder.statusNo==1||handoverOrder.statusNo==0}">
                                        <a class="btn btn-primary"
                                           href="${pageContext.request.contextPath}/handover_order/to_edit?id=${handoverOrder.id}">修改</a>&nbsp;
                                        <a class="btn btn-primary"
                                           href="${pageContext.request.contextPath}/handover_order/remove?id=${handoverOrder.id}">删除</a>
                                    </c:if>
                                </td>
                            </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <c:if test="${isSearch!=1}">
                        <nav aria-label="Page navigation">
                            <div class="panel-footer text-right remove-padding-horizontal pager-footer">
                                <!-- Pager -->
                                <ul class="pagination">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/handover_order/list?pageNum=1"><span>首页</span></a>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/handover_order/list?pageNum=${page.pageNum-1}"><span>上一页</span></a>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/handover_order/list?pageNum=${page.pageNum+1}"><span>下一页</span></a>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/handover_order/list?pageNum=${page.pages}"><span>尾页</span></a>
                                    </li>
                                </ul>
                            </div>
                        </nav>
                    </c:if>
                    <c:if test="${isSearch==1}">
                        <nav aria-label="Page navigation">
                            <div class="panel-footer text-right remove-padding-horizontal pager-footer">
                                <!-- Pager -->
                                <ul class="pagination">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/handover_order/search?pageNum=1&assetNumbers=${assetNumbers}&transferTeam=${transferTeam}&acceptTeam=${acceptTeam}"><span>首页</span></a>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/handover_order/search?pageNum=${page.pageNum-1}&assetNumbers=${assetNumbers}&transferTeam=${transferTeam}&acceptTeam=${acceptTeam}"><span>上一页</span></a>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/handover_order/search?pageNum=${page.pageNum+1}&assetNumbers=${assetNumbers}&transferTeam=${transferTeam}&acceptTeam=${acceptTeam}"><span>下一页</span></a>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/handover_order/search?pageNum=${page.pages}&assetNumbers=${assetNumbers}&transferTeam=${transferTeam}&acceptTeam=${acceptTeam}"><span>尾页</span></a>
                                    </li>
                                </ul>
                            </div>
                        </nav>
                    </c:if>
                </div>

            </div>
            <!-- end: .admin-form -->
        </div>
        <!-- end: .tray-center -->
    </section>
    <!-- End: Content -->
</section>
<!-- End: Content-Wrapper -->

<jsp:include page="../footer.jsp"/>

<script>

    $(function () {
        $('#date1').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            locale: moment.locale('zh-cn')
        });
        $('#date2').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            locale: moment.locale('zh-cn')
        });
    });
    $("#excel").click(function () {
        var realName = $("#realName").val();
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        window.location.href = "${pageContext.request.contextPath}/dailySearch?realName=" + realName + "&startTime=" + startTime + "&endTime=" + endTime;
    });

    function show(id) {
        layer.open({
            type: 2,
            title: name,
            shadeClose: true,
            shade: false,
            maxmin: true, //开启最大化最小化按钮
            area: ['1000px', '650px'],
            content: '${pageContext.request.contextPath}/handover_order/find_one?id=' + id
        });
    }

    function showAsset(id) {
        layer.open({
            type: 2,
            title: name,
            shadeClose: true,
            shade: false,
            maxmin: true, //开启最大化最小化按钮
            area: ['1000px', '650px'],
            content: '${pageContext.request.contextPath}/asset/find_one?id=' + id
        });
    }

    function confirm(id) {
        layer.confirm('您确定要删除？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            location.href = "${pageContext.request.contextPath}/handover_order/remove?id=" + id
        }, function () {
            parent.layer.closeAll();
        });
    }

    function submitCheck(id) {
        layer.confirm('您确定要提交审核？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            location.href = "${pageContext.request.contextPath}/handover_order/submit_check?id=" + id
            layer.msg("后台正努力发送邮件中...");
        }, function () {
            parent.layer.closeAll();
        });
    }
</script>