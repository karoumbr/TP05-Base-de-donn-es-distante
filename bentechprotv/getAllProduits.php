<?php
$db = "bentechprotvdb";
$host = "localhost";
$conn = mysqli_connect($host, "root","",$db);
if ($conn)
{
	mysqli_set_charset($conn,"utf8");
	
	$q= "select * from produits";
	$result=mysqli_query($conn, $q);
	
	while($row = mysqli_fetch_assoc($result)){
		$output[] = $row;
		
	}
	
	echo json_encode($output);
	
	
		
	mysqli_close($conn);
	
} else{
	echo "probleme de connexion";
}



?>