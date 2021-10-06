<?php
require_once "connection.php";

if(function_exists($_GET['function'])) {
    $_GET['function']();
}

function get_all_task() {
    global $connect;
    $query = $connect->query("SELECT * FROM tasks ORDER BY task_date ASC");
    while($row = mysqli_fetch_object($query)) {
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

function get_task_id() {
    global $connect;
    if (!empty($_GET['id'])) {
        $id = $_GET['id'];
    }

    $query = "SELECT * FROM tasks WHERE id = $id";
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

function add_new_task() {
    global $connect;
    $tableAtribute = array(
        'id' => '',
        'task_name' => '',
        'task_date' => '',
        'is_done' => ''
    );
    $dataFromUserCheck = count(array_intersect_key($_POST, $tableAtribute));

    if ($dataFromUserCheck == count($tableAtribute)) {
        $query = "INSERT INTO tasks SET 
                  id = '$_POST[id]',
                  task_name = '$_POST[task_name]',
                  task_date = '$_POST[task_date]',
                  is_done = '$_POST[is_done]'";
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

function update_task() {
    global $connect;

    if (!empty($_GET['id'])) {
        $id = $_GET['id'];
    }

    $tableAtribute = array(
        'task_name' => '',
        'task_date' => '',
        'is_done' => ''
    );
    $dataFromUserCheck = count(array_intersect_key($_POST, $tableAtribute));

    if ($dataFromUserCheck == count($tableAtribute)) {
        $query = "UPDATE tasks SET
                  task_name = '$_POST[task_name]',
                  task_date = '$_POST[task_date]',
                  is_done = '$_POST[is_done]' WHERE id = $id";
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

function delete_task() {
    global $connect;
    $id = $_GET['id'];
    $query = "DELETE FROM tasks WHERE id = $id";

    if (mysqli_query($connect, $query)) {
        $response = array(
            'status' => 1,
            'message' => 'delete data with id = $id succeed'
        );
    } else {
        $response = array(
            'status' => 0,
            'message' => 'delete data with id = $id failed'
        );
    }

    header('Content-Type: application/json');
    echo json_encode($response);
}
