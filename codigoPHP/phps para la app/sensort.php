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


$metodo = $_SERVER["REQUEST_METHOD"];

switch($metodo){
    case "GET":
        if(isset($_GET['id'])){
            $c = conexion();
            $s = $c->prepare("SELECT * FROM sensort");
            $s->bindValue(":id", $_GET['id']);
            $s->execute();
            $s->setFetchMode(PDO::FETCH_ASSOC);
            $r = $s->fetch();
        }else{
            $c = conexion();
            $s = $c->prepare("SELECT * FROM sensort");
            $s->execute();
            $s->setFetchMode(PDO::FETCH_ASSOC);
            $r = $s->fetchAll();
        }
        echo json_encode($r);
        break;
    case "POST":
        if( isset($_POST['valor'])){
            $c = conexion();
            $s = $c->prepare("INSERT INTO sensort(user,valor,fecha) VALUES(:u,:v,:f)");
            $s->bindValue(":u", $data['user']);
            $s->bindValue(":v", $_POST['valor']);
            $s->bindValue(":f", date("Y-m-d H:i:s"));
            $s->execute();
            if($s->rowCount()){
                $id = $c->lastInsertId();
                $r = array("add"=>"y", "id"=>$id);
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