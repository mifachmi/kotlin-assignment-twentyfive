<?php

$hostname = "localhost";
$database = "todolist_db";
$username = "root";
$password = "";

$connect = mysqli_connect($hostname, $username, $password, $database);

if (!$connect) {
    die("Connection Failed : " . mysqli_connect_error());
}
