<?php
$db="bentechprotvdb";
$user = $_POST["user"];
$pass = $_POST["pass"];
$host = "localhost";
$conn = mysqli_connect($host,"root","",$db);
if ($conn) {
	$q = "select * from users where login like '$user' and password like '$pass'";
	$resultat = mysqli_query($conn,$q);
	if (mysqli_num_rows($resultat)> 0){
		echo "succes";
	} else {
		echo "echec";
	}mysqli_close($conn);
} else{
	echo "probleme de connexion";
}
?>