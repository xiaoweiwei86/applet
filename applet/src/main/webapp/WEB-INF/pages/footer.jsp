<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    /* demo page styles */
    body { min-height: 2300px; }

    .content-header b,
    .admin-form .panel.heading-border:before,
    .admin-form .panel .heading-border:before {
        transition: all 0.7s ease;
    }
    /* responsive demo styles */
    @media (max-width: 800px) {
        .admin-form .panel-body { padding: 18px 12px; }
    }
</style>

<style>
    .ui-datepicker select.ui-datepicker-month,
    .ui-datepicker select.ui-datepicker-year {
        width: 48%;
        margin-top: 0;
        margin-bottom: 0;

        line-height: 25px;
        text-indent: 3px;

        color: #888;
        border-color: #DDD;
        background-color: #FDFDFD;

        -webkit-appearance: none; /*Optionally disable dropdown arrow*/
    }
</style>

<!-- jQuery -->
<script src="${pageContext.request.contextPath }/vendor/jquery/jquery-3.4.1.min.js"></script>
<script src="${pageContext.request.contextPath }/vendor/jquery/jquery_ui/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath }/vendor/jquery/jQuery.print.js"></script>
<!-- jQuery Validate Plugin-->
<script src="${pageContext.request.contextPath }/assets/admin-tools/admin-forms/js/jquery.validate.min.js"></script>

<!--layer js-->
<script src="${pageContext.request.contextPath }/layer/layer.js"></script>
<!-- jQuery Validate Addon -->
<script src="${pageContext.request.contextPath }/assets/admin-tools/admin-forms/js/additional-methods.min.js"></script>

<!-- AdminForms JS -->
<script src="${pageContext.request.contextPath }/assets/admin-tools/admin-forms/js/jquery-ui-datepicker.min.js"></script>

<!-- datetimepicker JS -->
<script src="${pageContext.request.contextPath }/assets/js/moment-with-locales.js"></script>
<script src="${pageContext.request.contextPath }/assets/js/bootstrap-datetimepicker.min.js"></script>
<!-- Theme Javascript -->
<script src="${pageContext.request.contextPath }/assets/js/utility/utility.js"></script>
<script src="${pageContext.request.contextPath }/assets/js/demo/demo.js"></script>
<script src="${pageContext.request.contextPath }/assets/js/main.js"></script>

<!-- END: PAGE SCRIPTS -->
<script type="text/javascript" src="${pageContext.request.contextPath }/js/pages.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/cal.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/print.js"></script>

</body>

</html>
