<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.4" tiledversion="1.4.3" name="coldwaterdeepwater" tilewidth="32" tileheight="32" tilecount="30" columns="5">
 <image source="coldwaterdeepwater.png" width="160" height="192"/>
 <terraintypes>
  <terrain name="Nowy Teren" tile="2"/>
  <terrain name="Nowy Teren" tile="22"/>
 </terraintypes>
 <tile id="0">
  <animation>
   <frame tileid="0" duration="500"/>
   <frame tileid="5" duration="500"/>
   <frame tileid="0" duration="500"/>
  </animation>
 </tile>
 <tile id="1" terrain="0,0,0,1"/>
 <tile id="2" terrain="0,0,1,0"/>
 <tile id="5">
  <animation>
   <frame tileid="5" duration="500"/>
   <frame tileid="0" duration="500"/>
   <frame tileid="5" duration="500"/>
  </animation>
 </tile>
 <tile id="6" terrain="0,1,0,0"/>
 <tile id="7" terrain="1,0,0,0"/>
 <tile id="10" terrain="1,1,1,0"/>
 <tile id="11" terrain="1,1,0,0"/>
 <tile id="12" terrain="1,1,0,1"/>
 <tile id="15" terrain="1,0,1,0"/>
 <tile id="16" terrain="0,0,0,0"/>
 <tile id="17" terrain="0,1,0,1"/>
 <tile id="20" terrain="1,0,1,1"/>
 <tile id="21" terrain="0,0,1,1"/>
 <tile id="22" terrain="0,1,1,1"/>
 <tile id="25">
  <animation>
   <frame tileid="25" duration="500"/>
   <frame tileid="27" duration="500"/>
   <frame tileid="26" duration="500"/>
  </animation>
 </tile>
 <tile id="26">
  <animation>
   <frame tileid="26" duration="500"/>
   <frame tileid="27" duration="500"/>
   <frame tileid="26" duration="500"/>
   <frame tileid="25" duration="500"/>
   <frame tileid="26" duration="500"/>
  </animation>
 </tile>
 <tile id="27">
  <animation>
   <frame tileid="27" duration="500"/>
   <frame tileid="25" duration="500"/>
   <frame tileid="27" duration="500"/>
   <frame tileid="26" duration="500"/>
  </animation>
 </tile>
</tileset>
