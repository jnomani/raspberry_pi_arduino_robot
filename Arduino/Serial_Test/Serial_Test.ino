//Test kit

int light = 12;
int FLF = 2;
int FLB = 8;
int FRF = 3;
int FRB = 4;
int BLF = 6;
int BLB = 7;
int BRF = 9;
int BRB = 10;

void setup(){
  Serial.begin(9600);
  Serial.println("Light Test");
  for(int i = 0; i < 13; i++){
    pinMode(i, OUTPUT);
  }
}

void leftForward(boolean on){
 if(on){
   digitalWrite(FLF, HIGH);
   digitalWrite(FLB, HIGH);
 }else{
    digitalWrite(FLF, LOW);
    digitalWrite(FLB, LOW);
 }
}

void rightForward(boolean on){
  if(on){
   digitalWrite(FRF, HIGH);
   digitalWrite(FRB, HIGH);
 }else{
    digitalWrite(FRF, LOW);
    digitalWrite(FRB, LOW);
 }
}

void lights(boolean on){
  if(on){
   digitalWrite(light, HIGH);
 }else{
    digitalWrite(light, LOW);
 }
}

void leftBack(boolean on){
  if(on){
   digitalWrite(BLF, HIGH);
   digitalWrite(BLB, HIGH);
 }else{
    digitalWrite(BLF, LOW);
    digitalWrite(BLB, LOW);
 }
}

void rightBack(boolean on){
  if(on){
   digitalWrite(BRF, HIGH);
   digitalWrite(BRB, HIGH);
 }else{
    digitalWrite(BRF, LOW);
    digitalWrite(BRB, LOW);
 }
}


void loop(){
  char message = Serial.read();
  
  if(message == 'L'){
   leftForward(true);
  }else if(message == 'R'){
   rightForward(true); 
  }else if(message == 'l'){
   leftForward(false); 
  }else if(message == 'r'){
   rightForward(false); 
  }else if(message == 'H'){
    lights(true);
  }else if(message == 'h'){
   lights(false);
  }else if(message == 'D'){
   rightBack(true);
  }else if(message == 'd'){
   rightBack(false); 
  }else if(message == 'T'){
   leftBack(true); 
  }else if(message == 't'){
   leftBack(false); 
  }else{
   //Moo! I'm a cow 
  }
  
}






