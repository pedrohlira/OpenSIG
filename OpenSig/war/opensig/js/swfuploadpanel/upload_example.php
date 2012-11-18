<?php
	if ( (!empty($_REQUEST["PHPSESSID"]) && !empty($_REQUEST["PHPSESSIDX"])) 
			&& $_REQUEST["PHPSESSID"] != $_REQUEST["PHPSESSIDX"] ) { 
			
			$_REQUEST["PHPSESSID"] = $_REQUEST["PHPSESSIDX"]; 
			unset($_REQUEST["PHPSESSIDX"]); 
			$_COOKIE["PHPSESSID"] = $_REQUEST["PHPSESSID"]; 
	}  

	session_start();
	
	$upload_good = false;
	if (!isset($_FILES["Filedata"])) {
		$upload_good = "Not recieved, probably exceeded POST_MAX_SIZE";
	}
	else if (!is_uploaded_file($_FILES["Filedata"]["tmp_name"])) {
		$upload_good = "Upload is not a file. PHP didn't like it.";
	} 
	else if ($_FILES["Filedata"]["error"] != 0) {
		$upload_good = "Upload error no. " + $_FILES["Filedata"]["error"];
	} else {
		$upload_good = "The upload was good";
	}
?>

{ success: true, data: [

	{cookies: [
<?
		foreach ($_COOKIE as $name => $value) {
			echo "{'" . htmlspecialchars($name) . "': '" . htmlspecialchars($value) . "'}, ";
		}
?>  
	]}
  
  ,	{get: [
<?
		foreach ($_GET as $name => $value) {
			echo "{'" . htmlspecialchars($name) . "': '" . htmlspecialchars($value) . "'}, ";
		}
?>  
	]}

  ,	{post: [
<?
		foreach ($_POST as $name => $value) {
			echo "{'" . htmlspecialchars($name) . "': '" . htmlspecialchars($value) . "'}, ";
		}
?>  
	]}

  ,	{session: [
<?
		foreach ($_SESSION as $name => $value) {
			echo "{'" . htmlspecialchars($name) . "': '" . htmlspecialchars($value) . "'}, ";
		}
?>  
	]}

  ,	{files: [
<?

		foreach ($_FILES as $name => $value) {
			echo "{" . $name . ": " . json_encode($value) . "}";
		}
?>  
	]}

]}