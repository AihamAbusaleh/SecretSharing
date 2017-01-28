

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload File to Google Drive</title>
 

</head>
<body>


	<br>
	<div class="fileUpload btn btn-primary">
		<form
			action="http://localhost:8016/googleDriveSecurity/webapi/myresource/upload"
			enctype="multipart/form-data" method="post">
 			 
			<p> Please choose a file : </p>
			<input type="file" name="file" ><br>
			<br>
			<span> <input type="submit" value="Upload"  > </span>	


		</form>
	</div>
	
	 <!-- 
	 
    <input type="file" id="file" name="myfiles[]" multiple />
 
	  -->
	  
	</body>
	
</html>





