
#include <LiquidCrystal.h>
#include "SoftwareSerial.h"

LiquidCrystal lcd(13, 12, 11, 10, 9, 8);
uint64_t counter = 0;
// float cm;
int start, finish;
float V;
float cm;  

SoftwareSerial BTSerial(4, 3); //RX, TX;
void setup () {
  Serial.begin(9600);
  pinMode(5, OUTPUT); // Trig
  pinMode(2, INPUT);  // Echo

  pinMode(7, OUTPUT);
  pinMode(6, OUTPUT);

  digitalWrite(7, 0);
  digitalWrite(6, 0);

  Serial.begin(9600);
  BTSerial.begin(115200); //в стандартном режиме
}
void loop (){
  // delay(500);
  digitalWrite(5, LOW);  // очистили сигнал, чтобы не было ошибок
  delayMicroseconds(2);
  digitalWrite(5, HIGH); // включили сонар (отправили ультразвуковой сигнал)
  delayMicroseconds(10); 
  digitalWrite(5, LOW);  // выключили сонар
  int duration = pulseIn(2, HIGH); // подсчет секунд до прихода отраженного сигнала
  // cm = duration / 58;
  // Serial.println(cm);
  // Serial.println("the first");
  display();
  controller();


  V = analogRead(A0)*0.0048828125;
  cm = 29*pow(V, -1.10);
  Serial.println(cm);

  bool flag = false;
  String request = "";
  while(BTSerial.available()){ // true, если есть что читать
  char t = (char)BTSerial.read();
    Serial.print(t);
	request.concat(t);
    delay(1);
	flag = true;
  }
  Serial.println(request);
  if(request == "1"){
	digitalWrite(7, 0);
  digitalWrite(6, 1);

  }
  if(request == "0"){
	digitalWrite(7, 1);
  digitalWrite(6, 0);

  }
    if(request == "2"){
	digitalWrite(7, 0);
  digitalWrite(6, 0);

  }
  
  if(flag){ //для удобства
	  Serial.println();
	  BTSerial.println("OK");
	  Serial.println("-----------------------------------------------------------");


  }
}

void display(){
  if (millis() - counter > 250){
      lcd.begin(8, 2);
      lcd.print(cm);
      counter = millis();
  }
}
bool flag1 = true;
void controller(){
  Serial.println(flag1);
  if (cm > 25 && flag1){
    	digitalWrite(7, 0);
      digitalWrite(6, 1);
      Serial.println("swfds");

  }
  else{
    flag1 = false;
  }
  if(true){
    	digitalWrite(7, 1);
      digitalWrite(6, 0);
      if(cm > 35){
        flag1 = true;
      }
  }
  
  
}
