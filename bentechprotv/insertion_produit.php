<?php
$db = "bentechprotvdb";
$des = $_POST["des"];
$host = "localhost";
$conn = mysqli_connect($host, "root","",$db);
if ($conn)
{
	$q= "insert into produits (designation) values ('$des')";
	if (mysqli_query($conn, $q)) {
		echo "succes insertion";
		
	} else {
		echo "echec insertion";
	}
	mysqli_close($conn);
	
} else{
	echo "probleme de connexion";
}

?>