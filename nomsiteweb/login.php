<?php
$db = "glsic_base_distante";
if (isset($_POST['user'])) {
  
   $user = $_POST["user"];
}
if (isset($_POST['pass'])) {
   //do something
   $pass = $_POST["pass"];
}


$host = "localhost";
$conn = mysqli_connect($host, "root","",$db);
if ($conn)
{
$q= "select * from user_info where login like '$user' and password like '$pass'";
$result = mysqli_query($conn, $q);
if (mysqli_num_rows($result) > 0 ) {
	echo "login correct";
} else
{
	echo "login incorrect...";
}
mysqli_close($conn);
}
else {
	echo "probleme de connexion";
}


?>