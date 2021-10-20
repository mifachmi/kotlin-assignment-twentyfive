<?php
require_once "connection.php";
require_once "config.php";
require_once "data.php";
require_once "firebase.php";

if (function_exists($_GET['function'])) {
    $_GET['function']();
}

function login_user()
{
    global $connect;
    if (!empty($_GET['email_user']) && !empty($_GET['password'])) {
        $my_email = $_GET['email_user'];
        $my_password = $_GET['password'];
    }

    $query = "SELECT * FROM users WHERE email_user = '$my_email' AND password = '$my_password'";
    $result = $connect->query($query);

    if ($result->num_rows > 0) {
        while ($row = mysqli_fetch_object($result)) {
            $data[] = $row;
        }
        $response = array(
            'status' => 1,
            'message' => 'get data succeed',
            'data' => $data
        );
    } else {
        $response = array(
            'status' => 0,
            'message' => 'no data found'
        );
    }

    header('Content-Type: application/json');
    echo json_encode($response);
}

function sendNotification() {
    $tableAtribute = array(
        'title' => '',
        'message' => ''
    );
    $dataFromUserCheck = count(array_intersect_key($_POST, $tableAtribute));

    if ($dataFromUserCheck == count($tableAtribute)) {
        
        $title = $_POST['title'];
        $message = $_POST['message'];

        $data = new Data();
        $firebase = new Firebase();

        $data -> setTitle($title);
        $data -> setMessage($message);

        $result = $firebase->send(FCM_TOKEN, $data->getPush());

        if ($result) {
            $response = array(
                'status' => 1,
                'message' => 'push notif succeed'
            );
        } else {
            $response = array(
                'status' => 0,
                'message' => 'push notif failed'
            );
        }
    } else {
        $response = array(
            'status' => 0,
            'message' => 'wrong parameter'
        );
    }

    echo json_encode($response);
}

function get_all_users()
{
    global $connect;
    $query = $connect->query("SELECT * FROM users");
    while ($row = mysqli_fetch_object($query)) {
        $data[] = $row;
    }

    $response = array(
        'status' => 1,
        'message' => 'get data succeed',
        'data' => $data
    );

    header('Content-Type: application/json');
    echo json_encode($response);
}

function get_user_with_id()
{
    global $connect;
    if (!empty($_GET['id'])) {
        $id = $_GET['id'];
    }

    $query = "SELECT * FROM users WHERE id = $id";
    $result = $connect->query($query);
    while ($row = mysqli_fetch_object($result)) {
        $data[] = $row;
    }

    if ($data) {
        $response = array(
            'status' => 1,
            'message' => 'get data succeed',
            'data' => $data
        );
    } else {
        $response = array(
            'status' => 0,
            'message' => 'no data found'
        );
    }

    header('Content-Type: application/json');
    echo json_encode($response);
}

function get_user_by_device_id() {
    global $connect;
    if (!empty($_GET['device_id'])) {
        $device_id = $_GET['device_id'];
    }

    $query = "SELECT * FROM users WHERE device_id = '$device_id'";
    $result = $connect->query($query);
    while ($row = mysqli_fetch_object($result)) {
        $data[] = $row;
    }

    if ($data) {
        $response = array(
            'status' => 1,
            'message' => 'get data succeed',
            'data' => $data
        );
    } else {
        $response = array(
            'status' => 0,
            'message' => 'no data found'
        );
    }

    header('Content-Type: application/json');
    echo json_encode($response);
}

function check_device_id() {
    global $connect;
    if (!empty($_GET['device_id'])) {
        $deviceId = $_GET['device_id'];
    }

    $query = "SELECT device_id FROM users WHERE device_id = '$deviceId'";
    $result = $connect->query($query);
    while ($row = mysqli_fetch_object($result)) {
        $data[] = $row;
    }

    if ($data) {
        $response = array(
            'status' => 1,
            'message' => 'get data succeed',
            'data' => $data
        );
    } else {
        $response = array(
            'status' => 0,
            'message' => 'no data found'
        );
    }

    header('Content-Type: application/json');
    echo json_encode($response);
}

function check_device_id_by_user_id()
{
    global $connect;
    if (!empty($_GET['id'])) {
        $id = $_GET['id'];
    }

    $query = "SELECT device_id FROM users WHERE id = '$id'";
    $result = $connect->query($query);
    while ($row = mysqli_fetch_object($result)) {
        $data[] = $row;
    }

    if ($data) {
        $response = array(
            'status' => 1,
            'message' => 'get data succeed',
            'data' => $data
        );
    } else {
        $response = array(
            'status' => 0,
            'message' => 'no data found'
        );
    }

    header('Content-Type: application/json');
    echo json_encode($response);
}

function add_new_user()
{
    global $connect;
    $tableAtribute = array(
        'id' => '',
        'name_user' => '',
        'email_user' => '',
        'password' => '',
        'photo_user' => ''
    );
    $dataFromUserCheck = count(array_intersect_key($_POST, $tableAtribute));

    if ($dataFromUserCheck == count($tableAtribute)) {
        $query = "INSERT INTO users SET 
                  id = '$_POST[id]',
                  name_user = '$_POST[name_user]',
                  email_user = '$_POST[email_user]',
                  password = '$_POST[password]',
                  photo_user = '$_POST[photo_user]'";
        $result = mysqli_query($connect, $query);

        if ($result) {
            $response = array(
                'status' => 1,
                'message' => 'insert data succeed'
            );
        } else {
            $response = array(
                'status' => 0,
                'message' => 'insert data failed'
            );
        }
    } else {
        $response = array(
            'status' => 0,
            'message' => 'wrong parameter'
        );
    }

    header('Content-Type: application/json');
    echo json_encode($response);
}

function update_user()
{
    global $connect;

    if (!empty($_GET['id'])) {
        $id = $_GET['id'];
    }

    $tableAtribute = array(
        'name_user' => '',
        'email_user' => '',
        'password' => '',
        'photo_user' => '',
        'device_id' =>''
    );
    $dataFromUserCheck = count(array_intersect_key($_POST, $tableAtribute));

    if ($dataFromUserCheck == count($tableAtribute)) {
        $query = "UPDATE users SET
                  name_user = '$_POST[name_user]',
                  email_user = '$_POST[email_user]',
                  password = '$_POST[password]',
                  photo_user = '$_POST[photo_user]',
                  device_id = '$_POST[device_id]' WHERE id = $id";
        $result = mysqli_query($connect, $query);

        if ($result) {
            $response = array(
                'status' => 1,
                'message' => 'update data succeed'
            );
        } else {
            $response = array(
                'status' => 0,
                'message' => 'update data failed'
            );
        }
    } else {
        $response = array(
            'status' => 0,
            'message' => 'wrong parameter',
            'data' => $id
        );
    }

    header('Content-Type: application/json');
    echo json_encode($response);
}

function delete_user()
{
    global $connect;
    $id = $_GET['id'];
    $query = "DELETE FROM users WHERE id = $id";

    if (mysqli_query($connect, $query)) {
        $response = array(
            'status' => 1,
            'message' => 'delete data with id = ' . $id . ' succeed'
        );
    } else {
        $response = array(
            'status' => 0,
            'message' => 'delete data with id = ' .$id. ' failed'
        );
    }

    header('Content-Type: application/json');
    echo json_encode($response);
}

function upload_image()
{
    $image = $_FILES['file']['tmp_name'];
    $image_name = str_replace(' ', '_', $_FILES['file']['name']);
    $image_size = $_FILES['file']['size'];
    $file_path = $_SERVER['DOCUMENT_ROOT'] . '/kotlin-assignment-twenty-backend/uploaded_image/';

    if(empty($image_name)) {
        $response = array(
            'status' => 0,
            'message' => "Upload Gambar Dulu"
        );
    } else {
        $fileExt = strtolower(pathinfo($image_name, PATHINFO_EXTENSION));
        $validExt = array('jpeg', 'jpg', 'png', 'webp');

        if(in_array($fileExt, $validExt)) {
            if (!file_exists($file_path)) {
                mkdir($file_path, 0777, true);
            }
            if(!file_exists($file_path . $image_name)) {
                if ($image_size < 5000000) {
                    move_uploaded_file($image, $file_path . '/' . $image_name);
                    $response = array(
                        'status' => 1,
                        'message' => "Succeed Upload Image",
                        'file path' => $image_name
                    );
                } else {
                    $response = array(
                        'status' => 0,
                        'message' => "Failed Upload (max 5 MB)",
                        'file path' => $image_name
                    );
                }
            } else {
                $response = array(
                    'status' => 0,
                    'message' => "Failed Upload (file already exist)",
                    'file path' => $image_name
                );
            }
        } else {
            $response = array(
                'status' => 0,
                'message' => "Failed Upload (invalid extension)",
                'file path' => $image_name
            );
        }

    }

    header('Content-Type: application/json');
    echo json_encode($response);
}