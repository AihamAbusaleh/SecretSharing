

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Split File </title>
 
<link type="text/css" rel="stylesheet"  href="JS-CSS/style.css" />
 
</head>
<body>
 
 
 	<br>
	
	<script src="JS-CSS/uploadFile.js"></script>
	<h1 id="pageName">File Splitter</h1>
	<div id="welcome"> Please select a File to be split into max slices <br> 
	The min input define how many slices are required in order to reconstruct the original File</div>
	  
        <div class="container">
  
			
            <div class="upload_form_cont">
                <form    id="upload_form" enctype="multipart/form-data" method="post" action="http://localhost:8016/SecretSharing/webapi/myresourceSplit/split">
                    <div>
                         <div>File : <input type="file" name="file" id="image_file" onchange="fileSelected();"  required /></div>
                         <div>Max  : <input type="text" name="max" id="max" onchange="MaximumNValidate();" required /></div>
                          <div>Min : <input type="text" name="min" id="min" onchange="MinimumNValidate();"  required /></div>
                    </div>
                    <div>
                        <input type="submit" value="Split"   id="submit"   /><br>
              <!--        <input type="submit" value="UploadSub"   onclick="startUploading()"  />--> 
                        
                    </div>
                    <div id="fileinfo">
                        <div id="filename"></div>
                         <div id="TEST"></div>
                        <div id="filesize"></div>
                        <div id="filetype"></div>
                       
                    </div>
                    <div id="error">You should select valid image files in order to display it here !!</div>
                    <div id="error2">An error occurred while uploading the file</div>
                    <div id="abort">The upload has been canceled by the user or the browser dropped the connection</div>
                    <div id="warnsize">Your file is very big. We can't accept it. Please select more small file</div>

                    <div id="progress_info">
                        <div id="progress"></div>
                        <div id="progress_percent">&nbsp;</div>
                        <div class="clear_both"></div>
                        <div>
                            <div id="speed">&nbsp;</div>
                            <div id="remaining">&nbsp;</div>
                            <div id="b_transfered">&nbsp;</div>
                            <div class="clear_both"></div>
                        </div>
                        <div id="upload_response"></div>
                    </div>
                </form>

                <img id="preview" />
            </div>
        </div>
 

	 
	 


	</body>
	
</html>





