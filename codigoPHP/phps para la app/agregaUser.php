<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Authorization, Access-Control-Allow-Methods, Access-Control-Allow-Headers, Allow, Access-Control-Allow-Origin");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS, HEAD");
header("Allow: GET, POST, PUT, DELETE, OPTIONS, HEAD");
require_once "conexion.php";
require_once "jwt.php";
if($_SERVER["REQUEST_METHOD"]=="OPTIONS"){
    exit();
}
$header = apache_request_headers();

$metodo = $_SERVER["REQUEST_METHOD"];

switch($metodo){
    case "GET":
        if(isset($_GET['user'])){
            $c = conexion();
            $s = $c->prepare("SELECT * FROM users WHERE user =:user");
            $s-> bindValue(":user", $_GET['user']);
            $s->execute();
            $s->setFetchMode(PDO::FETCH_ASSOC);
            $r = $s->fetch();
            
        }else{
            $c = conexion();
            $s = $c->prepare("SELECT * FROM users");
            $s->execute();
            $s->setFetchMode(PDO::FETCH_ASSOC);
            $r = $s->fetchAll();
           
        }
        echo json_encode($r);
        break;
    case "POST":
        if( isset($_POST['user'])){
            $c = conexion();
            $s = $c->prepare("INSERT INTO users(user,password,tipo) VALUES(:u,sha1(:p),:t)");
            $s->bindValue(":u", $_POST['user']);
            $s->bindValue(":p", $_POST['password']);
            $s->bindValue(":t", $_POST['tipo']);
            $s->execute();
            if($s->rowCount()){
                $id = $c->lastInsertId();
                $r = array("add"=>"y", "id"=>$id);
                header("HTTP/1.1 200 OK");
            }else{
                $r = array("add"=>"n");
            }
            header("HTTP/1.1 200 OK");
            echo json_encode($r);
        }else{
            header("HTT/1.1 400 Bad Request");
        }
        break;
    case "PUT":
        break;
    case "DELETE":
        break;
    default:
        header("HTT/1.1 400 Bad Request");
}