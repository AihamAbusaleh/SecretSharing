

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload File to Google Drive</title>
 
<link type="text/css" rel="stylesheet"  href="JS-CSS/style.css" />
 
</head>
<body>

   
	
	<script src="JS-CSS/uploadFile.js"></script>
	
	
	  
        <div class="container">
  
		 <img align="middle" style="border-radius: 50%;  max-width: 300px; 
    max-height: 300px " src="http://www.pcprofessionale.it/wp-content/uploads/2016/10/dropbox.png" />
		
            <div class="upload_form_cont">
                <form id="upload_form" enctype="multipart/form-data" method="post" action="http://localhost:8016/SecretSharing/webapi/myresourceDropbox/uploadToDropbox">
                    <div>
                         
                        <div>Select File : <input type="file" name="file" id="image_file" onchange="fileSelected();" multiple /></div>
                    </div>
                    <div>
                        <input type="submit" value="Upload"    onclick="startUploading()"  /><br>
              <!--        <input type="submit" value="UploadSub"   onclick="startUploading()"  />--> 
                        
                    </div>
                    <div id="fileinfo">
                        <div id="filename"></div>
                         <div id="TEST"></div>
                        <div id="filesize"></div>
                      
                       
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





