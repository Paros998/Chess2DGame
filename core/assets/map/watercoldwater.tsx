<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.4" tiledversion="1.4.3" name="watercoldwater" tilewidth="32" tileheight="32" tilecount="30" columns="5">
 <image source="watercoldwater.png" width="160" height="192"/>
 <terraintypes>
  <terrain name="Nowy Teren" tile="16"/>
 </terraintypes>
 <tile id="1" terrain="0,0,0,"/>
 <tile id="2" terrain="0,0,,0"/>
 <tile id="6" terrain="0,,0,0"/>
 <tile id="7" terrain=",0,0,0"/>
 <tile id="10" terrain=",,,0"/>
 <tile id="11" terrain=",,0,0"/>
 <tile id="12" terrain=",,0,"/>
 <tile id="15" terrain=",0,,0"/>
 <tile id="16" terrain="0,0,0,0"/>
 <tile id="17" terrain="0,,0,"/>
 <tile id="20" terrain=",0,,"/>
 <tile id="21" terrain="0,0,,"/>
 <tile id="22" terrain="0,,,"/>
 <tile id="25">
  <animation>
   <frame tileid="25" duration="500"/>
   <frame tileid="26" duration="500"/>
   <frame tileid="25" duration="500"/>
   <frame tileid="26" duration="500"/>
   <frame tileid="27" duration="500"/>
   <frame tileid="26" duration="500"/>
   <frame tileid="25" duration="500"/>
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
   <frame tileid="26" duration="500"/>
   <frame tileid="25" duration="500"/>
   <frame tileid="27" duration="500"/>
  </animation>
 </tile>
</tileset>
