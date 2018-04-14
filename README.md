<?php
if(isset($_POST)== true && empty($_POST)==false){
	$latitude=$_POST['Latitude'];
	$longitude=$_POST['Longitude'];
	$userid=$_POST['Userid'];
	$timestamp=$_POST['Timestamp'];
	
	$dbhost = 'friendfinder';
    $dbuser = 'root';
    $dbpass = 'root';
    $conn = mysqli_connect($dbhost, $dbuser, $dbpass);
	if(!$conn){
		die("connection failed" . mysqli_error());
	}
	$sql='INSERT into locationtable(Latitude,Longitude,Timestamp,Userid) VALUES ($latitude,$longitude,\"$timestamp\",$userid)';
	if(mysqli_query($conn,$sql)){
		$result=array("Result"=>1, "Message"=>"Inserted correctly");
		echo json_encode($result);
	}else{
		echo "Error: " . mysqli_error($conn);
	}
	mysqli_close();
}else{
	
	
}
?>