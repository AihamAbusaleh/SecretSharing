# SecretSharing

## RESTful Web Services 

This project uses AES 256 , SHA 256 and IDA "Rabin's Information Dispersal" Algorithms. 
It uses also a powerful library to compute matricses in Finite Field.


The aim of this project is to split a file with IDA algorithm "in GF(1024)" into Slices after encrypt this file with AES and SHA algorithms.
Than upload the split slices to Google Drive and Dropbox.


**How to**

1- run the code on a server

2- call the file split.jsp

3- choose a file to split it into slices (the slices are encrypted)

4- upload the slices into google drive or dropbox

5- In order to reconstruct the original file, use the class RecombineFile.java.
	there is no RESTful method to download from the Cloud 
