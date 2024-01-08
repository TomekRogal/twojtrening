<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form"
           uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<jsp:include page="/wid/head.jsp"/>
<body class="bg-primary">
<div id="layoutAuthentication">
    <div id="layoutAuthentication_content">
        <main>
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-7">
                        <div class="card shadow-lg border-0 rounded-lg mt-5">
                            <div class="card-header"><h3 class="text-center font-weight-light my-4">Stwórz konto</h3>
                            </div>
                            <div class="card-body">
                                <form:form method="post"
                                           modelAttribute="user">
                                    <form:hidden path="id"/>
                                    <div class="form-floating mb-3">
                                        <form:input cssClass="form-control" id="userName" placeholder="name@example.com"
                                                    path="username"/>
                                        <label for="userName">Nazwa użytkownika</label>
                                        <form:errors cssStyle="color: red" path="username"/>
                                        <c:if test='${register.equals("failed")}'>
                                            <p style="color:red"> Użytkownik o podanej nazwie już istnieje</p>
                                        </c:if>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <div class="form-floating mb-3 mb-md-0">

                                                <form:input cssClass="form-control" id="inputPassword"
                                                            placeholder="Hasło" type="password" path="password"/>
                                                <label for="inputPassword">Hasło</label>
                                                <form:errors cssStyle="color: red" path="password"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-floating mb-3 mb-md-0">
                                                <input class="form-control" id="inputPasswordConfirm" name="confirm"
                                                       type="password" placeholder="Powtórz hasło"/>
                                                <label for="inputPasswordConfirm">Powtórz hasło</label>
                                                <c:if test='${pass.equals("failed")}'>
                                                    <p style="color:red"> Hasła muszą być takie same</p>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="mt-4 mb-0">
                                        <div class="d-grid"><input class="btn btn-primary btn-block" type="submit"
                                                                   value="Stwórz konto"></div>
                                    </div>
                                </form:form>
                            </div>
                            <div class="card-footer text-center py-3">
                                <div class="small"><a href="/login">Masz już konto? Zaloguj się</a></div>
                                <div class="small"><a href="/">Strona główna</a></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <div id="layoutAuthentication_footer">
        <jsp:include page="/wid/footer.jsp"/>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
<script src="/js/scripts.js"></script>
</body>
</html>

