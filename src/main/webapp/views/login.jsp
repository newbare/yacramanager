<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Sign in &middot; Yacra Manager</title>
<link href="assets/css/bootstrap.min.css" rel="stylesheet">
<link href="assets/css/signin.css" rel="stylesheet">
</head>
<body>
	
	
	 <div class="container">
		<c:if test="${error==true}">
			<div class="alert alert-danger alert-dismissable">
				<a class="close" data-dismiss="alert" href="#" aria-hidden="true">&times;</a>
				<c:if test="${not empty errorMessage}">
	  				${errorMessage}	
	  			</c:if>
			</div>
		</c:if>
      <form class="form-signin" role="form" method="post" action="login">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="text" class="form-control" placeholder="Username" name="username" required autofocus>
        <input type="password" class="form-control" placeholder="Password" name="password" required>
        <div class="checkbox">
          <label>
            <input type="checkbox" name="remember-me"> Remember me
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit"><i class="ace-icon fa fa-key"></i><span class="bigger-110">Login</span></button>
      </form>

    </div> <!-- /container -->
	
	<%-- <div class="container">
		<c:if test="${error==true}">
			<div class="alert alert-danger alert-dismissable">
				<a class="close" data-dismiss="alert" href="#" aria-hidden="true">&times;</a>
				<c:if test="${not empty errorMessage}">
	  				${errorMessage}	
	  			</c:if>
			</div>
		</c:if>
		<form class="form-signin" method="post" action="login">
			<fieldset>
				<div class="input-group">
					<span class="input-group-addon"> <i
						class="input-icon icon-user"></i>
					</span> <input type="text" class="form-control" placeholder="Username"
						name="username" />

				</div>
				<div class="input-group">
					<span class="input-group-addon"> <i class="icon-lock"></i>
					</span> <input type="password" class="form-control" placeholder="Password"
						name="password" />
				</div>
				<div class="">
					<label class="inline"> <input type="checkbox" class="ace"
						name="remember-me" /><span class="lbl"> Remember Me</span>
					</label>
					<button type="submit"
						class="width-35 pull-right btn btn-sm btn-primary">

						<i class="ace-icon fa fa-key"></i> <span class="bigger-110">Login</span>
					</button>
				</div>

				<div class="space-4"></div>

			</fieldset>
		</form>
	</div>
	<!-- /container --> --%>

	<script src="assets/js/bootstrap.min.js"></script>
	<script src="assets/js/jquery.js"></script>

</body>
</html>
