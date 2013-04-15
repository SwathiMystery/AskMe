<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
     <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Sign in &middot; AskMe</title>
     <meta name="description" content="">
    <meta name="author" content="">
    <link href="../bootstrap/css/bootstrap.css" rel="stylesheet">
	
 
   <script src="../bootstrap/js/jquery-latest.js"></script>
     <script src="../bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
      <script src="../bootstrap/js/jquery.min.js"></script>
	     
      <style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }

      .form-signin {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }
       .error {color: red}

    </style>
    <link href="../bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
</head>
<body>
     <div class="container">
    
      <form class="form-signin" method="post">
        <h2 class="form-signin-heading">Login</h2>  Need to Create an account? <a href="/signup">Signup</a><p>
        <input type="text" name="username" class="input-block-level" placeholder="Username" value="${username}" class="error">
        <input type="password" name="password" class="input-block-level" placeholder="Password" value="" class="error">
        <p class="error">
	    ${login_error} 
        </td>
        </p>
        <button class="btn btn-large btn-primary" type="submit">Login </button>    
      </form>
  </div>
 </body>
</html>