#include <SPI.h>
#include <Phpoc.h>

#include <WaveHC.h>
#include <WaveUtil.h>
#include <SdReader.h>
#include <EEPROM.h>

#define error(msg) error_P(PSTR(msg))

void error_P(const char *str);
void playcomplete(char *name);
void sdErrorCheck(void);
void playfile(char *name);

// put pi in flash memory

SdReader card;    // This object holds the information for the card
FatVolume vol;    // This holds the information for the partition on the card
FatReader root;   // This holds the information for the volumes root directory
FatReader file;   // This object represent the WAV file for a pi digit or period
WaveHC wave;      // This is the only wave (audio) object, since we will only play one at a time
/*
 * Define macro to put error messages in flash memory
 */


//////////////////////////////////// SETUP

void setup() {
  // set up Serial library at 9600 bps
  Serial.begin(9600);
  //pinMode(6, OUTPUT);
  pinMode(10, OUTPUT);
  digitalWrite(10, HIGH);
  //SS-LOW 선택
  //SS-HIGH 비선택
  /*PgmPrintln("Pi speaker");

  if (!card.init()) {
    error("Card init. failed!");
  }
  if (!vol.init(card)) {
    error("No partition!");
  }
  if (!root.openRoot(vol)) {
    error("Couldn't open dir");
  }

  PgmPrintln("Files found:");
  root.ls();*/
}

/////////////////////////////////// LOOP
int cnt = 0;
void loop() {
  
   digitalWrite(9, HIGH);
   digitalWrite(10, LOW);
  int state = phpoc.read();
  if (state) {
    int check = check = EEPROM.read(1) ;
    playcomplete("alarm.WAV");    
  }
}

/*
 * print error message and halt
 */
void error_P(const char *str) {
  PgmPrint("Error: ");
  SerialPrint_P(str);
  sdErrorCheck();
  while (1);
}
/*
 * print error message and halt if SD I/O error
 */
void sdErrorCheck(void) {
  if (!card.errorCode()) return;
  PgmPrint("\r\nSD I/O error: ");
  Serial.print(card.errorCode(), HEX);
  PgmPrint(", ");
  Serial.println(card.errorData(), HEX);
  while (1);
}
/*
 * Play a file and wait for it to complete
 */
void playcomplete(char *name) {
  playfile(name);
  while (wave.isplaying);

  // see if an error occurred while playing
  sdErrorCheck();
}
/*
 * Open and start playing a WAV file
 */
void playfile(char *name) {
  if (wave.isplaying) {// already playing something, so stop it!
    wave.stop(); // stop it
  }
  if (!file.open(root, name)) {
    PgmPrint("Couldn't open file ");
    Serial.print(name);
    return;
  }
  if (!wave.create(file)) {
    PgmPrintln("Not a valid WAV");
    return;
  }
  // ok time to play!
  wave.play();
}
