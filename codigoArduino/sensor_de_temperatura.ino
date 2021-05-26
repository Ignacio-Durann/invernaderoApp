#include <Arduino.h>
#include <DHTesp.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include<Servo.h>
DHTesp Temp;
int T,H;
ESP8266WiFiMulti WiFiMulti;
String urlog        = "http://pw4.kyared.com/S17030167/proyectoweb/login.php?user=admin&password=123";
String urlsensorte  = "http://pw4.kyared.com/S17030167/proyectoweb/sensorte.php";
String urlsensorhu  = "http://pw4.kyared.com/S17030167/proyectoweb/sensorhu.php";
String urlsensorap  = "http://pw4.kyared.com/S17030167/proyectoweb/sensorap.php";
String urlservo     = "http://pw4.kyared.com/S17030167/proyectoweb/servo.php";
String login, token;
float valorp,valort,valora,valorh;

Servo s;
#define PIN 4

const int TRIG = 0;
const int ECHO = 5;

int tiempo;
int distancia;

void setup() {
pinMode (TRIG, OUTPUT);
  pinMode (ECHO, INPUT);
  Serial.begin(115200);
  digitalWrite(TRIG,LOW);
  
  s.attach(2);
 
  Temp.setup(4, DHTesp::DHT11);
  for (uint8_t t = 4; t > 0; t--) {
  ///  Serial.printf("[SETUP] WAIT %d...\n", t);
    Serial.flush();
   // acabo de comentar delay(1000);
  }

  WiFi.mode(WIFI_STA);
  WiFiMulti.addAP("conexionHome", "A1b2c3d4e5");

  pinMode(PIN,INPUT);
}

void loop() {
   if ((WiFiMulti.run() == WL_CONNECTED)) {
    inicioSesion();
    if(login == "s"){
    
    getp();
    gett();
    geth();
    geta();
    postt();
    posth();
    posta();
    
  }
  delay(5000);
}
}
void inicioSesion(){
    WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] GET begin log...\n");
    if (http.begin(client, urlog)) {  // HTTP
      Serial.print("[HTTP] GET...\n");
      int httpCode = http.GET();

      if (httpCode > 0) {
       Serial.printf("[HTTP] GET... code: %d\n", httpCode);

        if (httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY) {
          String payload = http.getString();
          Serial.println(payload);
          int inicio, fin;
          inicio = payload.indexOf("login");
          fin = payload.indexOf(",");
          login = payload.substring(inicio + 8, fin - 1);
          Serial.println("login: " + login);

          inicio = payload.indexOf("token");
          fin = payload.indexOf("}");
          token = payload.substring(inicio + 8, fin - 1);
          Serial.println("token: " + token);
        }
      } else {
        Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
      }

      http.end();
    } else {
      Serial.printf("[HTTP} Unable to connect\n");
    }
}

void postt(){
      WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] begin...post temp\n");
    http.begin(client, urlsensorte);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addHeader("Authorization", token);

   float t= Temp.getTemperature();
Serial.print("valor t get" );
Serial.println(valort );
Serial.print("valor te" );
Serial.println(t );
   if(valort != t){
    int httpCode = http.POST("valor=" + (String)t);
    if (httpCode > 0) {
      Serial.printf("[HTTP] POST... code: %d\n", httpCode);
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
       Serial.println("received payload:\n<<");
       Serial.println(payload);
        Serial.println(">>t");
      }
    } else {
      Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
   }
}
void posth(){
     WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] begin...post hum\n");
    http.begin(client, urlsensorhu);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addHeader("Authorization", token);

    Serial.print("[HTTP] POST...\n");
float H=Temp.getHumidity();
Serial.print("valor h get" );
Serial.println(valorh );
Serial.print("valor hu " );
Serial.println(H );
 if(valorh != H){
  int httpCode = http.POST("valor=" + (String)H);
    if (httpCode > 0) {
      Serial.printf("[HTTP] POST... code: %d\n", httpCode);
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
        Serial.println("received payload:\n<<");
        Serial.println(payload);
        Serial.println(">>");
      }
    } else {
      Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
 }
}

void posta(){
     WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] begin...post ap\n");
    http.begin(client, urlsensorap);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addHeader("Authorization", token);

    Serial.print("[HTTP] POST...\n");

   digitalWrite(TRIG,HIGH);
  delayMicroseconds (10);
    digitalWrite(TRIG,LOW);
 tiempo =pulseIn (ECHO, HIGH);
  distancia = tiempo/58.4;
  Serial.print("valor a get" );
Serial.println(valora );
Serial.print("valor ap " );
Serial.println(distancia );
 if(valora != distancia){
  int httpCode = http.POST("tipo=D&valor=" + (String)distancia);
    if (httpCode > 0) {
      Serial.printf("[HTTP] POST... code: %d\n", httpCode);
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
        Serial.println("received payload:\n<<");
        Serial.println(payload);
        Serial.println(">>");
      }
    } else {
      Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
 }
}


/*---------------------------------------------------*/
void gett(){

    WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] begin...get temp\n");
    http.begin(client, urlsensorte);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addHeader("Authorization", token);

    Serial.print("[HTTP] GET...\n");
 int httpCode = http.GET();
    if (httpCode > 0) {
      Serial.printf("[HTTP] GET... code: %d\n", httpCode);
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
        Serial.println("received payload:\n<<");
        Serial.println(payload);
        Serial.println(">> t");
         int inicio, fin;
         inicio = payload.indexOf("valor");
          fin = payload.indexOf("}");
      valort =(payload.substring(inicio + 8, fin - 1)).toFloat();
       
       
      }
    } else {
      Serial.printf("[HTTP] Get... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
}
/*---------------------------------------------------*/
void geta(){

    WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] begin...get ap\n");
    http.begin(client, urlsensorap);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addHeader("Authorization", token);

    Serial.print("[HTTP] GET...\n");
 int httpCode = http.GET();
    if (httpCode > 0) {
      Serial.printf("[HTTP] GET... code: %d\n", httpCode);
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
        Serial.println("received payload:\n<<");
        Serial.println(payload);
        Serial.println(">> a");
         int inicio, fin;
         inicio = payload.indexOf("valor");
          fin = payload.indexOf("}");
      valora =  (payload.substring(inicio + 8, fin - 1)).toInt();
       
       
      }
    } else {
      Serial.printf("[HTTP] Get... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
}
/*---------------------------------------------------*/
void geth(){

    WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] begin...get hum\n");
    http.begin(client,urlsensorhu);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addHeader("Authorization", token);

    Serial.print("[HTTP] GET...\n");
 int httpCode = http.GET();
    if (httpCode > 0) {
      Serial.printf("[HTTP] GET... code: %d\n", httpCode);
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
        Serial.println("received payload:\n<<");
        Serial.println(payload);
        Serial.println(">> h");
         int inicio, fin;
         inicio = payload.indexOf("valor");
          fin = payload.indexOf("}");
      valorh = (payload.substring(inicio + 8, fin - 1)).toFloat();
        Serial.print(valorh);
       
      }
    } else {
      Serial.printf("[HTTP] Get... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
}
/*-------------------------------------------------*/
void getp(){

    WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] begin..get .puerta\n");
    http.begin(client, urlservo);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addHeader("Authorization", token);

    Serial.print("[HTTP] GET...puerta\n");
 int httpCode = http.GET();
    if (httpCode > 0) {
      Serial.printf("[HTTP] GET... code: %d\n", httpCode);
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
        Serial.println("received payload:\n<<");
        Serial.println(payload);
        Serial.println(">>p");
         int inicio, fin;
         inicio = payload.indexOf("valor");
          fin = payload.indexOf("}");
      valorp =( payload.substring(inicio + 8, fin - 1)).toInt();
     
       if(valorp == 1){
        
        s.write(90);
       }else{
         s.write(180);
       }
       
      }
    } else {
      Serial.printf("[HTTP] Get... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
}
